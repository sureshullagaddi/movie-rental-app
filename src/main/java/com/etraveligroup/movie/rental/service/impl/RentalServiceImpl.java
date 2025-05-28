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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RentalServiceImpl provides the implementation for generating invoices based on customer rentals.
 * It retrieves customer data from the repository and processes their rentals to generate an invoice.
 *
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalInfoService {

    private final CustomerRepository customerRepository;

    /**
     * Generates an invoice for a customer based on their name.
     * It retrieves the customer by name, processes their rentals, and generates an invoice.
     *
     * @param generateInvoiceRequestDTO DTO containing the customer name for invoice generation
     * @return Mono<String> containing the generated invoice as a string
     * @throws CustomerNotFoundException if the customer with the given name does not exist
     * @throws RentalsNotFoundException  if the customer has no rentals
     * @throws RentalProcessingException if there is an error during the rental processing
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Cacheable(value = "invoices", key = "#generateInvoiceRequestDTO.customerName()")
    public Mono<String> generateInvoiceByName(final GenerateInvoiceByNameRequestDTO generateInvoiceRequestDTO) {
        return Mono.fromCallable(() -> {
                    log.info("Starting invoice generation for customer: {}", generateInvoiceRequestDTO.customerName());
                    final Customer customer = customerRepository.findByName(generateInvoiceRequestDTO.customerName())
                            .orElseThrow(() -> {
                                log.error("Customer not found: {}", generateInvoiceRequestDTO.customerName());
                                return new CustomerNotFoundException("Customer not found");
                            });
                    return customer;
                })
                .flatMap(customer -> generateInvoiceForCustomerWithRetry(customer, generateInvoiceRequestDTO.customerName()));
    }


    /**
     * Generates an invoice for a customer based on their ID.
     * It retrieves the customer by ID, processes their rentals, and generates an invoice.
     *
     * @param customerId the ID of the customer for whom the invoice is to be generated
     * @return Mono<String> containing the generated invoice as a string
     * @throws CustomerNotFoundException if the customer with the given ID does not exist
     * @throws RentalsNotFoundException  if the customer has no rentals
     * @throws RentalProcessingException if there is an error during the rental processing
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Cacheable(value = "invoices", key = "#customerId")
    public Mono<String> generateInvoiceById(final Long customerId) {
        return Mono.fromCallable(() -> {
                    log.info("Starting invoice generation for customer ID: {}", customerId);
                    final Customer customer = customerRepository.findById(customerId)
                            .orElseThrow(() -> {
                                log.error("Customer not found with ID: {}", customerId);
                                return new CustomerNotFoundException("Customer not found");
                            });
                    return customer;
                })
                .flatMap(customer -> generateInvoiceForCustomerWithRetry(customer, customer.getName()));
    }


    /**
     * Generates an invoice for a customer with retry logic in case of rental processing errors.
     * It attempts to generate the invoice up to 2 times with a backoff strategy.
     * @param customer     the customer for whom the invoice is to be generated
     * @param customerName the name of the customer for logging purposes
     * @return Mono<String> containing the generated invoice as a string
     * @throws RentalProcessingException if all retry attempts fail
     */
    private Mono<String> generateInvoiceForCustomerWithRetry(final Customer customer, final String customerName) {
        final AtomicInteger attempt = new AtomicInteger(1);
        return Mono.fromCallable(() -> {
                    log.info("Trying to generate invoice for customer: {} (attempt #{})", customerName, attempt.getAndIncrement());
                    return generateInvoiceForCustomer(customer, customerName);
                })
                .retryWhen(
                        Retry.backoff(2, Duration.ofSeconds(1))
                                .filter(throwable -> throwable instanceof RentalProcessingException)
                                .onRetryExhaustedThrow((spec, signal) -> signal.failure())
                )
                .onErrorResume(RentalProcessingException.class, ex -> {
                    log.error("All retry attempts failed for customer: {}. Returning error message.", customerName, ex);
                    return Mono.just("Invoice generation failed after retries: " + ex.getMessage());
                });
    }

    /**
     * Generates an invoice for a customer by processing their rentals.
     * It calculates the total amount and frequent renter points based on the rentals.
     * @param customer the customer for whom the invoice is to be generated
     * @param customerName the name of the customer for logging purposes
     * @return String containing the formatted invoice
     * @throws RentalsNotFoundException if the customer has no rentals
     * @throws IllegalArgumentException if there is an error in rental data validation
     */
    private String generateInvoiceForCustomer(final Customer customer, final String customerName) {
        final List<MovieRental> customerRentals = customer.getRentals();
        if (customerRentals == null || customerRentals.isEmpty()) {
            log.warn("No rentals found for customer: {}", customerName);
            throw new RentalsNotFoundException("No rentals found for customer");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        int frequentRenterPoints = 0;
        final StringBuilder invoice = new StringBuilder(InvoiceFormatter.formatHeader(customerName));

        for (final MovieRental rental : customerRentals) {
            try {
                final Movie movie = rental.getMovie();
                final var pricing = movie.getPricing();
                final BigDecimal rentalAmount = PriceCalculator.calculateRentalAmount(pricing, rental.getDays());
                frequentRenterPoints += PointsCalculator.calculateFrequentRenterPoints(pricing.getCode(), rental.getDays());

                invoice.append(InvoiceFormatter.formatLine(movie.getTitle(), rentalAmount));
                totalAmount = totalAmount.add(rentalAmount);

                log.info("Processed rental: {} | Amount: {} | Points: {}", movie.getTitle(), rentalAmount, frequentRenterPoints);

            } catch (final IllegalArgumentException e) {
                log.error("Validation failed for rental with movie ID {}: {}", rental.getMovie().getId(), e.getMessage());
                throw new IllegalArgumentException("Invalid rental data: " + e.getMessage(), e);
            }
        }

        invoice.append(InvoiceFormatter.formatFooter(totalAmount, frequentRenterPoints));
        log.info("Invoice generation completed for customer: {} | Total: {} | Points: {}", customerName, totalAmount, frequentRenterPoints);

        return invoice.toString();
    }
}