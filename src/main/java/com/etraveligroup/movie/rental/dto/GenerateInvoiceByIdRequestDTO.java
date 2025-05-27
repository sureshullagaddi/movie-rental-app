package com.etraveligroup.movie.rental.dto;

import jakarta.validation.constraints.NotNull;

public record GenerateInvoiceByIdRequestDTO(
    @NotNull(message = "Customer ID must not be null")
    Long customerId
) {}