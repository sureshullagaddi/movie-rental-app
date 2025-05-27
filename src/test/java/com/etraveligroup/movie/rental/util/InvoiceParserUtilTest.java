package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.dto.InvoiceItemDTO;
import com.etraveligroup.movie.rental.dto.InvoiceResponseDTO;
import com.etraveligroup.movie.rental.exceptions.InvoiceParsingException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InvoiceParserUtilTest {

    @ParameterizedTest
    @MethodSource("validInvoiceProvider")
    void parseInvoiceText_validInvoices(
            String invoice,
            String expectedCustomer,
            BigDecimal expectedTotal,
            int expectedPoints,
            List<String> expectedTitles,
            List<BigDecimal> expectedPrices
    ) {
        InvoiceResponseDTO result = InvoiceParserUtil.parseInvoiceText(invoice);
        assertEquals(expectedCustomer, result.customer());
        assertEquals(expectedTotal, result.total());
        assertEquals(expectedPoints, result.frequentPoints());
        List<InvoiceItemDTO> items = result.items();
        assertEquals(expectedTitles.size(), items.size());
        for (int i = 0; i < items.size(); i++) {
            assertEquals(expectedTitles.get(i), items.get(i).title());
            assertEquals(expectedPrices.get(i), items.get(i).price());
        }
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> validInvoiceProvider() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(
                        """
                                Rental Record for John Doe
                                \tMovie 1\t10.00
                                \tMovie 2\t5.50
                                Amount owed is 15.50
                                You earned 3 frequent points
                                """,
                        "John Doe",
                        new BigDecimal("15.50"),
                        3,
                        List.of("Movie 1", "Movie 2"),
                        List.of(new BigDecimal("10.00"), new BigDecimal("5.50"))
                ),
                org.junit.jupiter.params.provider.Arguments.of(
                        """
                                Rental Record for Alice
                                \tMatrix\t7.99
                                Amount owed is 7.99
                                You earned 1 frequent points
                                """,
                        "Alice",
                        new BigDecimal("7.99"),
                        1,
                        List.of("Matrix"),
                        List.of(new BigDecimal("7.99"))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("invalidInvoiceProvider")
    void parseInvoiceText_invalidInvoices_throwsException(String invoice) {
        assertThrows(InvoiceParsingException.class, () -> InvoiceParserUtil.parseInvoiceText(invoice));
    }

    static Stream<String> invalidInvoiceProvider() {
        return Stream.of(
                null,
                "   ",
                "Rental Record for John\nAmount owed is 10.00",
                """
                        John Doe
                        \tMovie\t5.00
                        Amount owed is 5.00
                        You earned 1 frequent points
                        """,
                """
                        Rental Record for John
                        \tMovieOnly
                        Amount owed is 5.00
                        You earned 1 frequent points
                        """,
                """
                        Rental Record for John
                        \tMovie\t5.00
                        Amount owed is abc
                        You earned 1 frequent points
                        """,
                """
                        Rental Record for John
                        \tMovie\t5.00
                        Amount owed is 5.00
                        You earned abc frequent points
                        """,
                """
                        Rental Record for John
                        \tMovie\t5.00
                        Amount owed is 5.00
                        You earned  frequent points
                        """
        );
    }
}