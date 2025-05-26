package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.dto.MovieRentalDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RentalValidatorTest {

    @Test
    void validateRental_validRental_noException() {
        assertDoesNotThrow(() -> RentalValidator.validateRental(new MovieRentalDTO("F001", 2)));
    }

    @Test
    void validateRental_nullRental_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> RentalValidator.validateRental(null));
    }

    @Test
    void validateRental_blankMovieId_throwsException() {
        MovieRentalDTO rental = new MovieRentalDTO(" ",2);
        assertThrows(IllegalArgumentException.class, () -> RentalValidator.validateRental(rental));
    }

    @Test
    void validateRental_negativeDays_throwsException() {
        MovieRentalDTO rental = new MovieRentalDTO("F001", -1);
        assertThrows(IllegalArgumentException.class, () -> RentalValidator.validateRental(rental));
    }
}