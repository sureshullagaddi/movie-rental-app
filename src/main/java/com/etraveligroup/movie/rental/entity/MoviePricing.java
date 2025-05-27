package com.etraveligroup.movie.rental.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entity class representing the pricing details of a movie in the rental system.
 * This class maps to the "MoviePricing" table in the database and contains fields for
 * the pricing code, base days, base price, and extra price per day.
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@Table(name = "MoviePricing")
public class MoviePricing implements Serializable {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "base_days")
    private int baseDays;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "extra_price_per_day")
    private BigDecimal extraPricePerDay;
}