/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.dto;

import java.math.BigDecimal;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Create Flight DTO.
 * 
 * @author floverde
 * @version 1.0
 */
@Data
public class CreateFlightDTO implements Serializable
{
    /**
     * Flight Code.
     */
    @NotEmpty
    @JsonProperty("flight-code")
    private String flightCode;
    
    /**
     * Departure Airport IATA Code.
     * 
     * @see https://en.wikipedia.org/wiki/IATA_airport_code
     */
    @NotEmpty
    @JsonProperty("departure-airport")
    @ApiModelProperty("Departure Airport IATA Code")
    private String departureAirport;

    /**
     * Destination Airport IATA Code.
     * 
     * @see https://en.wikipedia.org/wiki/IATA_airport_code
     */
    @NotEmpty
    @JsonProperty("destination-airport")
    @ApiModelProperty("Destination Airport IATA Code")
    private String destinationAirport;
    
    /**
     * Departure Date.
     */
    @NotNull
    @JsonProperty("departure-date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @ApiModelProperty(example = "1970-01-01T00:00")
    private LocalDateTime departureDate;
    
    /**
     * Aircraft Type.
     */
    @NotNull
    @JsonProperty("aircraft-type")
    private String aircraftType;
    
    /**
     * Number of seats on the flight.
     */
    @NotNull
    @Positive
    @JsonProperty("seat-availability")
    private Short seatAvailability;
    
    /**
     * Ticket price for this flight.
     */
    @NotNull
    @PositiveOrZero
    @JsonProperty("price")
    private BigDecimal price;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 6972490288415334631L;
}
