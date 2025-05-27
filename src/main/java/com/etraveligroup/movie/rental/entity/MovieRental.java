package com.etraveligroup.movie.rental.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Entity class representing a Movie Rental in the movie rental system.
 * This class maps to the "MovieRental" table in the database and contains fields for
 * rental ID, number of days rented, associated customer, and the movie being rented.
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@Table(name = "MovieRental")
@ToString(exclude = "customer")
public class MovieRental implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "days")
    private int days;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
