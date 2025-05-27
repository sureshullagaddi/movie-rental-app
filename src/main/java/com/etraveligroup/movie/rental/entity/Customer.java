package com.etraveligroup.movie.rental.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Entity class representing a Customer in the movie rental system.
 * This class maps to the "Customer" table in the database and contains fields for
 * customer ID, name, and a list of movie rentals associated with the customer.
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@Table(name = "Customer")
@ToString(exclude = "rentals")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    /**
     * List of movie rentals associated with this customer.
     * This field is mapped to the MovieRental entity and is fetched lazily.
     */
    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY)
    private List<MovieRental> rentals;

}
