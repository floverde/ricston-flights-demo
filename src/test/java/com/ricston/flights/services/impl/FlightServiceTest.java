/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.services.impl;

import com.ricston.flights.dto.CreateFlightDTO;
import com.ricston.flights.dto.FlightDTO;
import com.ricston.flights.services.data.FlightKey;
import com.ricston.flights.dto.PurchaseTicketDTO;
import com.ricston.flights.dto.SearchFlightDTO;
import com.ricston.flights.dto.TicketDTO;
import com.ricston.flights.errors.RicstonFlightError;
import com.ricston.flights.errors.RicstonFlightException;
import com.ricston.flights.mappers.FlightMapper;
import com.ricston.flights.model.entities.Airline;
import com.ricston.flights.model.entities.Flight;
import com.ricston.flights.model.entities.Route;
import com.ricston.flights.model.repositories.AircraftTypeRepository;
import com.ricston.flights.model.repositories.FlightRepository;
import com.ricston.flights.services.TicketService;
import com.ricston.flights.services.RouteService;
import com.ricston.flights.services.data.RouteKey;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import org.mockito.InjectMocks;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;

/**
 * {@link FlightService} Tests.
 * 
 * @author floverde
 * @version 1.0
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class FlightServiceTest
{
    @Mock
    private RouteService routeService;
    
    @Mock
    private TicketService ticketService;
    
    @Spy
    private final FlightMapper flightMapper;
    
    @Mock
    private FlightRepository flightRepository;
    
    @Mock
    private AircraftTypeRepository aircraftTypeRepository;
    
    @InjectMocks
    private FlightServiceImpl flightService;
    
    /**
     * Defines a sample {@link LocalDateTime} value.
     */
    private static final LocalDateTime SAMPLE_DATETIME = LocalDateTime.of(2020, 3, 12, 10, 30);
    
    /**
     * Initialize the real dependencies used in these tests.
     */
    public FlightServiceTest() {
        // Gets the instance of the flight mapper
        this.flightMapper = Mappers.getMapper(FlightMapper.class);
    }
    
    /**
     * Builds a list of sample flights.
     * 
     * @return see description.
     */
    private static List<Flight> createSampleFlights() {
        final Flight flight1, flight2;
        // Defines the first sample flight
        flight1 = new Flight("FR0001", SAMPLE_DATETIME);
        flight1.setAirlineName("Ryanair");
        flight1.setDepartureAirport("LIS");
        flight1.setDestinationAirport("BRU");
        flight1.setAircraftType("Airbus A300");
        flight1.setSeatAvailability((short) 100);
        flight1.setPrice(BigDecimal.TEN);
        // Defines the second sample flight
        flight2 = new Flight("KM116", SAMPLE_DATETIME.plusHours(5));
        flight1.setAirlineName("Air Malta");
        flight1.setDepartureAirport("MLA");
        flight1.setDestinationAirport("LGW");
        flight1.setAircraftType("Airbus A319");
        flight1.setSeatAvailability((short) 123);
        flight1.setPrice(BigDecimal.valueOf(129));
        // Returns the list of sample flights
        return Arrays.asList(flight1, flight2);
    }
    
    /**
     * Test of search method of class FlightService, by checking that,
     * when searching for the same departure date, all the sample flights
     * are returned (since by default they share the same date).
     */
    @Test
    public void testSearch() {
        final SearchFlightDTO params;
        final LocalDateTime from, to;
        final Collection<Flight> inputFlights;
        final Collection<FlightDTO> outputFlights;
        final Collection<FlightDTO> inputFlightDTOs;
        
        log.info("RUNNING TEST: Search (case: simple)");
        
        // Create an instance of the search
        // parameters, setting the sample date
        params = new SearchFlightDTO();
        params.setDate(SAMPLE_DATETIME.toLocalDate());
        
        // Create a collection of example flights
        inputFlights = FlightServiceTest.createSampleFlights();
        // Defines the boundaries of the search date using the UTC time zone
        to = LocalDateTime.of(params.getDate(), LocalTime.MAX);
        from = LocalDateTime.of(params.getDate(), LocalTime.MIN);
        // Mock the repository call returning the list of example flights
        when(this.flightRepository.findAllByPkDepartureDateBetween(
                from, to)).thenReturn(inputFlights);
        // Gets the list of DTOs corresponding to the example flights
        inputFlightDTOs = inputFlights.stream().map(this.
                flightMapper::toDTO).collect(
                Collectors.toList());
        
        // EXECUTE the test method: "FlightService.search"
        outputFlights = this.flightService.search(params);
        
        // Check that the two lists are identical
        assertIterableEquals(inputFlightDTOs, outputFlights);
    }
    
    /**
     * Test of search method of class FlightService, by
     * checking that when no search date is provided,
     * all example flights are returned.
     */
    @Test
    public void testSearch_noSearchDate() {
        final SearchFlightDTO params;
        final List<Flight> inputFlights;
        final Collection<FlightDTO> outputFlights;
        final Collection<FlightDTO> inputFlightDTOs;
        
        log.info("RUNNING TEST: Search (case: no search date)");
        
        // Create an instance of the search parameters,
        // without setting the sample date
        params = new SearchFlightDTO();
        
        // Create a collection of example flights
        inputFlights = FlightServiceTest.createSampleFlights();
        // Mock the repository call returning the list of example flights
        when(this.flightRepository.findAll()).thenReturn(inputFlights);
        // Gets the list of DTOs corresponding to the example flights
        inputFlightDTOs = inputFlights.stream().map(this.
                flightMapper::toDTO).collect(
                Collectors.toList());
        
        // EXECUTE the test method: "FlightService.search"
        outputFlights = this.flightService.search(params);
        
        // Check that the two lists are identical
        assertIterableEquals(inputFlightDTOs, outputFlights);
    }
    
    /**
     * Test of create method of class FlightService, by
     * checking that the creation procedure works correctly.
     */
    @Test
    public void testCreate_success() {
        final Route route;
        final Flight flight;
        final Airline airline;
        final RouteKey routeKey;
        final CreateFlightDTO params;
        final FlightDTO expectedOutput, actualOutput;
        
        log.info("RUNNING TEST: Create (case: success)");
        
        // Instantiates the creation parameters
        params = new CreateFlightDTO();
        params.setFlightCode("LH001");
        params.setDepartureAirport("EDI");
        params.setDestinationAirport("FRA");
        params.setDepartureDate(SAMPLE_DATETIME);
        params.setSeatAvailability((short) 100);
        params.setAircraftType("Airbus A320");
        params.setPrice(BigDecimal.TEN);
        
        // Create a sample airline
        airline = new Airline();
        airline.setName("Lufthansa");
        
        // Get the route key from the flight creation parameters
        routeKey = this.flightMapper.toRouteKey(params);
        
        // Create a sample route
        route = new Route();
        route.setAirlineCode(routeKey.getAirlineCode());
        route.setSourceAirport(routeKey.getSourceAirportCode());
        route.setDestinationAirport(routeKey.getDestinationAirportCode());
        route.setAirline(airline);
        
        // Computes the fields of the expected output flight
        flight = this.flightMapper.fromCreationParams(params);
        flight.setAirlineName(airline.getName());
        expectedOutput = this.flightMapper.toDTO(flight);

        // Mock up the call to the flight repository
        when(this.flightRepository.save(flight)).thenReturn(flight);
        // Mock up the call to the aircraft type repository
        when(this.aircraftTypeRepository.existsByModel(params.
                getAircraftType())).thenReturn(true);
        // Mock up the call to the route service
        when(this.routeService.getByKey(routeKey)).
                thenReturn(Optional.of(route));
        
        // EXECUTE the test method: "FlightService.create"
        actualOutput = this.flightService.create(params);
  
        // Check that the two flights are the same
        assertEquals(expectedOutput, actualOutput);
    }
    
    /**
     * Test of create method of class FlightService, by checking that
     * the procedure fails because of the non-existent aircraft model.
     */
    @Test
    public void testCreate_invalidAricraftModel() {
        final CreateFlightDTO params;
        final RicstonFlightException ex;
        log.info("RUNNING TEST: Create (case: invalid aircraft model)");
        
        // Instantiates the creation parameters
        params = new CreateFlightDTO();
        params.setFlightCode("LH001");
        params.setDepartureAirport("EDI");
        params.setDestinationAirport("FRA");
        params.setDepartureDate(SAMPLE_DATETIME);
        params.setSeatAvailability((short) 100);
        params.setAircraftType("Airbus A320");
        params.setPrice(BigDecimal.TEN);
        
        // Mock up the call to the aircraft type repository,
        // indicating that the aircraft type does not exist
        when(this.aircraftTypeRepository.existsByModel(params.
                getAircraftType())).thenReturn(false);
        
        // EXECUTE the test method: "FlightService.create"
        ex = assertThrows(RicstonFlightException.class,
                () -> this.flightService.create(params));
        
        // Check that the error message refers to the test case
        assertEquals(RicstonFlightError.AIRCRAFT_TYPE_NOT_FOUND, ex.getCode());
    }
    
    /**
     * Test of create method of class FlightService, by checking
     * that the procedure fails because the route doesn't exist.
     */
    @Test
    public void testCreate_invalidRoute() {
        final RouteKey routeKey;
        final CreateFlightDTO params;
        final RicstonFlightException ex;
        log.info("RUNNING TEST: Create (case: invalid aircraft model)");
        
        // Instantiates the creation parameters
        params = new CreateFlightDTO();
        params.setFlightCode("LH001");
        params.setDepartureAirport("EDI");
        params.setDestinationAirport("FRA");
        params.setDepartureDate(SAMPLE_DATETIME);
        params.setSeatAvailability((short) 100);
        params.setAircraftType("Airbus A320");
        params.setPrice(BigDecimal.TEN);
        
        // Get the route key from the flight creation parameters
        routeKey = this.flightMapper.toRouteKey(params);
        
        // Mock up the call to the aircraft type repository
        when(this.aircraftTypeRepository.existsByModel(params.
                getAircraftType())).thenReturn(true);
        
        // Mock up the call to the route service, indicating that
        // there is no route corresponding to those parameters
        when(this.routeService.getByKey(routeKey)).
                thenReturn(Optional.empty());
        
        // EXECUTE the test method: "FlightService.create"
        ex = assertThrows(RicstonFlightException.class,
                () -> this.flightService.create(params));
        
        // Check that the error message refers to the test case
        assertEquals(RicstonFlightError.ROUTE_NOT_FOUND, ex.getCode());
    }
    
    /**
     * Test of purchaseTicket method of class FlightService, by
     * checking that the purchase procedure works correctly.
     */
    @Test
    public void testPurchaseTicket_success() {
        final Flight flight;
        final FlightKey key;
        final FlightDTO flightDTO;
        final TicketDTO ticketDTO;
        final PurchaseTicketDTO params;
        
        log.info("RUNNING TEST: PurchaseTicket (case: success)");
        
        // Create ticket purchase parameters
        params = new PurchaseTicketDTO();
        params.setPassengers(Collections.singleton("Passenger"));
        
        // Create key parameters to identify the flight
        key = new FlightKey("LH001", SAMPLE_DATETIME);
        
        // Create a sample flight for which to purchase a ticket
        flight = new Flight(new Flight.PK());
        flight.getPk().setFlightCode(key.getFlightCode());
        flight.getPk().setDepartureDate(key.getDepartureDate());
        flight.setSeatAvailability((short) 100);
        flight.setAircraftType("Airbus A320");
        flight.setDestinationAirport("FRA");
        flight.setAirlineName("Lufthansa");
        flight.setDepartureAirport("EDI");
        flight.setPrice(BigDecimal.TEN);
        
        // Computes the fields of the output FlightDTO object
        flightDTO = this.flightMapper.toDTO(flight);
        flightDTO.setSeatAvailability((short) 99);
        // Creates a ticket that aggregates the passengers
        // and the flight with which it is associated
        ticketDTO = new TicketDTO("123456", params.
                getPassengers(), flightDTO);
        
        // Mock the call to the flight repository that saves the changes
        when(this.flightRepository.save(flight)).thenReturn(flight);
        // Mock the call to the flight repository that queries it by key
        when(this.flightRepository.findById(flight.getPk())).
                thenReturn(Optional.of(flight));
        // Mock the call to the ticket service
        when(this.ticketService.generate(flightDTO, params.
                getPassengers())).thenReturn(ticketDTO);
        
        // EXECUTE the test method: "FlightService.purchaseTicket"
        this.flightService.purchaseTicket(key, params);
    }
    
    /**
     * Test of purchaseTicket method of class FlightService, by
     * checking that the procedure fails because there is no flight.
     */
    @Test
    public void testPurchaseTicket_flightNotFound() {
        final FlightKey key;
        final PurchaseTicketDTO params;
        final RicstonFlightException ex;
        
        log.info("RUNNING TEST: PurchaseTicket (case: flight not found)");
        
        // Create ticket purchase parameters
        params = new PurchaseTicketDTO();
        params.setPassengers(Collections.singleton("Passenger"));
        
        // Create key parameters to identify the flight
        key = new FlightKey("LH???", LocalDateTime.MIN);
        
        // Mock call to the flight repository indicating that no flight exists
        when(this.flightRepository.findById(any())).thenReturn(Optional.empty());
        
        // EXECUTE the test method: "FlightService.purchaseTicket"
        ex = assertThrows(RicstonFlightException.class, () ->
                this.flightService.purchaseTicket(key, params));
        
        // Check that the error message refers to the test case
        assertEquals(RicstonFlightError.FLIGHT_NOT_FOUND, ex.getCode());
    }
    
    /**
     * Test of purchaseTicket method of class FlightService, by
     * cchecking that the procedure fails due to sold-out seats.
     */
    @Test
    public void testPurchaseTicket_soldOut() {
        final Flight flight;
        final FlightKey key;
        final PurchaseTicketDTO params;
        final RicstonFlightException ex;
        
        log.info("RUNNING TEST: PurchaseTicket (case: sold-out)");
        
        // Create ticket purchase parameters
        params = new PurchaseTicketDTO();
        params.setPassengers(new HashSet<>());
        params.getPassengers().add("Passenger01");
        params.getPassengers().add("Passenger02");
        
        // Create key parameters to identify the flight
        key = new FlightKey("LH001", SAMPLE_DATETIME);
        
        // Create a sample flight for which to purchase a ticket
        // (note: the flight is declared with 1 seats)
        flight = new Flight(new Flight.PK());
        flight.getPk().setFlightCode(key.getFlightCode());
        flight.getPk().setDepartureDate(key.getDepartureDate());
        flight.setSeatAvailability((short) 1);
        flight.setAircraftType("Airbus A320");
        flight.setDestinationAirport("FRA");
        flight.setAirlineName("Lufthansa");
        flight.setDepartureAirport("EDI");
        flight.setPrice(BigDecimal.TEN);
        
        // Mock the call to the flight repository that queries it by key
        when(this.flightRepository.findById(flight.getPk())).
                thenReturn(Optional.of(flight));
        
        // EXECUTE the test method: "FlightService.purchaseTicket"
        ex = assertThrows(RicstonFlightException.class, () ->
                this.flightService.purchaseTicket(key, params));
        
        // Check that the error message refers to the test case
        assertEquals(RicstonFlightError.OUT_OF_SEATS, ex.getCode());
    }
    
    /**
     * Test of changePrice method of class FlightService, by
     * checking that the update procedure works correctly.
     */
    @Test
    public void testChangePrice_success() {
        final Flight flight;
        final FlightKey key;
        final BigDecimal price, delta;
        final FlightDTO expectedOut, actualOut;
        
        log.info("RUNNING TEST: ChangePrice (case: success)");
        
        // Declare the amount of the price change
        delta = BigDecimal.ONE.negate();
        // Declare the price of the flight
        price = BigDecimal.TEN;
        
        // Create key parameters to identify the flight
        key = new FlightKey("LH001", SAMPLE_DATETIME);
        
        // Create a sample flight on which to change the price.
        flight = new Flight(new Flight.PK());
        flight.getPk().setFlightCode(key.getFlightCode());
        flight.getPk().setDepartureDate(key.getDepartureDate());
        flight.setSeatAvailability((short) 100);
        flight.setAircraftType("Airbus A320");
        flight.setDestinationAirport("FRA");
        flight.setAirlineName("Lufthansa");
        flight.setDepartureAirport("EDI");
        flight.setPrice(price);
        
        // Computes the fields of the output FlightDTO object
        expectedOut = this.flightMapper.toDTO(flight);
        expectedOut.setPrice(price.add(delta));
        
        // Mock the call to the flight repository that saves the changes
        when(this.flightRepository.save(flight)).thenReturn(flight);
        // Mock the call to the flight repository that queries it by key
        when(this.flightRepository.findById(flight.getPk())).
                thenReturn(Optional.of(flight));
        
        // EXECUTE the test method: "FlightService.changePrice"
        actualOut = this.flightService.changePrice(key, delta);
        
        // Check that the two flights are the same
        assertEquals(expectedOut, actualOut);
    }
    
    /**
     * Test of changePrice method of class FlightService,
     * by checking that the procedure fails because the
     * new price would have a negative value.
     */
    @Test
    public void testChangePrice_belowZero() {
        final Flight flight;
        final FlightKey key;
        final BigDecimal price, delta;
        final RicstonFlightException ex;
        
        log.info("RUNNING TEST: ChangePrice (case: below zero)");
        
        // Declare the amount of the price change
        delta = BigDecimal.valueOf(-20);
        // Declare the price of the flight
        price = BigDecimal.TEN;
        
        // Create key parameters to identify the flight
        key = new FlightKey("LH001", SAMPLE_DATETIME);
        
        // Create a sample flight on which to change the price.
        flight = new Flight(new Flight.PK());
        flight.getPk().setFlightCode(key.getFlightCode());
        flight.getPk().setDepartureDate(key.getDepartureDate());
        flight.setSeatAvailability((short) 100);
        flight.setAircraftType("Airbus A320");
        flight.setDestinationAirport("FRA");
        flight.setAirlineName("Lufthansa");
        flight.setDepartureAirport("EDI");
        flight.setPrice(price);
        
        // Mock the call to the flight repository that queries it by key
        when(this.flightRepository.findById(flight.getPk())).
                thenReturn(Optional.of(flight));
        
        // EXECUTE the test method: "FlightService.changePrice"
        ex = assertThrows(RicstonFlightException.class, () ->
                this.flightService.changePrice(key, delta));
        
        // Check that the error message refers to the test case
        assertEquals(RicstonFlightError.NEGATIVE_FLIGHT_PRICE, ex.getCode());
    }
}
