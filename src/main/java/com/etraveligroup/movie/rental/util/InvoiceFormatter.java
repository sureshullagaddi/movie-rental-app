package com.etraveligroup.movie.rental.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * Utility class for formatting invoice details.
 * Provides methods to format the header, line items, and footer of an invoice.
 * This class is designed to be used in conjunction with the RentalInfoService
 *
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public final class InvoiceFormatter {

    private InvoiceFormatter() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static String formatHeader(String customerName) {
        String header = String.format("Rental Record for %s%n", customerName);
        log.debug("Formatted invoice header for customer: {}", customerName);
        return header;
    }

    public static String formatLine(String movieTitle, BigDecimal amount) {
        String line = String.format("\t%s\t%.2f%n", movieTitle, amount);
        log.debug("Formatted invoice line: movieTitle={}, amount={}", movieTitle, amount);
        return line;
    }

    public static String formatFooter(BigDecimal totalAmount, int frequentPoints) {
        String footer = String.format("Amount owed is %.2f%nYou earned %d frequent points%n", totalAmount, frequentPoints);
        log.debug("Formatted invoice footer: totalAmount={}, frequentPoints={}", totalAmount, frequentPoints);
        return footer;
    }
}