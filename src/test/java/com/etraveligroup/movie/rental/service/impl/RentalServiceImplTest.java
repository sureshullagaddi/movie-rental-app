package com.etraveligroup.movie.rental.service.impl;

import com.etraveligroup.movie.rental.dto.GenerateInvoiceByNameRequestDTO;
import com.etraveligroup.movie.rental.entity.Customer;
import com.etraveligroup.movie.rental.entity.Movie;
import com.etraveligroup.movie.rental.entity.MoviePricing;
import com.etraveligroup.movie.rental.entity.MovieRental;
import com.etraveligroup.movie.rental.exceptions.CustomerNotFoundException;
import com.etraveligroup.movie.rental.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RentalServiceImpl rentalService;

    @Test
    void generateInvoiceByName_successful() {
        String customerName = "John Doe";
        GenerateInvoiceByNameRequestDTO dto = new GenerateInvoiceByNameRequestDTO(customerName);

        MoviePricing pricing = new MoviePricing();
        pricing.setCode("regular");
        pricing.setBaseDays(2);
        pricing.setBasePrice(BigDecimal.valueOf(2.0));
        pricing.setExtraPricePerDay(BigDecimal.valueOf(1.5));

        Movie movie = new Movie();
        movie.setId("F001");
        movie.setTitle("You've Got Mail");
        movie.setPricing(pricing);

        MovieRental rental = new MovieRental();
        rental.setMovie(movie);
        rental.setDays(3);

        Customer customer = new Customer();
        customer.setName(customerName);
        customer.setRentals(List.of(rental));

        when(customerRepository.findByName(customerName)).thenReturn(Optional.of(customer));

        String invoice = rentalService.generateInvoiceByName(dto).block();

        assertNotNull(invoice);
        assertTrue(invoice.contains(customerName));
        assertTrue(invoice.contains(movie.getTitle()));
    }

    @Test
    void generateInvoiceByName_customerNotFound() {
        String customerName = "Unknown";
        GenerateInvoiceByNameRequestDTO dto = new GenerateInvoiceByNameRequestDTO(customerName);
        when(customerRepository.findByName(customerName)).thenReturn(Optional.empty());

        Mono<String> result = rentalService.generateInvoiceByName(dto);
        assertThrows(CustomerNotFoundException.class, result::block);
    }

    @Test
    void generateInvoiceByName_noRentals() {
        String customerName = "No Rentals";
        GenerateInvoiceByNameRequestDTO dto = new GenerateInvoiceByNameRequestDTO(customerName);
        Customer customer = new Customer();
        customer.setName(customerName);
        customer.setRentals(List.of());

        when(customerRepository.findByName(customerName)).thenReturn(Optional.of(customer));

        Mono<String> result = rentalService.generateInvoiceByName(dto);
        assertThrows(com.etraveligroup.movie.rental.exceptions.RentalsNotFoundException.class, result::block);
    }


    @Test
    void generateInvoiceByName_rentalWithUnexpectedException() {
        String customerName = "Unexpected Error";
        GenerateInvoiceByNameRequestDTO dto = new GenerateInvoiceByNameRequestDTO(customerName);

        MoviePricing pricing = new MoviePricing();
        pricing.setCode("regular");
        pricing.setBaseDays(2);
        pricing.setBasePrice(BigDecimal.valueOf(2.0));
        pricing.setExtraPricePerDay(BigDecimal.valueOf(1.5));

        Movie movie = new Movie();
        movie.setId("F003");
        movie.setTitle("Cars");
        movie.setPricing(pricing);

        MovieRental rental = spy(new MovieRental());
        rental.setMovie(movie);
        rental.setDays(2);

        doThrow(new RuntimeException("DB error")).when(rental).getMovie();

        Customer customer = new Customer();
        customer.setName(customerName);
        customer.setRentals(List.of(rental));

        when(customerRepository.findByName(customerName)).thenReturn(Optional.of(customer));

        String result = rentalService.generateInvoiceByName(dto).block();
        assertNotNull(result);
        assertTrue(result.startsWith("Invoice generation failed after retries:"));
    }

    @Test
    void generateInvoiceById_successful() {
        Long customerId = 1L;

        MoviePricing pricing = new MoviePricing();
        pricing.setCode("regular");
        pricing.setBaseDays(2);
        pricing.setBasePrice(BigDecimal.valueOf(2.0));
        pricing.setExtraPricePerDay(BigDecimal.valueOf(1.5));

        Movie movie = new Movie();
        movie.setId("F001");
        movie.setTitle("You've Got Mail");
        movie.setPricing(pricing);

        MovieRental rental = new MovieRental();
        rental.setMovie(movie);
        rental.setDays(3);

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setRentals(List.of(rental));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        String invoice = rentalService.generateInvoiceById(customerId).block();

        assertNotNull(invoice);
        assertTrue(invoice.contains(customer.getName()));
        assertTrue(invoice.contains(movie.getTitle()));
    }

    @Test
    void generateInvoiceById_customerNotFound() {
        Long customerId = 99L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Mono<String> result = rentalService.generateInvoiceById(customerId);
        assertThrows(CustomerNotFoundException.class, result::block);
    }

    @Test
    void generateInvoiceById_noRentals() {
        Long customerId = 2L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("No Rentals");
        customer.setRentals(List.of());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Mono<String> result = rentalService.generateInvoiceById(customerId);
        assertThrows(com.etraveligroup.movie.rental.exceptions.RentalsNotFoundException.class, result::block);
    }
}