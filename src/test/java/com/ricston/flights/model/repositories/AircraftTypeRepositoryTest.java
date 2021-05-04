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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.jdbc.Sql;

/**
 * {@link AircraftTypeRepository} Tests.
 * 
 * @author floverde
 * @version 1.0
 */
@Slf4j
@DataJpaTest
@Sql("/inserts.sql")
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = AircraftTypeRepository.class)
class AircraftTypeRepositoryTest
{   
    @Autowired
    private AircraftTypeRepository repository;
    
    /**
     * Test of existsByModel method of class AircraftTypeRepository,
     * by checking that a certain model of aircraft exists.
     */
    @Test
    public void testExistsByModel() {
        log.info("RUNNING TEST: ExistsByModel (case: simple)");
        assertTrue(this.repository.existsByModel("Airbus A300"));
    }
    
    /**
     * Test of existsByModel method of class AircraftTypeRepository,
     * by checking that a certain model of aircraft does not exist.
     */
    @Test
    public void testExistsByModel_notExistingModel() {
        log.info("RUNNING TEST: ExistsByModel (case: Not-existing model)");
        assertFalse(this.repository.existsByModel("Fake model"));
    }
}
