package com.etraveligroup.movie.rental.service.impl;

import com.etraveligroup.movie.rental.dto.CustomerRequestDTO;
import com.etraveligroup.movie.rental.dto.MovieRentalDTO;
import com.etraveligroup.movie.rental.entity.Movie;
import com.etraveligroup.movie.rental.enums.MovieType;
import com.etraveligroup.movie.rental.exceptions.MovieNotFoundException;
import com.etraveligroup.movie.rental.exceptions.RentalProcessingException;
import com.etraveligroup.movie.rental.repository.MovieRepository;
import com.etraveligroup.movie.rental.service.RentalInfoService;
import com.etraveligroup.movie.rental.util.InvoiceFormatter;
import com.etraveligroup.movie.rental.util.PointsCalculator;
import com.etraveligroup.movie.rental.util.RentalCalculator;
import com.etraveligroup.movie.rental.util.RentalValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of RentalInfoService that handles rental processing and invoice generation.
 * This service uses asynchronous processing and caching to improve performance.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalInfoService {
    private final MovieRepository movieRepository;

    @Override
    @Async
    @Retryable(
            value = RentalProcessingException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    @Cacheable(value = "invoices", key = "#customer")
    public CompletableFuture<String> generateInvoice(CustomerRequestDTO customer) {
        log.info("Starting invoice generation for customer: {}", customer.name());

        List<MovieRentalDTO> rentals = Optional.ofNullable(customer.rentals())
                .filter(r -> !r.isEmpty())
                .orElseThrow(() -> {
                    log.warn("No rentals found for customer: {}", customer.name());
                    return new IllegalArgumentException("No rentals to process");
                });

        BigDecimal totalAmount = BigDecimal.ZERO;
        int frequentRenterPoints = 0;
        StringBuilder invoice = new StringBuilder(InvoiceFormatter.formatHeader(customer.name()));
        List<String> errors = new ArrayList<>();

        for (MovieRentalDTO rental : rentals) {
            try {
                log.debug("Validating rental: {}", rental);
                RentalValidator.validateRental(rental);

                log.info("Fetching movie details for ID: {}", rental.movieId());
                Movie movie = movieRepository.findById(rental.movieId())
                        .orElseThrow(() -> new MovieNotFoundException(rental.movieId()));

                MovieType movieType = MovieType.valueOf(movie.getMovieType().toUpperCase());

                log.debug("Calculating rental amount for movie: {} ({} days)", movie.getTitle(), rental.days());
                BigDecimal rentalAmount = RentalCalculator.calculateAmount(movieType, rental.days());

                log.debug("Calculating frequent renter points for movie: {}", movie.getTitle());
                frequentRenterPoints += PointsCalculator.calculateFrequentRenterPoints(movieType, rental.days());

                invoice.append(InvoiceFormatter.formatLine(movie.getTitle(), rentalAmount));
                totalAmount = totalAmount.add(rentalAmount);

                log.info("Processed rental: {} | Amount: {} | Points: {}", movie.getTitle(), rentalAmount, frequentRenterPoints);

            } catch (IllegalArgumentException e) {
                log.error("Validation failed for rental with movie ID {}: {}", rental.movieId(), e.getMessage());
                throw new IllegalArgumentException("Invalid rental data: " + e.getMessage(), e);

            } catch (Exception e) {
                log.error("Unexpected error processing rental for movie ID {}: {}", rental.movieId(), e.getMessage());
                errors.add("Rental for movie ID " + rental.movieId() + " failed: " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            log.error("Invoice generation failed with {} error(s)", errors.size());
            throw new RentalProcessingException("Invoice generation failed", errors);
        }

        invoice.append(InvoiceFormatter.formatFooter(totalAmount, frequentRenterPoints));
        log.info("Invoice generation completed for customer: {} | Total: {} | Points: {}", customer.name(), totalAmount, frequentRenterPoints);

        return CompletableFuture.completedFuture(invoice.toString());
    }
}