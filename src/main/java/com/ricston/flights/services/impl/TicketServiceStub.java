/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.services.impl;

import com.ricston.flights.dto.FlightDTO;
import com.ricston.flights.dto.TicketDTO;
import com.ricston.flights.errors.RicstonFlightExceptions;
import com.ricston.flights.services.TicketService;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.CollectionUtils;
import java.util.Set;

/**
 * Stub implementation of {@link TicketService}.
 * 
 * @author floverde
 * @version 1.0
 */
@Service
public class TicketServiceStub implements TicketService
{
    /**
     * {@inheritDoc}
     */
    @Override
    public TicketDTO generate(final FlightDTO flight, final Set<String> passengers) {
        final String code;
        // Check that the flight is not null
        Objects.requireNonNull(flight);
        // Check if the passenger list is empty
        if (CollectionUtils.isEmpty(passengers)) {
            // Raises an exception indicating that
            // the passenger list cannot be empty
            throw RicstonFlightExceptions.atLeastOnePassenger(
                    flight.getFlightCode());
        }
        // Randomly generates a 10-character alphanumeric string
        code = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        // Combine this information to generate the ticket
        return new TicketDTO(code, passengers, flight);
    }
}
