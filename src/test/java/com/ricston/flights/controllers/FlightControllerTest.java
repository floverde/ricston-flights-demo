/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricston.flights.dto.ChangePriceDTO;
import com.ricston.flights.dto.CreateFlightDTO;
import com.ricston.flights.dto.FlightDTO;
import com.ricston.flights.services.data.FlightKey;
import com.ricston.flights.dto.PurchaseTicketDTO;
import com.ricston.flights.dto.SearchFlightDTO;
import com.ricston.flights.dto.TicketDTO;
import com.ricston.flights.errors.RicstonFlightError;
import com.ricston.flights.errors.RicstonFlightExceptions;
import com.ricston.flights.errors.RicstonValidationException;
import com.ricston.flights.services.FlightService;
import com.ricston.flights.mappers.FlightMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import java.util.Collections;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;

/**
 * {@link FlightController} tests.
 * 
 * @author floverde
 * @version 1.0
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(FlightController.class)
public class FlightControllerTest
{
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private FlightService service;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Spy
    private final FlightMapper mappper;
    
    /**
     * Initialize the real dependencies used in these tests.
     */
    public FlightControllerTest() {
        // Gets the instance of the flight mapper
        this.mappper = Mappers.getMapper(FlightMapper.class);
    }
    
    /**
     * Test of search method of class FlightController.
     * <p>Scenario: Search successfully</p>
     * <p>Output-payload: list of all flights.</p>
     * <p>Output-status: HTTP 200 OK.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testSearch_success() throws Exception {
        final FlightDTO flight;
        final String departureDate;
        final SearchFlightDTO params;
        final Collection<FlightDTO> flights;
        
        log.info("RUNNING REST TEST: Search (case: success)");
        
        flight = new FlightDTO();
        flight.setFlightCode("FR0001");
        flight.setAirlineName("Ryanair");
        flight.setDepartureAirport("LIS");
        flight.setDestinationAirport("BRU");
        departureDate = "2017-04-12T10:35:00";
        flight.setDepartureDate(LocalDateTime.parse(departureDate));
        flight.setPrice(BigDecimal.valueOf(52.25D));
        flight.setSeatAvailability((short) 100);
        flight.setAircraftType("Airbus A300");
        
        params = new SearchFlightDTO();
        flights = Collections.singleton(flight);
        params.setDate(flight.getDepartureDate().toLocalDate());
        when(this.service.search(params)).thenReturn(flights);
        
        this.mvc.perform(get("/flights").accept(MediaType.APPLICATION_JSON).
                queryParam("date", departureDate.substring(0, 10))).andDo(print()).
                andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].departure-date", is(departureDate))).
                andExpect(jsonPath("$[0].flight-code", is(flight.getFlightCode()))).
                andExpect(jsonPath("$[0].airline-name", is(flight.getAirlineName()))).
                andExpect(jsonPath("$[0].price", is(flight.getPrice().doubleValue()))).
                andExpect(jsonPath("$[0].aircraft-type", is(flight.getAircraftType()))).
                andExpect(jsonPath("$[0].departure-airport", is(flight.getDepartureAirport()))).
                andExpect(jsonPath("$[0].destination-airport", is(flight.getDestinationAirport()))).
                andExpect(jsonPath("$[0].destination-airport", is(flight.getDestinationAirport()))).
                andExpect(jsonPath("$[0].seat-availability", is((int) flight.getSeatAvailability())));
    }
    
    /**
     * Test of search method of class FlightController.
     * <p>Scenario: No flight available.</p>
     * <p>Output-payload: empty list.</p>
     * <p>Output-status: HTTP 200 OK.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testSearch_noFlights() throws Exception {
        log.info("RUNNING REST TEST: GetDestinations (case: airline not found)");
        
        when(this.service.search(any())).thenReturn(Collections.emptyList());
        
        this.mvc.perform(get("/flights").accept(MediaType.APPLICATION_JSON)).andDo(
                print()).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }
    
    /**
     * Test of create method of class FlightController.
     * <p>Scenario: Create successfully</p>
     * <p>Input-payload: creation parameters.</p>
     * <p>Output-payload: newly created flight.</p>
     * <p>Output-status: HTTP 201 OK.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testCreate_success() throws Exception {
        final FlightDTO flight;
        final String departureDate;
        final CreateFlightDTO params;
        
        log.info("RUNNING REST TEST: Create (case: success)");
        
        flight = new FlightDTO();
        flight.setFlightCode("FR0001");
        flight.setAirlineName("Ryanair");
        flight.setDepartureAirport("LIS");
        flight.setDestinationAirport("BRU");
        departureDate = "2017-04-12T10:35:00";
        flight.setDepartureDate(LocalDateTime.parse(departureDate));
        flight.setPrice(BigDecimal.valueOf(52.25D));
        flight.setSeatAvailability((short) 100);
        flight.setAircraftType("Airbus A300");
        
        params = new CreateFlightDTO();
        params.setFlightCode(flight.getFlightCode());
        params.setAircraftType(flight.getAircraftType());
        params.setDepartureAirport(flight.getDepartureAirport());
        params.setDestinationAirport(flight.getDestinationAirport());
        params.setSeatAvailability(flight.getSeatAvailability());
        params.setDepartureDate(flight.getDepartureDate());
        params.setPrice(flight.getPrice());
        
        when(this.service.create(params)).thenReturn(flight);
        
        this.mvc.perform(post("/flights").accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(jsonContent(params))).
                andDo(print()).andExpect(status().isCreated()).
                andExpect(jsonPath("$.departure-date", is(departureDate))).
                andExpect(jsonPath("$.flight-code", is(flight.getFlightCode()))).
                andExpect(jsonPath("$.airline-name", is(flight.getAirlineName()))).
                andExpect(jsonPath("$.price", is(flight.getPrice().doubleValue()))).
                andExpect(jsonPath("$.aircraft-type", is(flight.getAircraftType()))).
                andExpect(jsonPath("$.departure-airport", is(flight.getDepartureAirport()))).
                andExpect(jsonPath("$.destination-airport", is(flight.getDestinationAirport()))).
                andExpect(jsonPath("$.destination-airport", is(flight.getDestinationAirport()))).
                andExpect(jsonPath("$.seat-availability", is((int) flight.getSeatAvailability())));
    }
    
    /**
     * Test of create method of class FlightController.
     * <p>Scenario: invalid creation parameters.</p>
     * <p>Input-payload: creation parameters.</p>
     * <p>Output-payload: error details.</p>
     * <p>Output-status: HTTP 400 BAD REQUEST.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testCreate_badRequest() throws Exception {
        log.info("RUNNING REST TEST: Create (case: bad request)");
        
        when(this.service.create(any())).thenThrow(RicstonValidationException.class);
        
        this.mvc.perform(post("/flights").accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content("{}")).
                andDo(print()).andExpect(status().isBadRequest());
    }
    
    /**
     * Test of purchaseTicket method of class FlightController.
     * <p>Scenario: Successfully purchased.</p>
     * <p>Input-URL-param: flight code.</p>
     * <p>Input-query-param: departure date.</p>
     * <p>Input-payload: purchasing parameters.</p>
     * <p>Output-payload: newly created flight.</p>
     * <p>Output-status: HTTP 201 OK.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testPurchaseTicket_success() throws Exception {
        final FlightDTO flight;
        final FlightKey key;
        final TicketDTO ticket;
        final String departureDate;
        final PurchaseTicketDTO params;
        
        log.info("RUNNING REST TEST: purchaseTicket (case: success)");
        
        params = new PurchaseTicketDTO();
        params.setPassengers(Collections.singleton("Passenger"));
        
        flight = new FlightDTO();
        flight.setFlightCode("FR0001");
        departureDate = "2017-04-12T10:35:00";
        flight.setDepartureDate(LocalDateTime.parse(departureDate));
        
        key = new FlightKey(flight.getFlightCode(), flight.getDepartureDate());
        
        ticket = new TicketDTO("123456", params.getPassengers(), flight);
        
        when(this.service.purchaseTicket(key, params)).thenReturn(ticket);
        
        this.mvc.perform(post("/flights/{flightCode}/ticket", key.getFlightCode()).queryParam(
                "departure-date", departureDate.substring(0, 16)).accept(MediaType.
                APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(
                jsonContent(params))).andDo(print()).andExpect(status().isCreated()).
                andExpect(jsonPath("$.code", anything())).
                andExpect(jsonPath("$.passengers", hasSize(1))).
                andExpect(jsonPath("$.flight.departure-date", is(departureDate))).
                andExpect(jsonPath("$.flight.flight-code", is(flight.getFlightCode())));
    }
    
    /**
     * Test of purchaseTicket method of class FlightController.
     * <p>Scenario: Flight not found.</p>
     * <p>Input-URL-param: flight code.</p>
     * <p>Input-query-param: departure date.</p>
     * <p>Input-payload: purchasing parameters.</p>
     * <p>Output-payload: error details.</p>
     * <p>Output-status: HTTP 404 NOT FOUND.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testPurchaseTicket_flightNotFound() throws Exception {
        final FlightKey key;
        final String departureDate;
        final PurchaseTicketDTO params;
        
        log.info("RUNNING REST TEST: purchaseTicket (case: flight not found)");
        
        departureDate = "2017-04-12T10:35:00";
        key = new FlightKey("FR99999", LocalDateTime.parse(departureDate));
        
        params = new PurchaseTicketDTO();
        params.setPassengers(Collections.singleton("Passenger"));
        
        when(this.service.purchaseTicket(key, params)).thenThrow(
                RicstonFlightExceptions.flightNotFound(key));
        
        this.mvc.perform(post("/flights/{flightCode}/ticket", key.getFlightCode()).
                queryParam("departure-date", departureDate.substring(0, 16)).accept(
                MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).
                content(jsonContent(params))).andDo(print()).andExpect(status().isNotFound()).
                andExpect(jsonPath("$.code", is(RicstonFlightError.FLIGHT_NOT_FOUND.getCode())));
    }
    
    /**
     * Test of purchaseTicket method of class FlightController.
     * <p>Scenario: Sold-out (no more seats).</p>
     * <p>Input-URL-param: flight code.</p>
     * <p>Input-query-param: departure date.</p>
     * <p>Input-payload: purchasing parameters.</p>
     * <p>Output-payload: error details.</p>
     * <p>Output-status: HTTP 400 BAD REQUEST.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testPurchaseTicket_soldOut() throws Exception {
        final FlightKey key;
        final String departureDate;
        final PurchaseTicketDTO params;
        
        log.info("RUNNING REST TEST: purchaseTicket (case: sold-out)");
        
        departureDate = "2017-04-12T10:35:00";
        key = new FlightKey("FR0001", LocalDateTime.parse(departureDate));
        
        params = new PurchaseTicketDTO();
        params.setPassengers(Collections.singleton("Passenger"));
        
        when(this.service.purchaseTicket(any(), eq(params))).thenThrow(
                RicstonFlightExceptions.outOfSeats(key.getFlightCode(),
                (short) 1, (short) 0));
        
        this.mvc.perform(post("/flights/{flightCode}/ticket", key.getFlightCode()).
                queryParam("departure-date", departureDate.substring(0, 16)).accept(
                MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).
                content(jsonContent(params))).andDo(print()).andExpect(status().isConflict()).
                andExpect(jsonPath("$.code", is(RicstonFlightError.OUT_OF_SEATS.getCode())));
    }
    
    /**
     * Test of changePrice method of class FlightController.
     * <p>Scenario: Successfully changed.</p>
     * <p>Input-URL-param: flight code.</p>
     * <p>Input-query-param: departure date.</p>
     * <p>Input-payload: increasing/descreasing value.</p>
     * <p>Output-payload: newly created flight.</p>
     * <p>Output-status: HTTP 200 OK.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testChangePrice_success() throws Exception {
        final FlightDTO flight;
        final FlightKey key;
        final String departureDate;
        final ChangePriceDTO params;
        
        log.info("RUNNING REST TEST: ChangePrice (case: success)");
        
        flight = new FlightDTO();
        flight.setFlightCode("FR0001");
        departureDate = "2017-04-12T10:35:00";
        flight.setPrice(BigDecimal.valueOf(31.25D));
        flight.setDepartureDate(LocalDateTime.parse(departureDate));
        
        key = new FlightKey(flight.getFlightCode(), flight.getDepartureDate());
        
        params = new ChangePriceDTO(BigDecimal.valueOf(-1.25D));
        
        when(this.service.changePrice(key, params.getDelta())).thenReturn(flight);
        
        this.mvc.perform(patch("/flights/{flightCode}/price", key.getFlightCode()).queryParam(
                "departure-date", departureDate.substring(0, 16)).accept(MediaType.
                APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(
                jsonContent(params))).andDo(print()).andExpect(status().isOk()).
                andExpect(jsonPath("$.departure-date", is(departureDate))).
                andExpect(jsonPath("$.flight-code", is(flight.getFlightCode()))).
                andExpect(jsonPath("$.price", is(flight.getPrice().doubleValue())));
    }
    
    /**
     * Test of changePrice method of class FlightController.
     * <p>Scenario: Flight not found.</p>
     * <p>Input-URL-param: flight code.</p>
     * <p>Input-query-param: departure date.</p>
     * <p>Input-payload: increasing/descreasing value.</p>
     * <p>Output-payload: error details.</p>
     * <p>Output-status: HTTP 404 NOT FOUND.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testChangePrice_flightNotFound() throws Exception {
        final FlightKey key;
        final String departureDate;
        final ChangePriceDTO params;
        
        log.info("RUNNING REST TEST: ChangePrice (case: flight not found)");
        
        departureDate = "2017-04-12T10:35";
        key = new FlightKey("FR99999", LocalDateTime.parse(departureDate));
        
        params = new ChangePriceDTO(BigDecimal.valueOf(-1.25D));
        
        when(this.service.changePrice(key, params.getDelta())).thenThrow(
                RicstonFlightExceptions.flightNotFound(key));
        
        this.mvc.perform(patch("/flights/{flightCode}/price", key.getFlightCode()).
                queryParam("departure-date", departureDate).accept(MediaType.
                APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(
                jsonContent(params))).andDo(print()).andExpect(status().isNotFound()).
                andExpect(jsonPath("$.code", is(RicstonFlightError.FLIGHT_NOT_FOUND.getCode())));
    }
    
    /**
     * Test of changePrice method of class FlightController.
     * <p>Scenario: Attempt to set a negative price.</p>
     * <p>Input-URL-param: flight code.</p>
     * <p>Input-query-param: departure date.</p>
     * <p>Input-payload: increasing/descreasing value.</p>
     * <p>Output-payload: error details.</p>
     * <p>Output-status: HTTP 400 BAD REQUEST.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testChangePrice_belowZero() throws Exception {
        final FlightKey key;
        final String departureDate;
        final ChangePriceDTO params;
        
        log.info("RUNNING REST TEST: ChangePrice (case: below zero)");
        
        departureDate = "2017-04-12T10:35";
        key = new FlightKey("FR0001", LocalDateTime.parse(departureDate));
        
        params = new ChangePriceDTO(BigDecimal.valueOf(-20D));
        
        when(this.service.changePrice(key, params.getDelta())).thenThrow(
                RicstonFlightExceptions.invalidFlightPriceReduction(
                key.getFlightCode(), 10, -20));
        
        this.mvc.perform(patch("/flights/{flightCode}/price", key.getFlightCode()).
                queryParam("departure-date", departureDate).accept(MediaType.
                APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(
                jsonContent(params))).andDo(print()).andExpect(status().isConflict()).
                andExpect(jsonPath("$.code", is(RicstonFlightError.NEGATIVE_FLIGHT_PRICE.getCode())));
    }
    
    /**
     * Utility method for serializing an object in JSON.
     * 
     * @param payload object to be serialised in JSON.
     * @return string representing the object provided in JSON format.
     * @throws JsonProcessingException in case of errors during serialisation.
     */
    private String jsonContent(final Object payload) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(payload);
    }
}