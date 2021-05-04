/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.services.data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import lombok.Value;

/**
 * Flight Key.
 * 
 * @author floverde
 * @version 1.0
 */
@Value
public class FlightKey
{
    /**
     * Flight code.
     */
    private final String flightCode;
    
    /**
     * Departure date.
     */
    private final LocalDateTime departureDate;
    
    /**
     * Creates a {@code FlightKey} object that
     * encapsulates fields that uniquely identify a flight.
     * 
     * @param flightCode flight code.
     * @param departureDate departure date.
     */
    public FlightKey(final String flightCode, final LocalDateTime departureDate) {
        // Stores the value of the departure date by truncating it to minutes
        this.departureDate = departureDate.truncatedTo(ChronoUnit.MINUTES);
        // Stores the value of the flight code checking that it is not null
        this.flightCode = Objects.requireNonNull(flightCode);
    }
}
