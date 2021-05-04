/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.services.impl;

import com.ricston.flights.model.entities.Route;
import com.ricston.flights.model.repositories.RouteRepository;
import com.ricston.flights.services.data.RouteKey;
import com.ricston.flights.services.RouteService;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import java.util.Optional;

/**
 * Implementation of {@link RouteService}.
 * 
 * @author floverde
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class RouteServiceImpl implements RouteService
{
    /**
     * Route repository reference.
     */
    private final RouteRepository repository;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Route> getByKey(final RouteKey key) {
        // Queries the repository to find the route identified by
        // the IATA codes of the airline and the airports it connects
        return this.repository.findByRouteKey(key.getAirlineCode(), key.
                getSourceAirportCode(), key.getDestinationAirportCode());
    }
}
