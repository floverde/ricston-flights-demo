/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * Airline JPA Entity.
 *
 * @author floverde
 * @version 1.0
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "airlines")
@SuppressWarnings("PersistenceUnitPresent")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Airline implements Serializable
{
    @Id
    @Column(name = "Airline_ID", nullable = false)
    private Integer ID;
    
    @Size(max = 50)
    @Column(name = "Name")
    private String name;
    
    @Size(max = 100)
    @Column(name = "Alias")
    private String alias;
    
    @Size(max = 2)
    @Column(name = "IATA")
    private String iata;
    
    @Size(max = 3)
    @Column(name = "ICAO")
    private String icao;
    
    @Size(max = 50)
    @Column(name = "Callsign")
    private String callsign;
    
    @Size(max = 50)
    @Column(name = "Country")
    private String country;
    
    @Column(name = "Active")
    private Character active;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "airline")
    private Collection<Route> routes;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -5279013308578279774L;

    public Airline(final Integer airlineID) {
        this.ID = airlineID;
    }
}
