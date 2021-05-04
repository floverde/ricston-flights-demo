/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.mappers;

import com.ricston.flights.data.FlightCode;
import com.ricston.flights.dto.CreateFlightDTO;
import com.ricston.flights.dto.FlightDTO;
import com.ricston.flights.model.entities.Flight;
import com.ricston.flights.services.data.RouteKey;
import com.ricston.flights.errors.RicstonValidationException;
import org.mapstruct.Mappings;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

/**
 * Data Mapper used to map entity {@link Flight} to and from the JPA layer.
 * <p><i>Note:</i> it uses <b>MapStruct</b> as the mapping engine.</p>
 * 
 * @author floverde
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public abstract class FlightMapper extends TimeMapper
{
    /**
     * Converts DTO into a JPA entity.
     * 
     * @param dto DTO to be converted.
     * @return related JPA entity.
     */
    @Mappings({
        @Mapping(target = "pk.flightCode", source = "flightCode"),
        @Mapping(target = "pk.departureDate", source = "departureDate",
                qualifiedByName = "truncatedToMinutes"),
    })
    public abstract Flight toEntity(final FlightDTO dto);
    
    /**
     * Converts JPA entity into a DTO.
     * 
     * @param entity JPA entity to be converted.
     * @return related DTO.
     */
    @Mappings({
        @Mapping(target = "flightCode", source = "pk.flightCode"),
        @Mapping(target = "departureDate", source = "pk.departureDate")
    })
    public abstract FlightDTO toDTO(final Flight entity);
    
    /**
     * Gets the JPA entity starting from the creation parameters.
     * 
     * @param params creation parameters.
     * @return derived JPA entity.
     */
    @Mappings({
        @Mapping(target = "pk.flightCode", source = "flightCode"),
        @Mapping(target = "pk.departureDate", source = "departureDate",
                qualifiedByName = "truncatedToMinutes")
    })
    public abstract Flight fromCreationParams(final CreateFlightDTO params);
    
    /**
     * Gets the IATA codes that uniquely identify a
     * {@link Route} using flight creation parameters.
     * 
     * @param params creation parameters.
     * @throws RicstonValidationException unparsable flight code.
     * @return IATA codes that uniquely identify a route.
     */
    public final RouteKey toRouteKey(final CreateFlightDTO params) {
        // Check that the parameters are not null
        if (params != null) {
            // Builds the route key using the parameters provided
            return new RouteKey(FlightCode.parse(params.getFlightCode()).
                    getAirline(), params.getDepartureAirport(),
                    params.getDestinationAirport());
        } else {
            // Return null
            return null;
        }
    }
}
