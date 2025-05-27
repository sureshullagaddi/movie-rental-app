package com.etraveligroup.movie.rental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entity class representing a Movie in the rental system.
 * This class maps to the "Movie" table in the database and contains fields for
 * the movie ID, title, and pricing information.
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@Table(name = "Movie")
public class Movie implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    /**
     * The pricing information for the movie.
     * This field is mapped to the MoviePricing entity and is used to determine the rental price of the movie.
     */
    @ManyToOne
    @JoinColumn(name = "code", referencedColumnName = "code")
    private MoviePricing pricing;
}

