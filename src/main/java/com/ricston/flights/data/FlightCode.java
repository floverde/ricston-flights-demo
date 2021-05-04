/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.data;

import com.ricston.flights.errors.RicstonFlightError;
import com.ricston.flights.errors.RicstonValidationException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import lombok.Value;

/**
 * Data structure defining a flight code.
 * <p>Node: Immutable data structure.</p>
 * 
 * @see https://en.wikipedia.org/wiki/Flight_number
 * @author floverde
 * @version 1.0
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightCode implements Serializable
{
    /**
     * Flight number.
     */
    private final short number;
    
    /**
     * Airline IATA code.
     * @see https://en.wikipedia.org/wiki/Airline_codes#IATA_airline_designator
     */
    private final String airline;
    
    /**
     * Pattern the regular expression of a flight code.
     */
    public static final String PATTERN = "([A-Z]+)(\\d+)";
    
    /**
     * Defines the regular expression of a flight code.
     */
    private static final Pattern FLIGHT_CODE_REGEX = Pattern.compile(
            FlightCode.PATTERN, Pattern.CASE_INSENSITIVE);
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 6498285520483628402L;
    
    /**
     * Exception message indicating that a flight code cannot be parsed.
     */
    private static final String UNPARSABLE_FLIGHT_CODE = 
            "Unable to parse flight code \"%s\".";
    
    /**
     * Exception message indicating a malformed IATA airline code.
     */
    private static final String MALFORMED_AIRLINE_CODE = 
            "Malformed IATA airline code: \"%s\".";
    
    /**
     * Exception message indicating that the flight number cannot be negative. 
     */
    private static final String NEGATIVE_FLIGHT_NUMBER = 
            "The flight number cannot be negative.";
    
    /**
     * Create a flight code by specifying
     * the airline IATA code and the flight number.
     * 
     * @param airline airline IATA code.
     * @param number flight number.
     * @throws RicstonValidationException malformed {@code airline}
     *         code or negative flight {@code number}.
     * @return {@link FlightCode} object.
     */
    public static final FlightCode of(final String airline, final int number) {
        // Check that the airline IATA code is not blank or contains numbers
        if (StringUtils.isNotBlank(airline) && StringUtils.isAlpha(airline)) {
            // Check that the flight number is positive
            if (number >= 0) {
                // Returns a valid flight code object
                return new FlightCode((short) number, airline);
            } else {
                // Raises an exception indicating that the flight number cannot be negative
                throw new RicstonValidationException(RicstonFlightError.
                    INVALID_FLIGHT_CODE, FlightCode.NEGATIVE_FLIGHT_NUMBER);
            }
        } else {
            // Raises an exception indicating that the IATA airline code is malformed
            throw new RicstonValidationException(RicstonFlightError.
                    INVALID_FLIGHT_CODE, String.format(FlightCode.
                    MALFORMED_AIRLINE_CODE, airline));
        }
    }
    
    /**
     * Parses a flight code.
     * 
     * @param string string to be parsed.
     * @throws RicstonValidationException unparsable flight code.
     * @return {@link FlightCode} object.
     */
    public static final FlightCode parse(final String string) {
        // Creates a matcher to parse the given string
        final Matcher m = FlightCode.FLIGHT_CODE_REGEX.matcher(string);
        // Checks whether the string satisfies the regular expression
        if (m.matches()) {
            // Returns a valid flight code object
            return new FlightCode((short) Integer.parseUnsignedInt(
                    m.group(2)), m.group(1).toUpperCase());
        } else {
            // Raises an exception indicating that the string cannot be parsed
            throw new RicstonValidationException(RicstonFlightError.
                    INVALID_FLIGHT_CODE, String.format(FlightCode.
                    UNPARSABLE_FLIGHT_CODE, string));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        // Returns a textual representation of the flight code
        return String.format("%s%d", this.airline, this.number);
    }
}
