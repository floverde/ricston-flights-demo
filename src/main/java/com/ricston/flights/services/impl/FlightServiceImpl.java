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
import com.ricston.flights.errors.RicstonFlightExceptions;
import com.ricston.flights.model.entities.Flight;
import com.ricston.flights.model.repositories.FlightRepository;
import com.ricston.flights.services.FlightService;
import com.ricston.flights.mappers.FlightMapper;
import com.ricston.flights.model.entities.Route;
import com.ricston.flights.model.repositories.AircraftTypeRepository;
import com.ricston.flights.services.RouteService;
import com.ricston.flights.services.TicketService;
import com.ricston.flights.services.data.RouteKey;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import java.util.Collection;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Implementation of {@link FlightService}.
 * 
 * @author floverde
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService
{
    /**
     * Flight mapper reference.
     */
    private final FlightMapper mapper;
    
    /**
     * Route service reference.
     */
    private final RouteService routeService;
    
    /**
     * Ticket service reference.
     */
    private final TicketService ticketService;
    
    /**
     * Flight repository reference.
     */
    private final FlightRepository flightRepository;
    
    /**
     * Aircraft type repository reference.
     */
    private final AircraftTypeRepository aircraftTypeRepository;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<FlightDTO> search(final SearchFlightDTO params) {
        final Collection<Flight> flights;
        // Check if the date filter has been provided
        if (params.getDate() != null) {
            // Retrieve all flights that depart on a certain date
            flights = this.flightRepository.findAllByPkDepartureDateBetween(
                    LocalDateTime.of(params.getDate(), LocalTime.MIN),
                    LocalDateTime.of(params.getDate(), LocalTime.MAX));
        } else {
            // Retrieve all available flights
            flights = this.flightRepository.findAll();
        }
        // Convert the results into a list of DTOs
        return flights.stream().map(this.mapper::toDTO).
                collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FlightDTO create(final CreateFlightDTO params) {
        final Route route;
        final Flight flight;
        final RouteKey routeKey;
        // Get the route key from the flight creation parameters
        routeKey = this.mapper.toRouteKey(params);
        // Check if the aircraft model does not exist on the repository
        if (!this.aircraftTypeRepository.existsByModel(params.getAircraftType())) {
            // Raises an exception indicating that the aircraft type does not exist
            throw RicstonFlightExceptions.invalidAircraftType(params.getAircraftType());
        }
        // Retrieves the route that the new flight will follow
        route = this.routeService.getByKey(routeKey).orElseThrow(() ->
                RicstonFlightExceptions.invalidFlightRoute(routeKey));
        // Initialize a new flight using the creation parameters
        flight = this.mapper.fromCreationParams(params);
        // Set the name of the airline (defined in the route)
        flight.setAirlineName(route.getAirline().getName());
        // Creates the new flight on the repository and returns its DTO
        return this.mapper.toDTO(this.flightRepository.save(flight));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TicketDTO purchaseTicket(final FlightKey key, final PurchaseTicketDTO params) {
        final Flight flight;
        final short seatRequired;
        final FlightDTO flightDTO;
        // Retrieve the flight for which to purchase a ticket
        flight = this.findByKey(key).orElseThrow(() ->
                RicstonFlightExceptions.flightNotFound(key));
        // Retrieve the number of passengers for this ticket
        seatRequired = (short) params.getPassengers().size();
        // Check that there are enough seats on this flight
        if (seatRequired > flight.getSeatAvailability()) {
            // Raise an exception indicating that there are not enough seats on this flight
            throw RicstonFlightExceptions.outOfSeats(key.getFlightCode(),
                    seatRequired, flight.getSeatAvailability());
        }
        // Update the number of seats by subtracting those required for this ticket
        flight.setSeatAvailability((short) (flight.getSeatAvailability() - seatRequired));
        // Persists the changes in the repository and derives the DTO of the flight
        flightDTO = this.mapper.toDTO(this.flightRepository.save(flight));
        // Invoke the service to issue the ticket from this flight
        return this.ticketService.generate(flightDTO, params.getPassengers());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FlightDTO changePrice(final FlightKey key, final BigDecimal delta) {
        final Flight flight;
        final BigDecimal newPrice;
        // Retrieves the flight that needs to be modified
        flight = this.findByKey(key).orElseThrow(() ->
                RicstonFlightExceptions.flightNotFound(key));
        // Calculate the new price for this flight
        newPrice = flight.getPrice().add(delta);
        // Check that the price is not a negative value
        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            // Raise an exception indicating that the price cannot be reduced further
            throw RicstonFlightExceptions.invalidFlightPriceReduction(
                    key.getFlightCode(), flight.getPrice(), delta);
        }
        // Set the updated flight price
        flight.setPrice(newPrice);
        // Persists the changes in the repository and derives the DTO of the flight
        return this.mapper.toDTO(this.flightRepository.save(flight));
    }
    
    /**
     * Internal method to retrieve a flight through its key.
     * 
     * @param key key parameters that identify a flight.
     * @return flight identified by these parameters.
     */
    private Optional<Flight> findByKey(final FlightKey key) {
        // Gets the flight by specifying all fields of the primary key
        return this.flightRepository.findById(new Flight.PK(key.getFlightCode(),
                key.getDepartureDate().truncatedTo(ChronoUnit.MINUTES)));
    }
}
