package com.etraveligroup.movie.rental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Data
@Table(name = "Movie")
public class Movie implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "code", referencedColumnName = "code")
    private MoviePricing pricing;
}

