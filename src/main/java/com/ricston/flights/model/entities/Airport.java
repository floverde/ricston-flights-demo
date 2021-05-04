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
 * Airport JPA Entity.
 *
 * @author floverde
 * @version 1.0
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "airports")
@SuppressWarnings("PersistenceUnitPresent")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Airport implements Serializable
{
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "Airport_ID", nullable = false)
    private Integer ID;
    
    @Size(max = 75)
    @Column(name = "Name")
    private String name;
   
    @Size(max = 50)
    @Column(name = "City")
    private String city;
    
    @Size(max = 50)
    @Column(name = "Country")
    private String country;
    
    @Size(max = 3)
    @Column(name = "IATA")
    private String iata;
    
    @Size(max = 4)
    @Column(name = "ICAO")
    private String icao;
    
    @Column(name = "Latitude")
    private Long latitude;
    
    @Column(name = "Longitude")
    private Long longitude;
    
    @Column(name = "Altitude")
    private Integer altitude;
    
    @Column(name = "Timezone")
    private Integer timezone;
    
    @Column(name = "DST")
    private Character dst;
    
    @Size(max = 50)
    @Column(name = "Tz_Timezone")
    private String tzTimezone;
    
    @Size(max = 45)
    @Column(name = "Type")
    private String type;
    
    @Size(max = 45)
    @Column(name = "Source")
    private String source;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destination")
    private Collection<Route> incomingRoutes;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departure")
    private Collection<Route> outcomingRoutes;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 5034874850446255079L;

    public Airport(final Integer airportID) {
        this.ID = airportID;
    }
}
