/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * Defines the location and name of an airport.
 * 
 * @author floverde
 * @version 1.0
 */
@Data
public class AirportSiteDTO implements Serializable
{
    /**
     * Country where the airport is located.
     */
    private String country;
    
    /**
     * City where the airport is located.
     */
    private String city;
    
    /**
     * Airport name.
     */
    private String airport;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -8952026514716874310L;
}
