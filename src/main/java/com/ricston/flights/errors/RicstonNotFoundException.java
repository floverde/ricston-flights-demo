/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.errors;

/**
 * Defines the proprietary exception for not found errors.
 * 
 * @author floverde
 * @version 1.0
 */
public class RicstonNotFoundException extends RicstonFlightException
{
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 5789304548744256793L;
    
    /**
     * Creates a new {@code RicstonNotFoundException}
     * instance indicating an unknown error.
     */
    public RicstonNotFoundException() {
        // Invokes the superclass constructor
        super();
    }

    /**
     * Creates a new {@code RicstonNotFoundException}
     * instance with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RicstonNotFoundException(final String msg) {
        // Invokes the superclass constructor
        super(msg);
    }
    
    /**
     * Creates a new {@code RicstonNotFoundException}
     * instance by specifying its error code.
     * 
     * @param code the error code.
     */
    public RicstonNotFoundException(final RicstonFlightError code) {
        // Invokes the superclass constructor
        super(code);
    }

    /**
     * Create a new {@code RicstonNotFoundException} instance
     * by specifying its error code and a detailed message.
     * 
     * @param code the error code.
     * @param msg the detail message.
     */
    public RicstonNotFoundException(final RicstonFlightError code, final String msg) {
        // Invokes the superclass constructor
        super(code, msg);
    }
}
