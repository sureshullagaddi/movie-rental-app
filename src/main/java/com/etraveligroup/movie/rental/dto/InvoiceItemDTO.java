package com.etraveligroup.movie.rental.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Invoice Item.
 * This record represents an item in an invoice, containing the title of the item and its price.
 * * @param title the title of the invoice item
 * * @param price the price of the invoice item
 * * @author Suresh
 * * @version 1.0
 * * @since 1.0
 */
public record InvoiceItemDTO(String title, BigDecimal price) {
}


