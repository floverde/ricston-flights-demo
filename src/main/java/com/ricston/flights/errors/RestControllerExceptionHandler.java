/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.errors;

import java.io.Serializable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.Value;
import org.springframework.web.bind.MethodArgumentNotValidException;


/**
 * REST Controller Exception Handler.
 * <p>This component is tasked with catching exceptions
 * raised by REST controllers and handling them.</p>
 * 
 * @author floverde
 * @version 1.0
 */
@Slf4j
@ControllerAdvice
public class RestControllerExceptionHandler
{
    /**
     * Catches {@link RicstonFlightException}s.
     * 
     * @param ex {@link RicstonFlightException} instance.
     * @return an {@link ResponseEntity} with the HTTP code and the exception message.
     */
    @ExceptionHandler(RicstonFlightException.class)
    public ResponseEntity<Object> handle(final RicstonFlightException ex) {
        final Object payload;
        final HttpStatus status;
        // Writes the exception message to the log
        log.error(ex.getMessage());
        // Extracts a payload representing the exception
        payload = RestControllerExceptionHandler.payload(ex);
        // Determines the HTTP code for the raised exception
        status = RestControllerExceptionHandler.detect(ex);
        // Builds the HTTP response with the error details
        return new ResponseEntity(payload, status);
    }
    
    /**
     * Catches {@link MethodArgumentNotValidException}s.
     * 
     * @param ex {@link MethodArgumentNotValidException} instance.
     * @return an {@link ResponseEntity} with the HTTP code and the exception message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handle(final MethodArgumentNotValidException ex) {
        // Writes an error message to the log reporting the exception
        log.error("Intercepted MethodArgumentNotValidException", ex);
        // Handles this exception as an unknown validation error
        return this.handle(new RicstonValidationException(ex.getMessage()));
    }
    
    /**
     * Extracts a payload representing the exception.
     * 
     * @param ex {@link RicstonFlightException} instance.
     * @return payload representing the exception provided.
     */
    private static Serializable payload(final RicstonFlightException ex) {
        // Creates an object that encapsulates the exception details
        return new RestControllerExceptionHandler.ErrorDetails(
                ex.getCode().getCode(), ex.getMessage());
    }
    
    /**
     * Internal method that provides an HTTP code for
     * the various {@link RicstonFlightException} types.
     * 
     * @param ex {@link RicstonFlightException} instance.
     * @return HTTP code associated with the exception.
     */
    private static HttpStatus detect(final RicstonFlightException ex) {
        // Checks if the exception indicates an illegal state of the resource
        if (ex instanceof RicstonIllegalStateException) {
            // Return HTTP 409 - Conflit
            return HttpStatus.CONFLICT;
        }
        // Check if the exception is a validation error 
        if (ex instanceof RicstonValidationException) {
            // Return HTTP 400 - Bad Request
            return HttpStatus.BAD_REQUEST;
        }
        // Check if the exception indicates a resource not found
        if (ex instanceof RicstonNotFoundException) {
            // Return HTTP 404 - Not Found
            return HttpStatus.NOT_FOUND;
        }
        // Return HTTP 500 - Internal Server Error
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * Payload describing a RicstonFlightException.
     */
    @Value
    private static final class ErrorDetails implements Serializable
    {
        /**
         * Error code.
         */
        private final int code;
        
        /**
         * Detailed message.
         */
        private final String message;
        
        /**
         * Serial Version UID.
         */
        private static final long serialVersionUID = -8130500276942901882L;
    }
}
