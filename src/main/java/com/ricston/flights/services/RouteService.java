/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.services;

import com.ricston.flights.model.entities.Route;
import com.ricston.flights.services.data.RouteKey;
import java.util.Optional;

/**
 * Route Service.
 * 
 * @author floverde
 * @version 1.0
 */
public interface RouteService
{
    /**
     * Retrieves the individual route identified by the key
     * using the IATA codes of the airline and airports it connects.
     * 
     * @param key key parameters of the flight route.
     * @return see description.
     */
    public Optional<Route> getByKey(final RouteKey key);
}
