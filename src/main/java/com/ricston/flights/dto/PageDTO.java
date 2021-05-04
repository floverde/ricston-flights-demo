/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import lombok.Data;

/**
 * Defines a generic results page.
 * 
 * @param <T> class of paginated results.
 * @author floverde
 * @version 1.0
 */
@Data
public class PageDTO<T> implements Serializable
{
    /**
     * Collection of paginated results.
     */
    private final Collection<T> items;
    
    /**
     * Total number of pages expected.
     */
    private final int totalPages;
    
    /**
     * Index of the current page.
     */
    private final int page;
    
    /**
     * Number of items on this page.
     */
    private final int size;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 4275735771945890420L;
    
    /**
     * Create a page that contains all the results.
     * 
     * @param items page results.
     */
    public PageDTO(final Collection<T> items) {
        // Invokes the overload constructor
        this(items, 1, 1);
    }
    
    /**
     * Create a results page by specifying the
     * page index and the number of total pages.
     * 
     * @param items page results.
     * @param page page index.
     * @param totalPages total pages.
     */
    public PageDTO(final Collection<T> items, final int page, final int totalPages) {
        // Stores the result collection checking that it is not null
        this.items = Objects.requireNonNull(items);
        // Stores the total number of pages
        this.totalPages = totalPages;
        // Stores the number of elements on the page
        this.size = items.size();
        // Stores the page index
        this.page = page;
    }
}
