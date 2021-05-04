/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.errors;

/**
 * Defines the list of error codes for {@link
 * RicstonFlightException RicstonFlightExceptions}.
 * 
 * @author floverde
 * @version 1.0
 */
public enum RicstonFlightError
{
    /**
     * Unknown error.
     */
    UNKNOWN,
    
    /**
     * Indicates that there are not enough seats on a certain flight.
     */
    OUT_OF_SEATS,
    
    /**
     * Indicates that the set of passengers is empty.
     */
    NO_PASSENGERS,
    
    /**
     * Indicates that the searched route was not found.
     */
    ROUTE_NOT_FOUND,
    
    /**
     * Indicates that the searched flight was not found.
     */
    FLIGHT_NOT_FOUND,
    
    /**
     * Indicates that the searched airline was not found.
     */
    AIRLINE_NOT_FOUND,
    
    /**
     * Indicates that the type of aircraft does not exist.
     */
    AIRCRAFT_TYPE_NOT_FOUND,
    
    /**
     * Indicates that the flight code provided is malformed.
     */
    INVALID_FLIGHT_CODE,
    
    /**
     * Indicates that the page index is invalid.
     */
    INVALID_PAGE_INDEX,
    
    /**
     * Indicates an attempt to set a negative price for a flight.
     */
    NEGATIVE_FLIGHT_PRICE;
    
    /**
     * Gets the numeric error code.
     * 
     * @return numeric error code.
     */
    public final int getCode() {
        // Uses the ordinal index as an error code
        return this.ordinal();
    }
}
