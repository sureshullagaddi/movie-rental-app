
package com.etraveligroup.movie.rental.controller;

import com.etraveligroup.movie.rental.dto.GenerateInvoiceByNameRequestDTO;
import com.etraveligroup.movie.rental.dto.InvoiceResponseDTO;
import com.etraveligroup.movie.rental.service.RentalInfoService;
import com.etraveligroup.movie.rental.service.impl.PdfGenerateService;
import com.etraveligroup.movie.rental.util.InvoiceParserUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalControllerTest {

    @Mock
    RentalInfoService rentalInfoService;

    @Mock
    PdfGenerateService pdfGenerateService;

    @InjectMocks
    RentalController rentalController;

    @Test
    void generateInvoice_returnsPdfResponse() {
        GenerateInvoiceByNameRequestDTO request = mock(GenerateInvoiceByNameRequestDTO.class);
        String invoiceText = "invoice text";
        InvoiceResponseDTO invoiceDTO = mock(InvoiceResponseDTO.class);
        byte[] pdfBytes = new byte[]{1, 2, 3};

        when(rentalInfoService.generateInvoiceByName(request)).thenReturn(Mono.just(invoiceText));
        try (MockedStatic<InvoiceParserUtil> utilMock = mockStatic(InvoiceParserUtil.class)) {
            utilMock.when(() -> InvoiceParserUtil.parseInvoiceText(invoiceText)).thenReturn(invoiceDTO);
            when(pdfGenerateService.generatePdfFromHtmlTemplate(invoiceDTO)).thenReturn(pdfBytes);

            ResponseEntity<Object> response = rentalController
                    .generateInvoice(request, MediaType.APPLICATION_PDF_VALUE)
                    .block();

            assertNotNull(response);
            assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
            assertArrayEquals(pdfBytes, (byte[]) response.getBody());
        }
    }

    @Test
    void generateInvoice_returnsJsonResponse() {
        GenerateInvoiceByNameRequestDTO request = mock(GenerateInvoiceByNameRequestDTO.class);
        String invoiceText = "invoice text";
        InvoiceResponseDTO invoiceDTO = mock(InvoiceResponseDTO.class);

        when(rentalInfoService.generateInvoiceByName(request)).thenReturn(Mono.just(invoiceText));
        try (MockedStatic<InvoiceParserUtil> utilMock = mockStatic(InvoiceParserUtil.class)) {
            utilMock.when(() -> InvoiceParserUtil.parseInvoiceText(invoiceText)).thenReturn(invoiceDTO);

            ResponseEntity<Object> response = rentalController
                    .generateInvoice(request, MediaType.APPLICATION_JSON_VALUE)
                    .block();

            assertNotNull(response);
            assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
            assertEquals(invoiceDTO, response.getBody());
        }
    }

    @Test
    void generateInvoice_returnsTextResponse() {
        GenerateInvoiceByNameRequestDTO request = mock(GenerateInvoiceByNameRequestDTO.class);
        String invoiceText = "plain text invoice";

        when(rentalInfoService.generateInvoiceByName(request)).thenReturn(Mono.just(invoiceText));

        ResponseEntity<Object> response = rentalController
                .generateInvoice(request, MediaType.TEXT_PLAIN_VALUE)
                .block();

        assertNotNull(response);
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals(invoiceText, response.getBody());
    }

    @Test
    void generateInvoiceById_returnsPdfResponse() {
        Long customerId = 1L;
        String invoiceText = "invoice text";
        InvoiceResponseDTO invoiceDTO = mock(InvoiceResponseDTO.class);
        byte[] pdfBytes = new byte[]{1, 2, 3};

        when(rentalInfoService.generateInvoiceById(customerId)).thenReturn(Mono.just(invoiceText));
        try (MockedStatic<InvoiceParserUtil> utilMock = mockStatic(InvoiceParserUtil.class)) {
            utilMock.when(() -> InvoiceParserUtil.parseInvoiceText(invoiceText)).thenReturn(invoiceDTO);
            when(pdfGenerateService.generatePdfFromHtmlTemplate(invoiceDTO)).thenReturn(pdfBytes);

            ResponseEntity<Object> response = rentalController
                    .generateInvoiceById(customerId, MediaType.APPLICATION_PDF_VALUE)
                    .block();

            assertNotNull(response);
            assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
            assertArrayEquals(pdfBytes, (byte[]) response.getBody());
        }
    }

    @Test
    void generateInvoiceById_returnsJsonResponse() {
        Long customerId = 1L;
        String invoiceText = "invoice text";
        InvoiceResponseDTO invoiceDTO = mock(InvoiceResponseDTO.class);

        when(rentalInfoService.generateInvoiceById(customerId)).thenReturn(Mono.just(invoiceText));
        try (MockedStatic<InvoiceParserUtil> utilMock = mockStatic(InvoiceParserUtil.class)) {
            utilMock.when(() -> InvoiceParserUtil.parseInvoiceText(invoiceText)).thenReturn(invoiceDTO);

            ResponseEntity<Object> response = rentalController
                    .generateInvoiceById(customerId, MediaType.APPLICATION_JSON_VALUE)
                    .block();

            assertNotNull(response);
            assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
            assertEquals(invoiceDTO, response.getBody());
        }
    }

    @Test
    void generateInvoiceById_returnsTextResponse() {
        Long customerId = 1L;
        String invoiceText = "plain text invoice";

        when(rentalInfoService.generateInvoiceById(customerId)).thenReturn(Mono.just(invoiceText));

        ResponseEntity<Object> response = rentalController
                .generateInvoiceById(customerId, MediaType.TEXT_PLAIN_VALUE)
                .block();

        assertNotNull(response);
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals(invoiceText, response.getBody());
    }

}