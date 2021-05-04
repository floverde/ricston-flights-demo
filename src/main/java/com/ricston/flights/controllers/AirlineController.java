/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.controllers;

import com.ricston.flights.dto.AirportSitePageDTO;
import com.ricston.flights.services.AirlineService;
import javax.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;


/**
 * Airline REST Controller.
 * 
 * @author floverde
 * @version 1.0
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("airlines")
public class AirlineController
{
    /**
     * Airline service reference.
     */
    private final AirlineService service;
    
    /**
     * Gets all destinations reached by a given airline.
     * Destinations are provided as tuples: country, city, airport name.
     * <ul>
     *  <li>HTTP 200 - destinations reached by a given airline.</li>
     *  <li>HTTP 400 - invalid HTTP requrst.</li>
     *  <li>HTTP 404 - airline not found.</li>
     * </ul>
     * 
     * @param id airline unique indentifier.
     * @param page results page index (starting from {@code 1}).
     * @return destinations reached by a given airline.
     */
    @GetMapping("{id}/destinations")
    public ResponseEntity<AirportSitePageDTO> getDestinations(@PathVariable final int id,
            @Positive @RequestParam(required = false, defaultValue = "1") final Integer page) {
        final int pageIndex = ObjectUtils.defaultIfNull(page, NumberUtils.INTEGER_ONE);
        log.info("Calling \"/airlines/{}/destinations?page={}\"", id, pageIndex);
        return ResponseEntity.ok(this.service.getDestinations(id, pageIndex));
    } 
}
