package com.etraveligroup.movie.rental.service.impl;

import com.etraveligroup.movie.rental.dto.CustomerRequestDTO;
import com.etraveligroup.movie.rental.dto.MovieRentalDTO;
import com.etraveligroup.movie.rental.entity.Movie;
import com.etraveligroup.movie.rental.exceptions.RentalProcessingException;
import com.etraveligroup.movie.rental.repository.MovieRepository;
import com.etraveligroup.movie.rental.util.PointsCalculator;
import com.etraveligroup.movie.rental.util.RentalValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private RentalServiceImpl rentalService;

    private Movie movie;
    private MovieRentalDTO rentalDTO;
    private CustomerRequestDTO customerDTO;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId("M1");
        movie.setTitle("Test Movie");
        movie.setMovieType("REGULAR");

        rentalDTO = new MovieRentalDTO("M1", 3);
        customerDTO = new CustomerRequestDTO("John Doe", List.of(rentalDTO));
    }

    @Test
    void generateInvoice_successful() {
        when(movieRepository.findById("M1")).thenReturn(Optional.of(movie));

        rentalService.generateInvoice(customerDTO);
        verify(movieRepository, times(1)).findById("M1");
    }

    @Test
    void generateInvoice_noRentals_throwsException() {
        CustomerRequestDTO emptyCustomer = new CustomerRequestDTO("Jane", List.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> rentalService.generateInvoice(emptyCustomer));
        assertTrue(ex.getMessage().contains("No rentals to process"));
    }

    @Test
    void generateInvoice_movieNotFound_throwsRentalProcessingException() {
        when(movieRepository.findById("M1")).thenReturn(Optional.empty());

        RentalProcessingException ex = assertThrows(RentalProcessingException.class,
                () -> rentalService.generateInvoice(customerDTO));
        assertTrue(ex.getMessage().contains("Invoice generation failed"));
    }

    @Test
    void generateInvoice_invalidRental_throwsIllegalArgumentException() {
        // Simulate RentalValidator throwing IllegalArgumentException
        try (MockedStatic<RentalValidator> validatorMock = mockStatic(RentalValidator.class)) {
            validatorMock.when(() -> RentalValidator.validateRental(any())).thenThrow(new IllegalArgumentException("Invalid rental"));

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> rentalService.generateInvoice(customerDTO));
            assertTrue(ex.getMessage().contains("Invalid rental data"));
        }
    }

    @Test
    void generateInvoice_unexpectedException_throwsRentalProcessingException() {
        // Simulate unexpected exception in PointsCalculator
        try (MockedStatic<RentalValidator> validatorMock = mockStatic(RentalValidator.class);
             MockedStatic<PointsCalculator> pointsMock = mockStatic(PointsCalculator.class)) {
            validatorMock.when(() -> RentalValidator.validateRental(any())).thenCallRealMethod();
            when(movieRepository.findById("M1")).thenReturn(Optional.of(movie));
            pointsMock.when(() -> PointsCalculator.calculateFrequentRenterPoints(any(), anyInt()))
                    .thenThrow(new RuntimeException("Unexpected error"));

            RentalProcessingException ex = assertThrows(RentalProcessingException.class,
                    () -> rentalService.generateInvoice(customerDTO));
            assertTrue(ex.getMessage().contains("Invoice generation failed"));
        }
    }
}