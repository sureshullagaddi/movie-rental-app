package com.etraveligroup.movie.rental.repository;

import com.etraveligroup.movie.rental.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for managing Customer entities.
 * Provides methods to perform CRUD operations and custom queries.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Finds a customer by their name.
    Optional<Customer> findByName(String name);
}