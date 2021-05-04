/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.errors;

/**
 * Defines the proprietary exception for validation errors.
 * 
 * @author floverde
 * @version 1.0
 */
public class RicstonValidationException extends RicstonFlightException
{   
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 5789304548744256793L;
    
    /**
     * Creates a new {@code RicstonValidationException}
     * instance indicating an unknown error.
     */
    public RicstonValidationException() {
        // Invokes the superclass constructor
        super();
    }

    /**
     * Creates a new {@code RicstonValidationException}
     * instance with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RicstonValidationException(final String msg) {
        // Invokes the superclass constructor
        super(msg);
    }
    
    /**
     * Creates a new {@code RicstonValidationException}
     * instance by specifying its error code.
     * 
     * @param code the error code.
     */
    public RicstonValidationException(final RicstonFlightError code) {
        // Invokes the superclass constructor
        super(code);
    }

    /**
     * Create a new {@code RicstonValidationException} instance
     * by specifying its error code and a detailed message.
     * 
     * @param code the error code.
     * @param msg the detail message.
     */
    public RicstonValidationException(final RicstonFlightError code, final String msg) {
        // Invokes the superclass constructor
        super(code, msg);
    }
}
