package com.etraveligroup.movie.rental.repository;


import com.etraveligroup.movie.rental.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    Optional<Movie> findById(String movieId);
}
