package com.etraveligroup.movie.rental.util;

import java.math.BigDecimal;

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