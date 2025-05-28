package com.etraveligroup.movie.rental.controller;

import com.etraveligroup.movie.rental.exceptions.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * This class handles various exceptions thrown by the application and formats the error responses.
 * It captures validation errors, illegal arguments, and specific exceptions related to customer and rental processing.
 * The responses are formatted as JSON with appropriate HTTP status codes.
 *
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */
@RestControllerAdvice
@Tag(name = "Internal", description = "Internal controller for handling exceptions")
@Hidden // This controller is not exposed in the API documentation
public class GlobalExceptionHandler {

    /**
     * Handles constraint violation exceptions.
     * This method captures validation errors that occur when constraints on method parameters are violated.
     * * @param ex the ConstraintViolationException containing validation errors
     * * @return a ResponseEntity with a bad request status and a map of error messages
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv ->
                errors.put("error", cv.getMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles validation errors for method arguments.
     * This method captures validation exceptions thrown by Spring and formats the error messages.
     *
     * @param ex the MethodArgumentNotValidException containing validation errors
     * @return a ResponseEntity with a bad request status and a formatted error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body("Validation failed: " + errorMessage);
    }


    /**
     * Handles illegal argument exceptions.
     * This method captures illegal argument exceptions thrown by the application and formats the error message.
     *
     * @param ex the IllegalArgumentException containing the error message
     * @return a ResponseEntity with a bad request status and a formatted error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + ex.getMessage());
    }

    /**
     * Handles customer not found exceptions.
     * This method captures exceptions when a customer is not found and formats the error message.
     *
     * @param ex the CustomerNotFoundException containing the error message
     * @return a ResponseEntity with a bad request status and a formatted error message
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleIllegalArgument(CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles rentals not found exceptions.
     * This method captures exceptions when rentals are not found for a customer and formats the error message.
     *
     * @param ex the RentalsNotFoundException containing the error message
     * @return a ResponseEntity with an OK status and a formatted error message
     */
    @ExceptionHandler(RentalsNotFoundException.class)
    public ResponseEntity<String> handleIllegalArgument(RentalsNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.OK).body(ex.getMessage());
    }

    /**
     * Handles validation exceptions thrown by handler methods.
     * This method captures validation exceptions thrown by handler methods and formats the error message.
     *
     * @param ex the HandlerMethodValidationException containing the error message
     * @return a ResponseEntity with a bad request status and a formatted error message
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<String> handleIllegalArgument(HandlerMethodValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles generic exceptions.
     * This method captures any unexpected exceptions thrown by the application and formats the error message.
     *
     * @param ex the Exception containing the error message
     * @return a ResponseEntity with an internal server error status and a formatted error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }

    /**
     * Handles rental processing exceptions.
     * This method captures exceptions related to rental processing, such as invoice generation failures,
     * and formats the error message.
     *
     * @param ex the RentalProcessingException containing the error details
     * @return a ResponseEntity with a bad request status and a formatted error message
     */
    @ExceptionHandler(RentalProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleRentalProcessing(RentalProcessingException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Invoice generation failed");
        response.put("details", ex.getErrors());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handles invoice parsing exceptions.
     * This method captures exceptions related to invoice parsing failures and formats the error message.
     *
     * @param ex the InvoiceParsingException containing the error message
     * @return a ResponseEntity with an internal server error status and a formatted error message
     */
    @ExceptionHandler(InvoiceParsingException.class)
    public ResponseEntity<String> handleIllegalArgument(InvoiceParsingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    /**
     * Handles PDF generation exceptions.
     * This method captures exceptions related to PDF generation failures and formats the error message.
     * * @param ex the PdfGenerationException containing the error message
     * * @return a ResponseEntity with an internal server error status and a formatted error message
     */
    @ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity<String> handleIllegalArgument(PdfGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleNotFound(NoHandlerFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Required path variable 'customerId' is missing or URL is incorrect.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}