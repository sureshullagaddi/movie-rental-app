package com.etraveligroup.movie.rental.service.impl;

import com.etraveligroup.movie.rental.dto.InvoiceResponseDTO;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfGenerateServiceTest {

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private PdfGenerateService pdfGenerateService;

    @Test
    void generatePdfFromHtmlTemplate_successful() {
        InvoiceResponseDTO invoice = mock(InvoiceResponseDTO.class);
        String html = "<html><body>Test Invoice</body></html>";
        when(templateEngine.process(eq("invoice-template"), any(Context.class))).thenReturn(html);

        byte[] pdfBytes = pdfGenerateService.generatePdfFromHtmlTemplate(invoice);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        verify(templateEngine).process(eq("invoice-template"), any(Context.class));
    }

    @Test
    void generatePdfFromHtmlTemplate_templateEngineThrowsException() {
        InvoiceResponseDTO invoice = mock(InvoiceResponseDTO.class);
        when(templateEngine.process(eq("invoice-template"), any(Context.class)))
                .thenThrow(new RuntimeException("Template error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pdfGenerateService.generatePdfFromHtmlTemplate(invoice));
        assertTrue(ex.getMessage().contains("Template error"));
    }

    @Test
    void generatePdfFromHtmlTemplate_pdfRendererThrowsException() {
        InvoiceResponseDTO invoice = mock(InvoiceResponseDTO.class);
        // Simulate invalid HTML to cause PdfRendererBuilder to throw
        when(templateEngine.process(eq("invoice-template"), any(Context.class)))
                .thenReturn("<html><body><invalid></body></html>");

        // Use a spy to simulate exception on PdfRendererBuilder.run()
        try (MockedConstruction<PdfRendererBuilder> mocked = mockConstruction(PdfRendererBuilder.class,
                (mock, context) -> doThrow(new RuntimeException("PDF error")).when(mock).run())) {

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> pdfGenerateService.generatePdfFromHtmlTemplate(invoice));
            assertTrue(ex.getMessage().contains("Failed to generate PDF from HTML"));
        }
    }
}