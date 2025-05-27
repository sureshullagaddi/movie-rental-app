package com.etraveligroup.movie.rental.controller;

import com.etraveligroup.movie.rental.dto.GenerateInvoiceByNameRequestDTO;
import com.etraveligroup.movie.rental.dto.InvoiceResponseDTO;
import com.etraveligroup.movie.rental.service.RentalInfoService;
import com.etraveligroup.movie.rental.service.impl.PdfGenerateService;
import com.etraveligroup.movie.rental.util.InvoiceParserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/generate")
@RequiredArgsConstructor
@Tag(name = "Invoice API", description = "Generate Invoices for Movie Rentals")
public class RentalController {

    private final RentalInfoService rentalInfoService;
    private final PdfGenerateService pdfGeneratorService;

    /**
     * Generates an invoice based on the customer's name.
     * The response format is determined by the 'Accept' header:
     * If 'Accept' contains 'application/pdf', a PDF file is generated.
     * If 'Accept' contains 'application/json', a structured JSON response is returned.
     * If 'Accept' contains 'text/plain', a plain text invoice is returned.
     *
     * @param generateInvoiceByNameDTO the request containing customer name or ID
     * @param acceptHeader             the 'Accept' header indicating the desired response format
     * @return a Mono containing the ResponseEntity with the generated invoice
     * @throws IllegalArgumentException if the request is invalid or the customer is not found
     * @author Suresh
     * @version 1.0
     */
    @PostMapping(value = "/invoice", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Generates invoice by customer name")
    public Mono<ResponseEntity<Object>> generateInvoice(@Valid @RequestBody GenerateInvoiceByNameRequestDTO generateInvoiceByNameDTO,
                                                        @RequestHeader("Accept") String acceptHeader) {

        return rentalInfoService.generateInvoiceByName(generateInvoiceByNameDTO)
                .map(invoiceText -> {
                    if (invoiceText == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("error", "Invoice not found for customer: " + generateInvoiceByNameDTO.customerName()));
                    }
                    if (acceptHeader.contains(MediaType.APPLICATION_PDF_VALUE)) {
                        InvoiceResponseDTO structuredInvoice = InvoiceParserUtil.parseInvoiceText(invoiceText);
                        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtmlTemplate(structuredInvoice);
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf").body(pdfBytes);
                    } else if (acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
                        InvoiceResponseDTO structuredInvoice = InvoiceParserUtil.parseInvoiceText(invoiceText);
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(structuredInvoice);
                    } else {
                        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(invoiceText);
                    }
                });
    }

    /**
     * Generates an invoice based on the customer's ID.
     * The response format is determined by the 'Accept' header:
     * If 'Accept' contains 'application/pdf', a PDF file is generated.
     * If 'Accept' contains 'application/json', a structured JSON response is returned.
     * If 'Accept' contains 'text/plain', a plain text invoice is returned.
     * * @param customerId the ID of the customer for whom the invoice is to be generated
     * * @param acceptHeader the 'Accept' header indicating the desired response format
     * * @return a Mono containing the ResponseEntity with the generated invoice
     * * @throws IllegalArgumentException if the request is invalid or the customer is not found
     * * @author Suresh
     * * @version 1.0
     */
    @GetMapping(value = "/invoice/by-id/{customerId}", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Generates invoice by customer ID")
    public Mono<ResponseEntity<Object>> generateInvoiceById(
            @PathVariable Long customerId,
            @RequestHeader("Accept") String acceptHeader) {

        return rentalInfoService.generateInvoiceById(customerId)
                .map(invoiceText -> {
                    if (invoiceText == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("error", "Invoice not found for customer ID: " + customerId));
                    }
                    if (acceptHeader.contains(MediaType.APPLICATION_PDF_VALUE)) {
                        InvoiceResponseDTO structuredInvoice = InvoiceParserUtil.parseInvoiceText(invoiceText);
                        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtmlTemplate(structuredInvoice);
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf").body(pdfBytes);
                    } else if (acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
                        InvoiceResponseDTO structuredInvoice = InvoiceParserUtil.parseInvoiceText(invoiceText);
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(structuredInvoice);
                    } else {
                        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(invoiceText);
                    }
                });
    }
}