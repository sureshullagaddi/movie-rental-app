package com.etraveligroup.movie.rental.service.impl;


import com.etraveligroup.movie.rental.dto.CustomerRequestDTO;
import com.etraveligroup.movie.rental.entity.Customer;
import com.etraveligroup.movie.rental.entity.Movie;
import com.etraveligroup.movie.rental.entity.MovieRental;
import com.etraveligroup.movie.rental.exceptions.RentalProcessingException;
import com.etraveligroup.movie.rental.repository.CustomerRepository;
import com.etraveligroup.movie.rental.repository.MovieRepository;
import com.etraveligroup.movie.rental.service.RentalInfoService;
import com.etraveligroup.movie.rental.util.InvoiceFormatter;
import com.etraveligroup.movie.rental.util.PointsCalculator;
import com.etraveligroup.movie.rental.util.PriceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalInfoService {
    private final MovieRepository movieRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Async
    @Transactional
    @Retryable(
            value = RentalProcessingException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    @Cacheable(value = "invoices", key = "#customerRequestDTO")
    public CompletableFuture<String> generateInvoice(CustomerRequestDTO customerRequestDTO) {

        log.info("Starting invoice generation for customer: {}", customerRequestDTO.name());

        // In RentalServiceImpl.java
        Optional<Customer> customerOptional = customerRepository.findByName(customerRequestDTO.name());
        Customer customer = customerOptional.orElseThrow(() -> {
            log.error("Customer not found: {}", customerRequestDTO.name());
            return new IllegalArgumentException("Customer not found");
        });

        List<MovieRental> customerRentals = customer.getRentals();
        if (customerRentals == null || customerRentals.isEmpty()) {
            log.warn("No rentals found for customer: {}", customerRequestDTO.name());
            throw new IllegalArgumentException("No rentals found for customer");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        int frequentRenterPoints = 0;
        StringBuilder invoice = new StringBuilder(InvoiceFormatter.formatHeader(customerRequestDTO.name()));
        List<String> errors = new ArrayList<>();

        for (MovieRental rental : customerRentals) {
            try {
                Movie movie = rental.getMovie();
                var pricing = movie.getPricing();
                BigDecimal rentalAmount = PriceCalculator.calculateRentalAmount(pricing, rental.getDays());
                frequentRenterPoints += PointsCalculator.calculateFrequentRenterPoints(pricing.getCode(), rental.getDays());

                invoice.append(InvoiceFormatter.formatLine(movie.getTitle(), rentalAmount));
                totalAmount = totalAmount.add(rentalAmount);

                log.info("Processed rental: {} | Amount: {} | Points: {}", movie.getTitle(), rentalAmount, frequentRenterPoints);

            } catch (IllegalArgumentException e) {
                log.error("Validation failed for rental with movie ID {}: {}", rental.getMovie().getId(), e.getMessage());
                throw new IllegalArgumentException("Invalid rental data: " + e.getMessage(), e);

            } catch (Exception e) {
                log.error("Unexpected error processing rental for movie ID {}: {}", rental.getMovie().getId(), e.getMessage());
                errors.add("Rental for movie ID " + rental.getMovie().getId() + " failed: " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            log.error("Invoice generation failed with {} error(s)", errors.size());
            throw new RentalProcessingException("Invoice generation failed", errors);
        }

        invoice.append(InvoiceFormatter.formatFooter(totalAmount, frequentRenterPoints));
        log.info("Invoice generation completed for customer: {} | Total: {} | Points: {}", customerRequestDTO.name(), totalAmount, frequentRenterPoints);

        return CompletableFuture.completedFuture(invoice.toString());
    }
}