/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.repositories;

/**
 * Aircraft type JPA Repository.
 * 
 * @author floverde
 * @version 1.0
 */
public interface AircraftTypeRepository
{
    /**
     * Check if a given aircraft model exists.
     * 
     * @param model aircraft model to be checked.
     * @return {@code true} if the model exists, {@code false} otherwise.
     */
    public boolean existsByModel(final String model);
}
