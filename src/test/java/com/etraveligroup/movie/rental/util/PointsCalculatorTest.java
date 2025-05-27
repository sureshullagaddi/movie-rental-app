package com.etraveligroup.movie.rental.util;

        import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.extension.ExtendWith;
        import org.mockito.junit.jupiter.MockitoExtension;

        import static org.junit.jupiter.api.Assertions.*;

        @ExtendWith(MockitoExtension.class)
        class PointsCalculatorTest {

            @Test
            void calculateFrequentRenterPoints_newReleaseMoreThanOneDay_returnsTwo() {
                assertEquals(2, PointsCalculator.calculateFrequentRenterPoints("NEW", 2));
            }

            @Test
            void calculateFrequentRenterPoints_newReleaseOneDay_returnsOne() {
                assertEquals(1, PointsCalculator.calculateFrequentRenterPoints("NEW", 1));
            }

            @Test
            void calculateFrequentRenterPoints_regularMovie_returnsOne() {
                assertEquals(1, PointsCalculator.calculateFrequentRenterPoints("REGULAR", 5));
            }

            @Test
            void calculateFrequentRenterPoints_nullPricingCode_throwsException() {
                assertThrows(IllegalArgumentException.class, () -> PointsCalculator.calculateFrequentRenterPoints(null, 3));
            }
        }