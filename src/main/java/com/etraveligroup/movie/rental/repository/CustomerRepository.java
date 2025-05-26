// java/com/etraveligroup/movie/rental/repository/CustomerRepository.java
package com.etraveligroup.movie.rental.repository;

import com.etraveligroup.movie.rental.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByName(String name);
}