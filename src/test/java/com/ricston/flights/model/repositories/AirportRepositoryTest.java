/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.ricston.flights.model.entities.Airport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

/**
 * {@link AirportRepository} Tests.
 * 
 * @author floverde
 * @version 1.0
 */
@Slf4j
@DataJpaTest
@Sql("/inserts.sql")
@ExtendWith(SpringExtension.class)
class AirportRepositoryTest
{   
    @Autowired
    private AirportRepository repository;
    
    /**
     * Defines the ID of the test airline.
     */
    private static final int SAMPLE_AIRLINE_ID = 4296;
    
    /**
     * Test of findDestinationAirportsByAirlineId method of class
     * AirportRepository, by checking that it only returns airports
     * that at least one incoming route served by a certain airline.
     */
    @Test
    public void testFindDestinationAirportsByAirlineId() {
        final Page<Airport> airportPage;
        log.info("RUNNING TEST: FindDestinationAirportsByAirlineId");
        
        airportPage = this.repository.findDestinationAirportsByAirlineId(
                SAMPLE_AIRLINE_ID, Pageable.unpaged());
        
        // Check that all airports have at least one incoming route served by the airline
        assertTrue(airportPage.stream().allMatch(a -> a.getIncomingRoutes().stream().
                anyMatch(r -> r.getPk().getAirlineID() == SAMPLE_AIRLINE_ID)));
    }
}
