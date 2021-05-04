/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.dto;

import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * Ticket DTO.
 * 
 * @author floverde
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO implements Serializable
{
    /**
     * Ticket ID code.
     */
    private String code;
    
    /**
     * Set of passenger names on an airline ticket.
     */
    private Set<String> passengers;
    
    /**
     * Flight to which this ticket refers.
     */
    private FlightDTO flight;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 7894145777918201729L;
}
