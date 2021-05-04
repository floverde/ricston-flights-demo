/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.errors;

import java.util.Objects;
import lombok.Getter;

/**
 * Defines the generic proprietary exception for this application.
 * 
 * @author floverde
 * @version 1.0
 */
@Getter
public class RicstonFlightException extends RuntimeException
{
    /**
     * Error code.
     */
    private final RicstonFlightError code;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -5564104934407919891L;
    
    /**
     * Creates a new {@code RicstonFlightException}
     * instance indicating an unknown error.
     */
    public RicstonFlightException() {
        // Invokes the overload constructor
        // by indicating an unknown error
        this(RicstonFlightError.UNKNOWN);
    }
    
    /**
     * Creates a new {@code RicstonFlightException}
     * instance with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RicstonFlightException(final String msg) {
        // Invokes the overload constructor
        // by indicating an unknown error
        this(RicstonFlightError.UNKNOWN, msg);
    }
    
    /**
     * Creates a new {@code RicstonFlightException}
     * instance by specifying its error code.
     * 
     * @param code the error code.
     */
    public RicstonFlightException(final RicstonFlightError code) {
        // Invokes the superclass constructor
        super(code.toString());
        // Stores the error code provided
        this.code = code;
    }

    /**
     * Create a new {@code RicstonFlightException} instance
     * by specifying its error code and a detailed message.
     * 
     * @param code the error code.
     * @param msg the detail message.
     */
    public RicstonFlightException(final RicstonFlightError code, final String msg) {
        // Invokes the superclass constructor
        super(msg);
        // Stores the error code provided
        this.code = Objects.requireNonNull(code);
    }
}
