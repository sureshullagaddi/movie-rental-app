package com.etraveligroup.movie.rental.controller;

import com.etraveligroup.movie.rental.exceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private org.springframework.validation.FieldError fieldError;

    @Mock
    private org.springframework.validation.BindingResult bindingResult;

    @Mock
    private HandlerMethodValidationException handlerMethodValidationException;

    @Test
    void handleValidationErrors_returnsBadRequest() {
        when(fieldError.getField()).thenReturn("field");
        when(fieldError.getDefaultMessage()).thenReturn("must not be null");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<String> response = handler.handleValidationErrors(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Validation failed: field: must not be null"));
    }

    @Test
    void handleIllegalArgument_returnsBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("bad input");
        ResponseEntity<String> response = handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input: bad input", response.getBody());
    }

    @Test
    void handleCustomerNotFound_returnsBadRequest() {
        CustomerNotFoundException ex = new CustomerNotFoundException("customer missing");
        ResponseEntity<String> response = handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("customer missing", response.getBody());
    }

    @Test
    void handleRentalsNotFound_returnsOk() {
        RentalsNotFoundException ex = new RentalsNotFoundException("no rentals");
        ResponseEntity<String> response = handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("no rentals", response.getBody());
    }

    @Test
    void handleHandlerMethodValidation_returnsBadRequest() {
        when(handlerMethodValidationException.getMessage()).thenReturn("handler validation failed");
        ResponseEntity<String> response = handler.handleIllegalArgument(handlerMethodValidationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("handler validation failed", response.getBody());
    }

    @Test
    void handleGenericException_returnsInternalServerError() {
        Exception ex = new Exception("unexpected");
        ResponseEntity<String> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("An unexpected error occurred: unexpected"));
    }

    @Test
    void handleRentalProcessing_returnsBadRequestWithDetails() {
        List<String> errors = List.of("field: error");
        RentalProcessingException ex = new RentalProcessingException("Invoice generation failed", errors);

        ResponseEntity<Map<String, Object>> response = handler.handleRentalProcessing(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
        assertEquals("Invoice generation failed", body.get("error"));
        assertEquals(errors, body.get("details"));
    }

    @Test
    void handleInvoiceParsing_returnsInternalServerError() {
        InvoiceParsingException ex = new InvoiceParsingException("parse error");
        ResponseEntity<String> response = handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("parse error", response.getBody());
    }

    @Test
    void handlePdfGeneration_returnsInternalServerError() {
        PdfGenerationException ex = new PdfGenerationException("pdf error");
        ResponseEntity<String> response = handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("pdf error", response.getBody());
    }
}