/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.repositories;

import com.ricston.flights.model.entities.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Airline JPA Repository.
 * 
 * @author floverde
 * @version 1.0
 */
@Repository
public interface AirlineRepository extends JpaRepository<Airline, Integer>
{
}
