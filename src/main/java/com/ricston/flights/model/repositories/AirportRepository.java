/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.repositories;

import com.ricston.flights.model.entities.Airline;
import com.ricston.flights.model.entities.Airport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

/**
 * Airport JPA Repository.
 * 
 * @author floverde
 * @version 1.0
 */
@Repository
public interface AirportRepository extends JpaRepository<Airport, Integer>
{
    /**
     * Paged search of the destination airports reached by a certain airline.
     * 
     * @param airlineID ID of the airline for which airports are requested.
     * @param pageable pagination parameters.
     * @return see description.
     */
    @Query("SELECT DISTINCT r.destination FROM Route r INNER JOIN r.destination WHERE"
            + " r.pk.airlineID = :airlineID ORDER BY r.destination.country, r.destination.city")
    public Page<Airport> findDestinationAirportsByAirlineId(@Param("airlineID") final int airlineID, final Pageable pageable);
    
    /**
     * Paged search of the destination airports reached by a certain airline.
     * 
     * @param airline airline for which airports are requested.
     * @param pageable pagination parameters.
     * @return see description.
     */
    @Query("SELECT DISTINCT r.destination FROM Route r INNER JOIN r.destination WHERE"
            + " r.airline = :airline ORDER BY r.destination.country, r.destination.city")
    public Page<Airport> findDestinationAirportsByAirline(@Param("airline") final Airline airline, final Pageable pageable);
}
