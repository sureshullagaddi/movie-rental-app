package com.etraveligroup.movie.rental.constants;

/**
 * Movie rental app constants
 */
public final class MovieRentalConstants {

    private MovieRentalConstants() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }
    // Movie types
    public static final String NEW = "new";
    public static final int DAYS_RENTED_2 = 2;
    public static final int DAYS_RENTED_1 = 1;

    // Invoice prefixes
    public static final String PREFIX_CUSTOMER = "Rental Record for ";
    public static final String PREFIX_TOTAL = "Amount owed is";
    public static final String PREFIX_POINTS = "You earned";

    // Invoice messages
    public static final String CUSTOMER_ID_LABEL = "customer ID: ";
    public static final String CUSTOMER_LABEL = "customer: ";
    public static final String INVOICE_NOT_FOUND_FOR = "Invoice not found for ";

}