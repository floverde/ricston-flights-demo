/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.errors;

import com.ricston.flights.services.data.FlightKey;
import com.ricston.flights.services.data.RouteKey;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

/**
 * Defines some method factory for {@link
 * RicstonFlightException RicstonFlightExceptions}.
 * 
 * @author floverde
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.NONE)
public final class RicstonFlightExceptions
{
    /**
     * Exception message indicating an excessive reduction in the price of the flight.
     */
    private static final String INVALID_FLIGHT_PRICE_REDUCTION = "The price of flight"
            + " \"%s\" cannot be reduced any further (current: %s, reduction: %s).";
    
    /**
     * Exception message indicating that a certain route does not exist.
     */
    private static final String INVALID_FLIGHT_ROUTE = "No route corresponding"
            + " to the following parameters: (airline-code: \"%s\", source-"
            + "airport-code: \"%s\", destination-airport-code: \"%s\")";
    
    /**
     * Exception message indicating that at least one passenger is needed to issue a ticket.
     */
    private static final String AT_LEAST_ONE_PASSENGER = "At least one "
            + "passenger is required to issue a ticket (flight-code: %s).";
    
    /**
     * Exception message indicating a shortage of seats on a given flight.
     */
    private static final String OUT_OF_SEATS = "There are not enough "
            + "seats for flight \"%s\" (available: %d, requested %d).";
    
    /**
     * Exception message indicating that the page number must be greater than one.
     */
    private static final String NO_POSITIVE_PAGE_INDEX = "Page "
            + "index must be a number greater than one.";
    
    /**
     * Exception message indicating that a certain flight
     * (identified by code and departure date) has not been found.
     */
    private static final String FLIGHT_NOT_FOUND = "No flight"
            + " with code \"%s\" and departure date %s found.";
    
    /**
     * Exception message indicating that the aircraft type does not exist.
     */
    private static final String INVALID_AIRCRAFT_TYPE = 
            "Aircraft type \"%s\" not exists.";
    
    /**
     * Exception message indicating that a certain airline does not exist.
     */
    private static final String AIRLINE_NOT_FOUND = 
            "No airline with ID %d found.";
    
    /**
     * Exception indicating that the aircraft type does not exist.
     * 
     * @param aircraftType non-existent aircraft type.
     * @return see description.
     */
    public static final RicstonFlightException invalidAircraftType(final String aircraftType) {
        return new RicstonValidationException(RicstonFlightError.AIRCRAFT_TYPE_NOT_FOUND,
                String.format(RicstonFlightExceptions.INVALID_AIRCRAFT_TYPE, aircraftType));
    }
    
    /**
     * Exception indicating that the requested route for this flight does not exist.
     * 
     * @param routeKey key parameters that identify a flight route.
     * @return see description.
     */
    public static final RicstonFlightException invalidFlightRoute(final RouteKey routeKey) {
        return new RicstonIllegalStateException(RicstonFlightError.ROUTE_NOT_FOUND, String.format(
                RicstonFlightExceptions.INVALID_FLIGHT_ROUTE, routeKey.getAirlineCode(),
                routeKey.getSourceAirportCode(), routeKey.getDestinationAirportCode()));
    }
    
    /**
     * Exception indicating that a certain flight was not found.
     * 
     * @param flightKey key parameters that identify a flight.
     * @return see description.
     */
    public static final RicstonFlightException flightNotFound(final FlightKey flightKey) {
        return new RicstonNotFoundException(RicstonFlightError.FLIGHT_NOT_FOUND,
                String.format(RicstonFlightExceptions.FLIGHT_NOT_FOUND,
                flightKey.getFlightCode(), flightKey.getDepartureDate()));
    }
    
    /**
     * Exception indicating that at least one passenger is needed to issue a ticket.
     * 
     * @param flightCode flight code.
     * @return see description.
     */
    public static final RicstonFlightException atLeastOnePassenger(final String flightCode) {
        return new RicstonValidationException(RicstonFlightError.NO_PASSENGERS, String.
                format(RicstonFlightExceptions.AT_LEAST_ONE_PASSENGER, flightCode));
    }
    
    /**
     * Exception indicating that a certain airline does not exist.
     * 
     * @param airlineID airline unique indentifier.
     * @return see description.
     */
    public static final RicstonFlightException airlineNotFound(final int airlineID) {
        return new RicstonNotFoundException(RicstonFlightError.AIRLINE_NOT_FOUND, String.
                format(RicstonFlightExceptions.AIRLINE_NOT_FOUND, airlineID));
    }
    
    /**
     * Exception indicating excessive reduction in flight price.
     * 
     * @param flightCode flight code.
     * @param currentPrice current flight price.
     * @param reduction requested price reduction.
     * @return see description.
     */
    public static final RicstonFlightException invalidFlightPriceReduction(final
            String flightCode, final Number currentPrice, final Number reduction) {
        return new RicstonIllegalStateException(RicstonFlightError.NEGATIVE_FLIGHT_PRICE,
                String.format(RicstonFlightExceptions.INVALID_FLIGHT_PRICE_REDUCTION,
                flightCode, currentPrice, reduction));
    }

    /**
     * Exception indicating shortage of seats on a given flight.
     * 
     * @param flightCode flight code.
     * @param seatRequired number of seats required
     * @param seatAvailability number of seats available
     * @return see description.
     */
    public static final RicstonFlightException outOfSeats(final String
            flightCode, final short seatRequired, final short seatAvailability) {
        return new RicstonIllegalStateException(RicstonFlightError.OUT_OF_SEATS,
                String.format(RicstonFlightExceptions.OUT_OF_SEATS,
                flightCode, seatAvailability, seatRequired));
    }

    /**
     * Exception indicating that the page number must be greater than one.
     * 
     * @return see description.
     */
    public static final RicstonFlightException notPositivePageIndex() {
        return new RicstonValidationException(RicstonFlightError.
                INVALID_PAGE_INDEX, RicstonFlightExceptions.
                NO_POSITIVE_PAGE_INDEX);
    }
}
