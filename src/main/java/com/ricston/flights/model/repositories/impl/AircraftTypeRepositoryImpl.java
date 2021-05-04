/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.repositories.impl;

import com.ricston.flights.model.repositories.AircraftTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.BooleanUtils;

/**
 * Implementation of {@link AircraftTypeRepository}.
 * 
 * @author floverde
 * @version 1.0
 */
@Repository
public class AircraftTypeRepositoryImpl implements AircraftTypeRepository
{
    /**
     * Entity Manager reference.
     */
    @Autowired
    private EntityManager em;
    
    /**
     * Native SQL query that checks if an aircraft model exists.
     */
    private static final String EXISTS_BY_MODEL = "SELECT COUNT(*)"
            + " FROM aircraft_type WHERE model = :model";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByModel(final String model) {
        // Executes the native SQL query and returns its boolean result
        return BooleanUtils.toBoolean(((Number) this.em.createNativeQuery(
                EXISTS_BY_MODEL).setParameter("model", model).
                getSingleResult()).intValue());
    }   
}
