package com.etraveligroup.movie.rental.service.impl;

import com.etraveligroup.movie.rental.dto.GenerateInvoiceRequestDTO;
import com.etraveligroup.movie.rental.entity.Customer;
import com.etraveligroup.movie.rental.entity.Movie;
import com.etraveligroup.movie.rental.entity.MoviePricing;
import com.etraveligroup.movie.rental.entity.MovieRental;
import com.etraveligroup.movie.rental.exceptions.CustomerNotFoundException;
import com.etraveligroup.movie.rental.exceptions.RentalProcessingException;
import com.etraveligroup.movie.rental.exceptions.RentalsNotFoundException;
import com.etraveligroup.movie.rental.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        GenerateInvoiceRequestDTO dto = new GenerateInvoiceRequestDTO(customerName);

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
        GenerateInvoiceRequestDTO dto = new GenerateInvoiceRequestDTO(customerName);
        when(customerRepository.findByName(customerName)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> rentalService.generateInvoiceByName(dto).block());
    }

    @Test
    void generateInvoiceByName_noRentals() {
        String customerName = "No Rentals";
        GenerateInvoiceRequestDTO dto = new GenerateInvoiceRequestDTO(customerName);
        Customer customer = new Customer();
        customer.setName(customerName);
        customer.setRentals(List.of());

        when(customerRepository.findByName(customerName)).thenReturn(Optional.of(customer));

        assertThrows(RentalsNotFoundException.class, () -> rentalService.generateInvoiceByName(dto).block());
    }

    @Test
    void generateInvoiceByName_rentalWithInvalidData() {
        String customerName = "Invalid Rental";
        GenerateInvoiceRequestDTO dto = new GenerateInvoiceRequestDTO(customerName);

        Movie movie = new Movie();
        movie.setId("F002");
        movie.setTitle("Matrix");
        movie.setPricing(null);

        MovieRental rental = new MovieRental();
        rental.setMovie(movie);
        rental.setDays(2);

        Customer customer = new Customer();
        customer.setName(customerName);
        customer.setRentals(List.of(rental));

        when(customerRepository.findByName(customerName)).thenReturn(Optional.of(customer));

        assertThrows(IllegalArgumentException.class, () -> rentalService.generateInvoiceByName(dto).block());
    }

    @Test
    void generateInvoiceByName_rentalWithUnexpectedException() {
        String customerName = "Unexpected Error";
        GenerateInvoiceRequestDTO dto = new GenerateInvoiceRequestDTO(customerName);

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

        assertThrows(RentalProcessingException.class, () -> rentalService.generateInvoiceByName(dto).block());
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

        assertThrows(CustomerNotFoundException.class, () -> rentalService.generateInvoiceById(customerId).block());
    }

    @Test
    void generateInvoiceById_noRentals() {
        Long customerId = 2L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("No Rentals");
        customer.setRentals(List.of());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        assertThrows(RentalsNotFoundException.class, () -> rentalService.generateInvoiceById(customerId).block());
    }

    @Test
    void generateInvoiceById_multipleRentals_oneWithError() {
        Long customerId = 3L;

        MoviePricing pricing = new MoviePricing();
        pricing.setCode("regular");
        pricing.setBaseDays(2);
        pricing.setBasePrice(BigDecimal.valueOf(2.0));
        pricing.setExtraPricePerDay(BigDecimal.valueOf(1.5));

        Movie movie1 = new Movie();
        movie1.setId("F001");
        movie1.setTitle("You've Got Mail");
        movie1.setPricing(pricing);

        MovieRental rental1 = new MovieRental();
        rental1.setMovie(movie1);
        rental1.setDays(3);

        Movie movie2 = new Movie();
        movie2.setId("F999");
        movie2.setTitle("Broken Movie");
        movie2.setPricing(null);

        MovieRental rental2 = new MovieRental();
        rental2.setMovie(movie2);
        rental2.setDays(2);

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setRentals(List.of(rental1, rental2));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        assertThrows(RentalProcessingException.class, () -> rentalService.generateInvoiceById(customerId).block());
    }
}