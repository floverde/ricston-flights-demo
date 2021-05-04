/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * Flight JPA Entity.
 * 
 * @author floverde
 * @version 1.0
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "flights_Fabrizio")
@SuppressWarnings("PersistenceUnitPresent")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Flight implements Serializable
{
    @EmbeddedId
    @EqualsAndHashCode.Include
    private Flight.PK pk;

    @Size(min = 1, max = 50)
    @Column(name = "Airline_Name", nullable = false)
    private String airlineName;
    
    @Size(min = 1, max = 3)
    @Column(name = "Departure_Airport", nullable = false)
    private String departureAirport;
    
    @Size(min = 1, max = 3)
    @Column(name = "Destination_Airport", nullable = false)
    private String destinationAirport;
    
    @Size(min = 1, max = 100)
    @Column(name = "Aircraft_Type", nullable = false)
    private String aircraftType;
    
    @Column(name = "Seat_Availability", nullable = false)
    private short seatAvailability;
    
    @Column(name = "Price", nullable = false)
    private BigDecimal price;
    
    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -1196470463308832556L;
    
    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PK implements Serializable
    {
        @Size(min = 1, max = 6)
        @Column(name = "Flight_Code", nullable = false)
        private String flightCode;
     
        @Column(name = "Departure_Date", nullable = false)
        private LocalDateTime departureDate;
        
        /**
         * Serial Version UID.
         */
        private static final long serialVersionUID = -7923068126821912073L;
    }
    
    /**
     * Create a {@link Flight} specifying its primary key.
     * 
     * @param pk primary key object.
     */
    public Flight(final Flight.PK pk) {
        this.pk = pk;
    }
    
    /**
     * Create a {@link Flight} specifying its key properties.
     * 
     * @param flightCode flight code
     * @param departureDate departure date
     */
    public Flight(final String flightCode, final LocalDateTime departureDate) {
        this(new Flight.PK(flightCode, departureDate));
    }
//    
//    /**
//     * Creates a {@link Flight} by specifying all of its properties.
//     * 
//     * @param flightCode flight code
//     * @param departureDate departure date
//     * @param airlineName airline name
//     * @param departureAirport departure airport
//     * @param destinationAirport destination airport
//     * @param aircraftType aircraft type
//     * @param seatAvailability seat availability
//     * @param price price.
//     */
//    public Flight(final String flightCode, final LocalDateTime departureDate,
//            final String airlineName, final String departureAirport, final
//            String destinationAirport, final String aircraftType, final
//            short seatAvailability, final BigDecimal price) {
//        this(flightCode, departureDate);
//        this.airlineName = airlineName;
//        this.departureAirport = departureAirport;
//        this.destinationAirport = destinationAirport;
//        this.aircraftType = aircraftType;
//        this.seatAvailability = seatAvailability;
//        this.price = price;
//    }
}
