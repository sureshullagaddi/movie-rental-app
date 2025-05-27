package com.etraveligroup.movie.rental.dto;

import jakarta.validation.constraints.NotBlank;

public record GenerateInvoiceRequestDTO(
    @NotBlank(message = "Customer name must not be blank")
    String customerName
) {}