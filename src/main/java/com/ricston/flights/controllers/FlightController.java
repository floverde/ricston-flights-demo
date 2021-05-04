/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.controllers;

import com.ricston.flights.dto.ChangePriceDTO;
import com.ricston.flights.dto.PurchaseTicketDTO;
import com.ricston.flights.dto.CreateFlightDTO;
import com.ricston.flights.dto.FlightDTO;
import com.ricston.flights.services.data.FlightKey;
import com.ricston.flights.dto.SearchFlightDTO;
import com.ricston.flights.dto.TicketDTO;
import com.ricston.flights.services.FlightService;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Flight REST Controller.
 * 
 * @author floverde
 * @version 1.0
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("flights")
public class FlightController
{
    /**
     * Flight service reference.
     */
    private final FlightService service;
    
    /**
     * Gets the flights that meet the search criteria.
     * <ul>
     *  <li>HTTP 200 - flights that meet the search criteria.</li>
     *  <li>HTTP 400 - invalid search criteria.</li>
     * </ul>
     * 
     * @param params DTO encapsulating flight search criteria.
     * @return see description.
     */
    @GetMapping
    public ResponseEntity<Collection<FlightDTO>> search(@Valid final SearchFlightDTO params) {
        log.info("Search flights (params: {})", params);
        return ResponseEntity.ok(this.service.search(params));
    }
    
    /**
     * Create a new flight.
     * <ul>
     *  <li>HTTP 201 - new flight successfully created.</li>
     *  <li>HTTP 400 - invalid creation parameters.</li>
     *  <li>HTTP 409 - no route matching the criteria provided.</li>
     * </ul>
     * 
     * @param payload creation parameters.
     * @return see description.
     */
    @PostMapping
    public ResponseEntity<FlightDTO> create(@Valid @RequestBody final CreateFlightDTO payload) {
        log.info("Create a new flight (payload: {})", payload);
        return new ResponseEntity(this.service.create(
                payload), HttpStatus.CREATED);
    }
    
    /**
     * Purchase a new ticket for certain flight.
     * <ul>
     *  <li>HTTP 201 - new ticket successfully purchased.</li>
     *  <li>HTTP 400 - invalid purchase parameters.</li>
     *  <li>HTTP 404 - flight not found.</li>
     *  <li>HTTP 409 - not enough places available.</li>
     * </ul>
     * 
     * @param flightCode flight identification code.
     * @param departureDate flight departure date.
     * @param payload ticket purchase parameters.
     * @return see description.
     */
    @PostMapping("{code}/ticket")
    public ResponseEntity<TicketDTO> purchaseTicket(@PathVariable("code") final String flightCode,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @RequestParam(name = "departure-date", required = true)
            final LocalDateTime departureDate, @Valid @RequestBody final PurchaseTicketDTO payload) {
        final FlightKey key = new FlightKey(flightCode, departureDate);
        log.info("Purchase a ticket for flight (key: {})", key);
        return new ResponseEntity(this.service.purchaseTicket(
                key, payload), HttpStatus.CREATED);
    }
    
    /**
     * Increases or decreases the price of a certain flight.
     * <ul>
     *  <li>HTTP 200 - price successfully updated.</li>
     *  <li>HTTP 400 - invalid update parameters.</li>
     *  <li>HTTP 404 - flight not found.</li>
     * </ul>
     * 
     * @param flightCode flight identification code.
     * @param departureDate flight departure date.
     * @param payload delta of price increase/decrease.
     * @return see description.
     */
    @PatchMapping("{code}/price")
    public ResponseEntity<FlightDTO> changePrice(@PathVariable("code") final String flightCode,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @RequestParam(name = "departure-date", required = true)
            final LocalDateTime departureDate, @Valid @RequestBody final ChangePriceDTO payload) {
        final FlightKey key = new FlightKey(flightCode, departureDate);
        log.info("Change flight (key: {}) price by {}", key, payload.getDelta());
        return ResponseEntity.ok(this.service.changePrice(key, payload.getDelta()));
    }
}
