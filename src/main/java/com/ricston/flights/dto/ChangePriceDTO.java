/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * Change Price DTO.
 * 
 * @author floverde
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePriceDTO
{
    /**
     * Amount of increase or decrease in flight price.
     */
    @NotNull
    private BigDecimal delta;
}
