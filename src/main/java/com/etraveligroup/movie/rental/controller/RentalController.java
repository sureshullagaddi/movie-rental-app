package com.etraveligroup.movie.rental.controller;

import com.etraveligroup.movie.rental.dto.GenerateInvoiceByNameRequestDTO;
import com.etraveligroup.movie.rental.dto.InvoiceResponseDTO;
import com.etraveligroup.movie.rental.service.RentalInfoService;
import com.etraveligroup.movie.rental.service.impl.PdfGenerateService;
import com.etraveligroup.movie.rental.util.InvoiceParserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.etraveligroup.movie.rental.constants.MovieRentalConstants.*;

/**
 * RentalController handles requests related to generating invoices for movie rentals.
 * It provides endpoints to generate invoices by customer name or ID.
 *
 * @author Suresh
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/generate")
@RequiredArgsConstructor
@Tag(name = "Invoice API", description = "Generate Invoices for Movie Rentals")
@Validated
public class RentalController {

    private final RentalInfoService rentalInfoService;
    private final PdfGenerateService pdfGeneratorService;

    /**
     * Generates an invoice based on the customer name or ID.
     * If the request contains a customer name, it generates an invoice by name.
     * If the request contains a customer ID, it generates an invoice by ID.
     *
     * @param generateInvoiceByNameDTO DTO containing customer name for invoice generation
     * @param acceptHeader             the Accept header to determine response format
     * @return Mono<ResponseEntity < Object>> containing the generated invoice in the requested format
     * @throws IllegalArgumentException if the customer name is null or empty
     */
    @PostMapping(value = "/invoice", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Generates invoice by customer name")
    public Mono<ResponseEntity<Object>> generateInvoice(
            @Valid @RequestBody GenerateInvoiceByNameRequestDTO generateInvoiceByNameDTO,
            @RequestHeader("Accept") String acceptHeader) {
        log.info("Received request to generate invoice by {}{}, Accept: {}", CUSTOMER_LABEL, generateInvoiceByNameDTO.customerName(), acceptHeader);

        return rentalInfoService.generateInvoiceByName(generateInvoiceByNameDTO)
                .map(invoiceText -> buildInvoiceResponse(invoiceText, acceptHeader, generateInvoiceByNameDTO.customerName(), null))
                .doOnError(e -> log.error("Error generating invoice for {}{}", CUSTOMER_LABEL, generateInvoiceByNameDTO.customerName(), e));
    }

    /**
     * Generates an invoice based on the customer ID.
     *
     * @param customerId   the ID of the customer for whom the invoice is to be generated
     * @param acceptHeader the Accept header to determine response format
     * @return Mono<ResponseEntity < Object>> containing the generated invoice in the requested format
     * @throws IllegalArgumentException if the customer ID is null or not positive
     */
    @GetMapping(value = "/invoice/{customerId}", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Generates invoice by customer ID")
    public Mono<ResponseEntity<Object>> generateInvoiceById(
            @PathVariable @NotNull @Positive Long customerId,
            @RequestHeader("Accept") String acceptHeader) {
        log.info("Received request to generate invoice by {}{}, Accept: {}", CUSTOMER_ID_LABEL, customerId, acceptHeader);

        return rentalInfoService.generateInvoiceById(customerId)
                .map(invoiceText -> buildInvoiceResponse(invoiceText, acceptHeader, null, customerId))
                .doOnError(e -> log.error("Error generating invoice for {}{}", CUSTOMER_ID_LABEL, customerId, e));
    }

    /**
     * Builds the response entity for the generated invoice based on the requested format.
     * @param invoiceText  the generated invoice text
     * @param acceptHeader the Accept header to determine response format
     * @param customerName the name of the customer (if available)
     * @param customerId   the ID of the customer (if available)
     * @return ResponseEntity containing the invoice in the requested format
     */
    private ResponseEntity<Object> buildInvoiceResponse(String invoiceText, String acceptHeader, String customerName, Long customerId) {
        String label = customerName != null ? CUSTOMER_LABEL + customerName : CUSTOMER_ID_LABEL + customerId;
        if (invoiceText == null) {
            String notFoundMsg = INVOICE_NOT_FOUND_FOR + label;
            log.warn(notFoundMsg);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", notFoundMsg));
        }

        if (acceptHeader.contains(MediaType.APPLICATION_PDF_VALUE)) {
            log.info("Generating PDF invoice for {}", label);
            InvoiceResponseDTO structuredInvoice = InvoiceParserUtil.parseInvoiceText(invoiceText);
            byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtmlTemplate(structuredInvoice);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                    .body(pdfBytes);
        } else if (acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
            log.info("Returning JSON invoice for {}", label);
            InvoiceResponseDTO structuredInvoice = InvoiceParserUtil.parseInvoiceText(invoiceText);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(structuredInvoice);
        } else {
            log.info("Returning plain text invoice for {}", label);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(invoiceText);
        }
    }
}