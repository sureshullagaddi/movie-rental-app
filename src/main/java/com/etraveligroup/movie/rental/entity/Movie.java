package com.etraveligroup.movie.rental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "movies")
@Data
public class Movie {
    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(name = "movie_type", nullable = false)
    private String movieType;
}
