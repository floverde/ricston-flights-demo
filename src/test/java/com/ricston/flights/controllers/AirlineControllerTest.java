/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.controllers;

import com.ricston.flights.dto.AirportSiteDTO;
import com.ricston.flights.dto.AirportSitePageDTO;
import com.ricston.flights.errors.RicstonFlightError;
import com.ricston.flights.errors.RicstonFlightExceptions;
import com.ricston.flights.services.AirlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import java.util.Collections;

/**
 * {@link AirlineController} tests.
 * 
 * @author floverde
 * @version 1.0
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(AirlineController.class)
public class AirlineControllerTest
{
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private AirlineService service;
    
    /**
     * Test of getDestinations method of class AirlineController.
     * <p>Scenario: Search successfully</p>
     * <p>Input-URL-Param[id]: ID of an existing airline.</p>
     * <p>Output-payload: destinations reached by a given airline.</p>
     * <p>Output-status: HTTP 200 OK.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testGetDestinations_success() throws Exception {
        final AirportSiteDTO site;
        final AirportSitePageDTO page;
        
        log.info("RUNNING REST TEST: GetDestinations (case: success)");
        
        site = new AirportSiteDTO();
        site.setAirport("Lisbon Portela Airport");
        site.setCountry("Portugal");
        site.setCity("Lisbon");
        
        page = new AirportSitePageDTO(Collections.singleton(site));
        when(this.service.getDestinations(eq(1), anyInt())).thenReturn(page);
        
        this.mvc.perform(get("/airlines/1/destinations").accept(MediaType.
                APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).
                andExpect(jsonPath("$.items", hasSize(1))).
                andExpect(jsonPath("$.size", is(1))).
                andExpect(jsonPath("$.page", is(1))).
                andExpect(jsonPath("$.items[0].airport", is(site.getAirport()))).
                andExpect(jsonPath("$.items[0].country", is(site.getCountry()))).
                andExpect(jsonPath("$.items[0].city", is(site.getCity())));
    }
    
    /**
     * Test of getDestinations method of class AirlineController.
     * <p>Scenario: Airline Not Found.</p>
     * <p>Input-URL-Param[id]: ID of non-existent airline.</p>
     * <p>Output-payload: error details.</p>
     * <p>Output-status: HTTP 404 OK.</p>
     * 
     * @throws Exception in case of an error during the test.
     */
    @Test
    public void testGetDestinations_airlineNotFound() throws Exception {
        log.info("RUNNING REST TEST: GetDestinations (case: airline not found)");
        
        when(this.service.getDestinations(anyInt(), anyInt())).thenThrow(
                RicstonFlightExceptions.airlineNotFound(999999));
        
        this.mvc.perform(get("/airlines/999999/destinations").accept(MediaType.
                APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound()).
                andExpect(jsonPath("$.code", is(RicstonFlightError.
                AIRLINE_NOT_FOUND.getCode())));
    }
}