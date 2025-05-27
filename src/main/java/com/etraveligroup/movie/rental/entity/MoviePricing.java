package com.etraveligroup.movie.rental.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

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