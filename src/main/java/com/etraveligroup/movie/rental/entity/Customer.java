package com.etraveligroup.movie.rental.entity;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}