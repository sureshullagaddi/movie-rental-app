package com.etraveligroup.movie.rental.controller;

import com.etraveligroup.movie.rental.dto.GenerateInvoiceByIdRequestDTO;
import com.etraveligroup.movie.rental.dto.GenerateInvoiceRequestDTO;
import com.etraveligroup.movie.rental.dto.InvoiceResponseDTO;
import com.etraveligroup.movie.rental.service.RentalInfoService;
import com.etraveligroup.movie.rental.service.impl.PdfService;
import com.etraveligroup.movie.rental.util.InvoiceParserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    private final PdfService pdfGeneratorService;


    @PostMapping(value = "/invoices", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Generates invoice by customer name")
    public Mono<ResponseEntity<Object>> generateInvoice(@Valid @RequestBody GenerateInvoiceRequestDTO requestDTO,
                                                        @RequestHeader("Accept") String acceptHeader) {

        return rentalInfoService.generateInvoiceByName(requestDTO)
                .map(invoiceText -> {
                    if (invoiceText == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("error", "Invoice not found for customer: " + requestDTO.customerName()));
                    }
                    if (acceptHeader.contains(MediaType.APPLICATION_PDF_VALUE)) {
                        byte[] pdfBytes = pdfGeneratorService.generatePdfFromText(invoiceText);
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+requestDTO.customerName()+".pdf")
                                .body(pdfBytes);
                    } else if (acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
                        InvoiceResponseDTO structuredInvoice = InvoiceParserUtil.parseInvoiceText(invoiceText);
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(structuredInvoice);
                    } else {
                        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(invoiceText);
                    }
                });
    }

    @PostMapping(value = "/invoice/by-id", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Generates invoice by customer ID")
    public Mono<ResponseEntity<Object>> generateInvoiceById(
            @Valid @RequestBody GenerateInvoiceByIdRequestDTO requestDTO,
            @RequestHeader("Accept") String acceptHeader) {

        return rentalInfoService.generateInvoiceById(requestDTO.customerId())
                .map(invoiceText -> {
                    if (invoiceText == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("error", "Invoice not found for customer ID: " + requestDTO.customerId()));
                    }
                    if (acceptHeader.contains(MediaType.APPLICATION_PDF_VALUE)) {
                        byte[] pdfBytes = pdfGeneratorService.generatePdfFromText(invoiceText);
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + requestDTO.customerId() + ".pdf")
                                .body(pdfBytes);
                    } else if (acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
                        InvoiceResponseDTO structuredInvoice = InvoiceParserUtil.parseInvoiceText(invoiceText);
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(structuredInvoice);
                    } else {
                        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(invoiceText);
                    }
                });
    }
}