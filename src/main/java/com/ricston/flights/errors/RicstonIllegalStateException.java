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
public class RicstonIllegalStateException extends RicstonFlightException
{   
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 5789304548744256793L;
    
    /**
     * Creates a new {@code RicstonIllegalStateException}
     * instance indicating an unknown error.
     */
    public RicstonIllegalStateException() {
        // Invokes the superclass constructor
        super();
    }

    /**
     * Creates a new {@code RicstonIllegalStateException}
     * instance with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RicstonIllegalStateException(final String msg) {
        // Invokes the superclass constructor
        super(msg);
    }
    
    /**
     * Creates a new {@code RicstonIllegalStateException}
     * instance by specifying its error code.
     * 
     * @param code the error code.
     */
    public RicstonIllegalStateException(final RicstonFlightError code) {
        // Invokes the superclass constructor
        super(code);
    }

    /**
     * Create a new {@code RicstonIllegalStateException} instance
     * by specifying its error code and a detailed message.
     * 
     * @param code the error code.
     * @param msg the detail message.
     */
    public RicstonIllegalStateException(final RicstonFlightError code, final String msg) {
        // Invokes the superclass constructor
        super(code, msg);
    }
}
