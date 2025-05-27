package com.etraveligroup.movie.rental.repository;

import com.etraveligroup.movie.rental.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for managing Movie entities.
 * Provides methods to perform CRUD operations and custom queries.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    // Finds a movie by its ID.
    Optional<Movie> findById(String movieId);
}
