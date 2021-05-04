/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.mappers;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.mapstruct.Named;

/**
 * Utility class for mapping {@code java.time} values.
 * 
 * @author floverde
 * @version 1.0
 */
@Component
public class TimeMapper
{
    /**
     * Truncates a {@link LocalDateTime} value
     * by zeroing out seconds and nanoseconds.
     * 
     * @param value value to truncate.
     * @return see description.
     */
    @Named("truncatedToMinutes") 
    protected static final LocalDateTime truncatedToMinutes(final LocalDateTime value) { 
        return value.truncatedTo(ChronoUnit.MINUTES); 
    }
}
