package com.etraveligroup.movie.rental.exceptions;

public class PdfGenerationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
        * Constructs a new PdfGenerationException with the specified message.
        * @param message the detail message
        */
    public PdfGenerationException(String message) {
        super(message);
    }
    /**
        * Constructs a new PdfGenerationException with the specified message and cause.
        * @param message the detail message
        * @param cause the cause of the exception
        */
    public PdfGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
