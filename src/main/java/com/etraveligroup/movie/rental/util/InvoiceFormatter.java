package com.etraveligroup.movie.rental.util;

import java.math.BigDecimal;

/**
 * Utility class for formatting invoice details.
 * Provides methods to format the header, line items, and footer of an invoice.
 * This class is designed to be used in conjunction with the RentalInfoService
 * to generate a formatted invoice string for a customer's movie rentals.
 * It includes methods to format the header with the customer's name,
 * format each line item with the movie title and rental amount,
 * and format the footer with the total amount owed and frequent renter points earned.
 * The methods return formatted strings that can be used to create a complete invoice.
 * This class is not intended to be instantiated, as it only contains static methods.
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */

public final class InvoiceFormatter {

    private InvoiceFormatter() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static String formatHeader(String customerName) {
        return String.format("Rental Record for %s%n", customerName);
    }

    public static String formatLine(String movieTitle, BigDecimal amount) {
        return String.format("\t%s\t%.2f%n", movieTitle, amount);
    }

    public static String formatFooter(BigDecimal totalAmount, int frequentPoints) {
        return String.format("Amount owed is %.2f%nYou earned %d frequent points%n", totalAmount, frequentPoints);
    }
}