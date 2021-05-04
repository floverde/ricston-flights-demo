/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;

/**
 * Purchase Ticket DTO.
 * 
 * @author floverdd
 * @version 1.0
 */
@Data
public class PurchaseTicketDTO implements Serializable
{
    /**
     * Set of passenger names on an airline ticket.
     */
    @Size(min = 1, max = Short.MAX_VALUE)
    private Set<String> passengers;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 6222632364063562409L;
}
