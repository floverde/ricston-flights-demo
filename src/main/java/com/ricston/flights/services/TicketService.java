/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.services;

import com.ricston.flights.dto.FlightDTO;
import com.ricston.flights.dto.TicketDTO;
import java.util.Set;

/**
 * Ticket Service.
 * 
 * @author floverde
 * @version 1.0
 */
public interface TicketService
{
    /**
     * Emits a ticket for a certain flight and a certain set of passengers.
     * 
     * @param flight flight for which to issue the ticket.
     * @param passengers set of passengers on this ticket.
     * @return ticket issued for this flight and these passengers.
     */
    public TicketDTO generate(final FlightDTO flight, final Set<String> passengers);
}
