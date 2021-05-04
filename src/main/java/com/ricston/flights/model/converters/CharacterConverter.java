/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.model.converters;

import javax.persistence.Converter;
import javax.persistence.AttributeConverter;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * JPA Character Converter.
 * 
 * @author floverde
 * @version 1.0
 */
@Converter(autoApply = true)
public class CharacterConverter implements AttributeConverter<Character, String>
{
    /**
     * Exception message indicating that the string contains
     * more than one character and cannot be converted.
     */
    private static final String TOO_LONG = "Cannot convert a string"
            + " longer than one character into Character.";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String convertToDatabaseColumn(final Character chr) {
        // Checks that the character provided is not null
        if (chr != null) {
            // Check if the character is different from zero
            if (chr != Character.MIN_VALUE) {
                // Converts the character into a string
                return CharUtils.toString(chr.charValue());
            } else {
                // Returns empty string
                return StringUtils.EMPTY;
            }
        } else {
            // Returns null
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character convertToEntityAttribute(final String str) {
        // Checks that the string provided is not null
        if (str != null) {
            // Check if the supplied string is empty
            if (str.isEmpty()) {
                // Returns the zero-coded ASCII character
                return Character.MIN_VALUE;
            } else {
                // Returns the first character of the string
                return str.charAt(0);
            }
        } else {
            // Return null
            return null;
        }
    }
}
