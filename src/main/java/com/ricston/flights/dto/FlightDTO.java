/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import lombok.Data;

/**
 * Flight DTO.
 * 
 * @author floverde
 * @version 1.0
 */
@Data
public class FlightDTO implements Serializable
{
    /**
     * Flight code.
     */
    @JsonProperty("flight-code")
    private String flightCode;

    /**
     * Departure date.
     */
    @JsonProperty("departure-date")
    private LocalDateTime departureDate;
    
    /**
     * Airline name.
     */
    @JsonProperty("airline-name")
    private String airlineName;
    
    /**
     * Departure Airport IATA code.
     */
    @JsonProperty("departure-airport")
    private String departureAirport;
    
    /**
     * Destination Airport IATA code.
     */
    @JsonProperty("destination-airport")
    private String destinationAirport;
    
    /**
     * Aircraft type.
     */
    @JsonProperty("aircraft-type")
    private String aircraftType;
    
    /**
     * Number of seats on the flight.
     */
    @JsonProperty("seat-availability")
    private short seatAvailability;
    
    /**
     * Ticket price for this flight.
     */
    @JsonProperty("price")
    private BigDecimal price;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 9003659993309102037L;
}
