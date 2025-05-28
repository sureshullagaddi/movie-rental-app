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

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalInfoService {

    private final CustomerRepository customerRepository;

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