/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.repositories;

import com.ricston.flights.model.entities.Route;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Route JPA Repository.
 * 
 * @author floverde
 * @version 1.0
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Route.PK>
{
    /**
     * Retrieve a route followed by a certain airline,
     * which goes from a certain airport to another.
     * 
     * @param airlineIataCode IATA code of the airline.
     * @param departureAirportIataCode IATA code of the departure airport.
     * @param destinationAirportIataCode IATA code of the destination airport.
     * @return single route that meets the search criteria.
     */
    @Query("SELECT r FROM Route r WHERE r.airlineCode = :airlineIata AND "
            + "r.sourceAirport = :deptIata AND r.destinationAirport = :destIata")
    public Optional<Route> findByRouteKey(@Param("airlineIata") final String
            airlineIataCode, @Param("deptIata") final String departureAirportIataCode,
            @Param("destIata") final String destinationAirportIataCode);
}
