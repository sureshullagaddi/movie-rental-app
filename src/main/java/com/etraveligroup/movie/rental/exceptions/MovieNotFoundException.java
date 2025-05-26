package com.etraveligroup.movie.rental.exceptions;

import java.io.Serial;

/**
 * Exception thrown when a movie is not found for a given ID.
 */
public class MovieNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new MovieNotFoundException with the specified movie ID.
     *
     * @param movieId the ID of the movie that was not found
     */
    public MovieNotFoundException(String movieId) {
        super("Movie not found for the id: " + movieId);
    }
}