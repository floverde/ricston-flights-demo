/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.mappers;

import com.ricston.flights.dto.AirportSiteDTO;
import com.ricston.flights.model.entities.Airport;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

/**
 * Data Mapper used to map entity {@link Airport} to and from the JPA layer.
 * <p><i>Note:</i> it uses <b>MapStruct</b> as the mapping engine.</p>
 * 
 * @author floverde
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface AirportMapper
{
    /**
     * Converts JPA entity into a {link SiteDTO} object.
     * 
     * @param entity JPA entity to be converted.
     * @return DTO that identifies its geographic location.
     */
    @Mapping(target = "airport", source = "name")
    public AirportSiteDTO toSiteDTO(final Airport entity);
}
