/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * Route JPA Entity.
 *
 * @author floverde
 * @version 1.0
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "routes")
@SuppressWarnings("PersistenceUnitPresent")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Route implements Serializable
{
    @EmbeddedId
    private Route.PK pk;
    
    @Size(min = 1, max = 3)
    @Column(name = "Airline", nullable = false)
    private String airlineCode;
    
    @Size(min = 1, max = 4)
    @Column(name = "Source_Airport", nullable = false)
    private String sourceAirport;
    
    @Size(min = 1, max = 4)
    @Column(name = "Destination_Airport", nullable = false)
    private String destinationAirport;
    
    @Column(name = "Codeshare")
    private Character codeshare;
    
    @Column(name = "Stops")
    private Boolean stops;
    
    @Size(max = 20)
    @Column(name = "Equipment")
    private String equipment;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "Airline_ID", referencedColumnName = "Airline_ID", insertable = false, updatable = false)
    private Airline airline;
    
    @JoinColumn(name = "Source_Airport_ID", referencedColumnName = "Airport_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Airport departure;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "Destination_Airport_ID", referencedColumnName = "Airport_ID", insertable = false, updatable = false)
    private Airport destination;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 2306143108689248300L;
    
    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PK implements Serializable
    {
        @Column(name = "Airline_ID", nullable = false)
        private int airlineID;

        @Column(name = "Source_Airport_ID", nullable = false)
        private int sourceAirportID;

        @Column(name = "Destination_Airport_ID", nullable = false)
        private int destinationAirportID;
        
        /**
         * Serial Version UID.
         */
        private static final long serialVersionUID = 3568710442981347990L;
    }

    public Route(final Route.PK pk) {
        this.pk = pk;
    }
    
    public Route(final int airlineID, final int sourceAirportID, final int destinationAirportID) {
        this(new Route.PK(airlineID, sourceAirportID, destinationAirportID));
    }

    public Route(final int airlineID, final int sourceAirportID, final int destinationAirportID,
            final String airlineCode, final String sourceAirport, final String destinationAirport,
            final Character codeshare, final Boolean stops, final String equipment) {
        this(airlineID, sourceAirportID, destinationAirportID);
        this.destinationAirport = destinationAirport;
        this.sourceAirport = sourceAirport;
        this.airlineCode = airlineCode;
        this.codeshare = codeshare;
        this.equipment = equipment;
        this.stops = stops;
    }
}
