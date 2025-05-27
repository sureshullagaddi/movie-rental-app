package com.etraveligroup.movie.rental.exceptions;

/**
 * Exception thrown when there is an error parsing an invoice.
 * This could be due to invalid data format, missing fields, or other parsing issues.
 */
public class InvoiceParsingException extends RuntimeException {
    public InvoiceParsingException(String message) {
        super(message);
    }

    public InvoiceParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
