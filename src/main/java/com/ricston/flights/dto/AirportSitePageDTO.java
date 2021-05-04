/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.dto;

import java.util.Collection;

/**
 * Implementation of {@link PageDTO} that
 * holds {@link AirportSiteDTO} objects.
 * 
 * @author floverde
 * @version 1.0
 */
public class AirportSitePageDTO extends PageDTO<AirportSiteDTO>
{
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 8859985662889012180L;
    
    /**
     * Create a page that contains all the results.
     * 
     * @param sites airport sites.
     */
    public AirportSitePageDTO(final Collection<AirportSiteDTO> sites) {
        // Invokes the superclass constructor
        super(sites, 1, 1);
    }
    
    /**
     * Create a results page by specifying the
     * page index and the number of total pages.
     * 
     * @param sites airport sites.
     * @param page page index.
     * @param totalPages total pages.
     */
    public AirportSitePageDTO(final Collection<AirportSiteDTO>
            sites, final int page, final int totalPages) {
        // Invokes the superclass constructor
        super(sites, page, totalPages);
    }
}
