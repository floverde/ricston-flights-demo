/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.repositories;

import com.ricston.flights.model.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Flight JPA Repository.
 * 
 * @author floverde
 * @version 1.0
 */
@Repository
public interface FlightRepository extends JpaRepository<Flight, Flight.PK>
{
    /**
     * Gets all flights that depart within a certain time range.
     * 
     * @param from start of the time search interval.
     * @param to end of the time search interval.
     * @return see description.
     */
    public Collection<Flight> findAllByPkDepartureDateBetween(final
            LocalDateTime from, final LocalDateTime to);
}
