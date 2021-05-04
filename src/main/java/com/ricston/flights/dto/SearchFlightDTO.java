/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.dto;

import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

/**
 * Search Flight DTO.
 * 
 * @author floverde
 * @version 1.0
 */
@Data
public class SearchFlightDTO implements Serializable
{
    /**
     * Date for which to filter available flights.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -295020880502926564L;
}
