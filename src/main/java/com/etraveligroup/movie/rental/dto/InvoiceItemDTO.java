package com.etraveligroup.movie.rental.dto;

import java.math.BigDecimal;
/**
 *
 * @param title
 * @param price
 */
public record InvoiceItemDTO(String title, BigDecimal price) {}


