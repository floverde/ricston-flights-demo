/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.services;

import com.ricston.flights.dto.AirportSitePageDTO;

/**
 * Airline Service.
 * 
 * @author floverde
 * @version 1.0
 */
public interface AirlineService
{
    /**
     * Gets the paged list of all destinations reached by a certain airline.
     * 
     * @param airlineID airline unique indentifier.
     * @param pageIndex index of the requested page.
     * @return see description.
     */
    public AirportSitePageDTO getDestinations(final int airlineID, final int pageIndex);
}
