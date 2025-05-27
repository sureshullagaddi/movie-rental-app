package com.etraveligroup.movie.rental.service.impl;

import com.etraveligroup.movie.rental.dto.InvoiceResponseDTO;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

/**
 * Service for generating PDF documents.
 * This service uses iText library to create PDF files from text input.
 */
@Service
public class PdfGenerateService {


    private final TemplateEngine templateEngine;

    public PdfGenerateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    /**
     * Generates a PDF document from the provided text.
     *
     * @param invoice the text to be included in the PDF
     * @return a byte array containing the PDF document
     */
    public byte[] generatePdfFromHtmlTemplate(InvoiceResponseDTO invoice) {
        Context context = new Context();
        context.setVariable("invoice", invoice);
        String html = templateEngine.process("invoice-template", context);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF from HTML", e);
        }
    }
}
