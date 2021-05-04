/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.services.impl;

import com.ricston.flights.dto.AirportSiteDTO;
import com.ricston.flights.dto.AirportSitePageDTO;
import com.ricston.flights.errors.RicstonFlightExceptions;
import com.ricston.flights.mappers.AirportMapper;
import com.ricston.flights.model.repositories.AirlineRepository;
import com.ricston.flights.services.AirlineService;
import com.ricston.flights.model.repositories.AirportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.Objects;

/**
 * Implementation of {@link AirlineService}.
 * 
 * @author floverde
 * @version 1.0
 */
@Service
public class AirlineServiceImpl implements AirlineService
{
    /**
     * Defines the number of items per page.
     */
    @Value("${com.ricston.flights.paging.size: 10}")
    private int pageSize;
    
    /**
     * Indicates if pagination is enabled.
     */
    @Value("${com.ricston.flights.paging.enable: true}")
    private boolean pagingEnabled;
    
    /**
     * Airport mapper reference.
     */
    private final AirportMapper airportMapper;
    
    /**
     * Airline repository reference.
     */
    private final AirlineRepository airlineRepository;
    
    /**
     * Airport repository reference.
     */
    private final AirportRepository airportRepository;
    
    /**
     * Create a new {@code AirlineServiceImpl} instance.
     * 
     * @param airportMapper {@link AirportMapper} reference.
     * @param airportRepository {@link AirlineRepository} reference.
     * @param airlineRepository {@link AirportRepository} reference.
     */
    public AirlineServiceImpl(final AirportMapper airportMapper, final AirportRepository
            airportRepository, final AirlineRepository airlineRepository) {
        // Stores the reference to the airlines repository
        this.airlineRepository = Objects.requireNonNull(airlineRepository);
        // Stores the reference to the airports repository
        this.airportRepository = Objects.requireNonNull(airportRepository);
        // Stores the reference to the airport mapper
        this.airportMapper = Objects.requireNonNull(airportMapper);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AirportSitePageDTO getDestinations(final int airlineID, final int pageIndex) {
        final Page<AirportSiteDTO> results;
        // Check if there is an airline with that ID
        if (this.airlineRepository.existsById(airlineID)) {
            // Returns a page of results of all destination airports for a given airline
            results = this.airportRepository.findDestinationAirportsByAirlineId(airlineID,
                    this.getPageBounds(pageIndex)).map(this.airportMapper::toSiteDTO);
        } else {
            // Raises an exception of indicating that the specified airline does not exist
            throw RicstonFlightExceptions.airlineNotFound(airlineID);
        }
        // Wraps the destinations in an appropriate data structure
        return new AirportSitePageDTO(results.toSet(),
                pageIndex, results.getTotalPages());
    }
    
    /**
     * Gets the required pagination boundaries.
     * <p>Note: the page index starts at {@code 1}, and these
     * boundaries are only returned if pagination is enabled.</p>
     * 
     * @param pageIndex index of the requested page.
     * @return required pagination boundaries
     */
    private Pageable getPageBounds(final int pageIndex) {
        // Check that the page index is greater than zero
        if (pageIndex > 0) {
            // Check if pagination is enabled
            if (this.pagingEnabled) {
                // Indicates that the results should not be paginated
                return PageRequest.of(pageIndex - 1, this.pageSize);
            } else {
                // Indicates that the results must not be paginated
                return Pageable.unpaged();
            }
        } else {
            // Raises an exception indicating that
            // the page index cannot be less than one.
            throw RicstonFlightExceptions.notPositivePageIndex();
        }
    }
}
