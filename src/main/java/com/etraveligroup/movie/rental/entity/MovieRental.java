package com.etraveligroup.movie.rental.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "movie_rentals")
@Data
public class MovieRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(nullable = false)
    private int days;
}
