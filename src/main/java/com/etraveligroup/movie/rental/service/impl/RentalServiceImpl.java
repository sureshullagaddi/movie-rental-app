package com.etraveligroup.movie.rental.service.impl;

import com.etraveligroup.movie.rental.dto.GenerateInvoiceByNameRequestDTO;
import com.etraveligroup.movie.rental.entity.Customer;
import com.etraveligroup.movie.rental.entity.Movie;
import com.etraveligroup.movie.rental.entity.MovieRental;
import com.etraveligroup.movie.rental.exceptions.CustomerNotFoundException;
import com.etraveligroup.movie.rental.exceptions.RentalProcessingException;
import com.etraveligroup.movie.rental.exceptions.RentalsNotFoundException;
import com.etraveligroup.movie.rental.repository.CustomerRepository;
import com.etraveligroup.movie.rental.service.RentalInfoService;
import com.etraveligroup.movie.rental.util.InvoiceFormatter;
import com.etraveligroup.movie.rental.util.PointsCalculator;
import com.etraveligroup.movie.rental.util.PriceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for handling rental information and generating invoices.
 * This service interacts with the CustomerRepository to retrieve customer data and
 * processes movie rentals to generate invoices.
 * It uses caching and retry mechanisms to ensure reliability and performance.
 * @author Suresh
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalInfoService {

    private final CustomerRepository customerRepository;

    /**
     * Generates an invoice for a customer based on their name.
     * The method retrieves the customer from the repository, processes their rentals,
     * and formats the invoice accordingly.
     * @param generateInvoiceRequestDTO the request containing customer name
     * @return a Mono containing the generated invoice as a String
     * @throws CustomerNotFoundException if the customer is not found in the repository
     * @throws RentalsNotFoundException if the customer has no rentals
     * @throws RentalProcessingException if there are errors during rental processing
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Retryable(retryFor = RentalProcessingException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Cacheable(value = "invoices", key = "#generateInvoiceRequestDTO.customerName()")
    public Mono<String> generateInvoiceByName(final GenerateInvoiceByNameRequestDTO generateInvoiceRequestDTO) {
        return Mono.fromCallable(() -> {
            log.info("Starting invoice generation for customer: {}", generateInvoiceRequestDTO.customerName());
            Customer customer = customerRepository.findByName(generateInvoiceRequestDTO.customerName())
                    .orElseThrow(() -> {
                        log.error("Customer not found: {}", generateInvoiceRequestDTO.customerName());
                        return new CustomerNotFoundException("Customer not found");
                    });

            return generateInvoiceForCustomer(customer, generateInvoiceRequestDTO.customerName());
        });
    }

    /**
     * Generates an invoice for a customer based on their ID.
     * The method retrieves the customer from the repository, processes their rentals,
     * and formats the invoice accordingly.
     * @param customerId the ID of the customer for whom the invoice is to be generated
     * @return a Mono containing the generated invoice as a String
     * @throws CustomerNotFoundException if the customer is not found in the repository
     * @throws RentalsNotFoundException if the customer has no rentals
     * @throws RentalProcessingException if there are errors during rental processing
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Retryable(retryFor = RentalProcessingException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Cacheable(value = "invoices", key = "#customerId")
    public Mono<String> generateInvoiceById(Long customerId) {
        return Mono.fromCallable(() -> {
            log.info("Starting invoice generation for customer ID: {}", customerId);
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> {
                        log.error("Customer not found with ID: {}", customerId);
                        return new CustomerNotFoundException("Customer not found");
                    });

            return generateInvoiceForCustomer(customer, customer.getName());
        });
    }

    private String generateInvoiceForCustomer(Customer customer, String customerName) {
        List<MovieRental> customerRentals = customer.getRentals();
        if (customerRentals == null || customerRentals.isEmpty()) {
            log.warn("No rentals found for customer: {}", customerName);
            throw new RentalsNotFoundException("No rentals found for customer");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        int frequentRenterPoints = 0;
        StringBuilder invoice = new StringBuilder(InvoiceFormatter.formatHeader(customerName));
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
        log.info("Invoice generation completed for customer: {} | Total: {} | Points: {}", customerName, totalAmount, frequentRenterPoints);

        return invoice.toString();
    }
}