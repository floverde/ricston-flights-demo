/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.services;

import com.ricston.flights.dto.CreateFlightDTO;
import com.ricston.flights.dto.FlightDTO;
import com.ricston.flights.services.data.FlightKey;
import com.ricston.flights.dto.PurchaseTicketDTO;
import com.ricston.flights.dto.SearchFlightDTO;
import com.ricston.flights.dto.TicketDTO;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * Flight Service.
 * 
 * @author floverde
 * @version 1.0
 */
public interface FlightService
{
    /**
     * Purchase a new ticket for certain flight.
     * 
     * @param key key parameters that identify the flight.
     * @param params ticket purchase parameters.
     * @return ticket issued for this flight.
     */
    public TicketDTO purchaseTicket(final FlightKey key, final PurchaseTicketDTO params);
    
    /**
     * Alter the price of a flight by increasing or decreasing it.
     * 
     * @param key key parameters that identify the flight.
     * @param delta amount to be added or subtracted to the flight price.
     * @return flight just modified.
     */
    public FlightDTO changePrice(final FlightKey key, final BigDecimal delta);
    
    /**
     * Gets the flights that meet the search criteria.
     * 
     * @param params DTO encapsulating flight search criteria.
     * @return see description.
     */
    public Collection<FlightDTO> search(final SearchFlightDTO params);
    
    /**
     * Create a new flight by providing some information.
     * 
     * @param params flight creation parameters.
     * @return newly created flight.
     */
    public FlightDTO create(final CreateFlightDTO params);
}
