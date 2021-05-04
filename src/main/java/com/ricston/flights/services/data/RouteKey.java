/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.services.data;

import java.io.Serializable;
import lombok.NonNull;
import lombok.Value;

/**
 * Route identification key using IATA codes.
 * 
 * @see https://en.wikipedia.org/wiki/Airline_codes#IATA_airline_designator
 * @see https://en.wikipedia.org/wiki/IATA_airport_code
 * @author floverde
 * @version 1.0
 */
@Value
public class RouteKey implements Serializable
{
    /**
     * Airline IATA Code.
     * 
     * @see https://en.wikipedia.org/wiki/Airline_codes#IATA_airline_designator
     */
    @NonNull
    private String airlineCode;

    /**
     * Source Airport IATA Code.
     * 
     * @see https://en.wikipedia.org/wiki/IATA_airport_code
     */
    @NonNull
    private String sourceAirportCode;

    /**
     * Destination Airport IATA Code.
     * 
     * @see https://en.wikipedia.org/wiki/IATA_airport_code
     */
    @NonNull
    private String destinationAirportCode;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -1804208715681312919L;
}
