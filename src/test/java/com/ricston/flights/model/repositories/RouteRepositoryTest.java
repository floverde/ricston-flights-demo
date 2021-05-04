/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.repositories;

import com.ricston.flights.model.converters.CharacterConverter;
import com.ricston.flights.model.entities.Route;
import java.util.Optional;
import javax.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.jdbc.Sql;

/**
 * {@link RouteRepository} Tests.
 * 
 * @author floverde
 * @version 1.0
 */
@Slf4j
@DataJpaTest
@Sql("/inserts.sql")
@ExtendWith(SpringExtension.class)
class RouteRepositoryTest
{   
    @Autowired
    private RouteRepository repository;
    
    /**
     * Test of findByRouteKey method of class RouteRepository,
     * by checking that a route exists that has certain key properties.
     */
    @Test
    public void testFindByRouteKey() {
        final Route route;
        final Optional<Route> optRoute;
        log.info("RUNNING TEST: FindByRouteKey (case: simple)");
        optRoute = this.repository.findByRouteKey("FR", "LIS", "BRU");
        assertTrue(optRoute.isPresent());
        route = optRoute.get();
        assertEquals(route.getDestinationAirport(), "BRU");
        assertEquals(route.getSourceAirport(), "LIS");
        assertEquals(route.getAirlineCode(), "FR");
    }
    
    /**
     * Test of findByRouteByKey method of class RouteRepository,
     * by checking that there is NO route with certain key properties.
     */
    @Test
    public void testFindByRouteKey_notExistingRoute() {
        log.info("RUNNING TEST: FindByRouteKey (case: Not-existing route)");
        assertFalse(this.repository.findByRouteKey("FR", "LIS", "???").isPresent());
    }
}
