/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.services.impl;

import com.ricston.flights.dto.AirportSiteDTO;
import com.ricston.flights.dto.AirportSitePageDTO;
import com.ricston.flights.errors.RicstonFlightError;
import com.ricston.flights.mappers.AirportMapper;
import com.ricston.flights.model.entities.Airport;
import com.ricston.flights.errors.RicstonFlightException;
import com.ricston.flights.model.repositories.AirlineRepository;
import com.ricston.flights.model.repositories.AirportRepository;
import org.junit.platform.commons.util.CollectionUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.mapstruct.factory.Mappers;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.Arrays;
import org.mockito.Spy;

/**
 * {@link AirlineService} Tests.
 * 
 * @author floverde
 * @version 1.0
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class AirlineServiceTest
{
    @Spy
    private final AirportMapper airportMapper;
    
    @Mock
    private AirportRepository airportRepository;
    
    @Mock
    private AirlineRepository airlineRepository;
            
    @InjectMocks
    private AirlineServiceImpl airlineService;
    
    /**
     * Defines the ID of the test airline.
     */
    private static final int SAMPLE_AIRLINE_ID = 4296;
    
    /**
     * Initialize the real dependencies used in these tests.
     */
    public AirlineServiceTest() {
        // Gets the instance of the airport mapper
        this.airportMapper = Mappers.getMapper(AirportMapper.class);
    }
    
    /**
     * Test of getDestinations method of class AirlineService,
     * by checking that given an airline, all destinations
     * reached by it are returned.
     */
    @Test
    public void testGetDestinations() {
        final Page<Airport> airportPage;
        final Airport airport1, airport2;
        final AirportSiteDTO airportSite;
        final AirportSitePageDTO airportSitePage;
        
        log.info("RUNNING TEST: GetDestinations (case: simple)");
        
        // Create a first sample airport
        airport1 = new Airport();
        airport1.setCity("Lisbon");
        airport1.setCountry("Portugal");
        airport1.setName("Lisbon Portela Airport");
        airport1.setType("Civil Airport");
        
        // Create a second example airport
        // (same location and same name)
        airport2 = new Airport();
        airport2.setCity(airport1.getCity());
        airport2.setCountry(airport1.getCountry());
        airport2.setName(airport1.getName());
        airport2.setType("Military Airport");
        
        // Wrap such objects within a JPA results page
        airportPage = new PageImpl<>(Arrays.asList(airport1, airport2));
        // Simulates the existence of the sample airline
        when(this.airlineRepository.existsById(SAMPLE_AIRLINE_ID)).thenReturn(Boolean.TRUE);
        // Simulates the existence of the sample airline
        when(this.airportRepository.findDestinationAirportsByAirlineId(eq(
                SAMPLE_AIRLINE_ID), any())).thenReturn(airportPage);
        
        // EXECUTE the test method: "AirlineService.getDestinations"
        airportSitePage = this.airlineService.getDestinations(SAMPLE_AIRLINE_ID, 1);
        
        // Checks that the page contains a single element
        assertEquals(airportSitePage.getSize(), 1);
        // Retrieves the single destination contained in the output page
        airportSite = CollectionUtils.getOnlyElement(airportSitePage.getItems());
        // Check that the country is the expected one
        assertEquals(airport1.getCountry(), airportSite.getCountry());
        // Check that the airport name is the expected one
        assertEquals(airport1.getName(), airportSite.getAirport());
        // Check that the city is the expected one
        assertEquals(airport1.getCity(), airportSite.getCity());
    }
    
    @Test
    public void testGetDestinations_notExistingAirline() {
        final RicstonFlightException ex;
        log.info("RUNNING TEST: GetDestinations (case: not-existing airline)");
        
        // Simulates the existence of the sample airline
        when(this.airlineRepository.existsById(SAMPLE_AIRLINE_ID)).thenReturn(Boolean.FALSE);
        
        // EXECUTE the test method: "AirlineService.getDestinations"
        ex = assertThrows(RicstonFlightException.class, () -> this.
                airlineService.getDestinations(SAMPLE_AIRLINE_ID, 0));
        
        // Check that the error message refers to the test case
        assertEquals(RicstonFlightError.AIRLINE_NOT_FOUND, ex.getCode());
    }
    
}
