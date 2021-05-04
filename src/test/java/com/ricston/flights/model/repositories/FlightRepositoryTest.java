/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import com.ricston.flights.model.entities.Flight;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

/**
 * {@link FlightRepository} Tests.
 * 
 * @author floverde
 * @version 1.0
 */
@Slf4j
@DataJpaTest
@Sql("/inserts.sql")
@ExtendWith(SpringExtension.class)
class FlightRepositoryTest
{   
    @Autowired
    private FlightRepository repository;
    
    /**
     * Test of findAllByPkDepartureDateBetween method of class FlightRepository,
     * by checking that all returned flights depart on a certain date.
     */
    @Test
    public void testFindAllByPkDepartureDateBetween() {
        final LocalDate departureDate;
        final LocalDateTime from, to;
        final Collection<Flight> flights;
        log.info("RUNNING TEST: FindAllByPkDepartureDateBetween");
        
        departureDate = LocalDate.of(2017, 4, 12);
        to = LocalDateTime.of(departureDate, LocalTime.MAX);
        from = LocalDateTime.of(departureDate, LocalTime.MIN);
        flights = this.repository.findAllByPkDepartureDateBetween(from, to);
        
        
        // Check that the departure date of each
        // flight is within the specified range
        assertTrue(flights.stream().allMatch(f -> f.getPk().
                getDepartureDate().isAfter(from) && f.getPk().
                getDepartureDate().isBefore(to)));
    }
}
