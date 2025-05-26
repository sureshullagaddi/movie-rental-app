package com.etraveligroup.movie.rental.controller;

import com.etraveligroup.movie.rental.dto.CustomerRequestDTO;
import com.etraveligroup.movie.rental.service.RentalInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Generate Invoice", description = "It generates an invoice for the customer based on their movie rentals")
public class RentalController {

    private final RentalInfoService rentalInfoService;

    /**
     * Generates an invoice for the given customer.
     *
     * @param customer and list of movie rentals
     * @return string invoice
     */
    @PostMapping("/invoice")
    @Operation(summary = "Get invoice for the customer")
    public CompletableFuture<ResponseEntity<String>> generateInvoice(@Valid @RequestBody CustomerRequestDTO customer) {
        return rentalInfoService.generateInvoice(customer)
                .thenApply(invoice -> {
                    log.info("Generated invoice for customer: {}", customer.name());
                    return ResponseEntity.ok(invoice);
                });
    }
}
