package com.etraveligroup.movie.rental.service.impl;

import com.etraveligroup.movie.rental.dto.InvoiceResponseDTO;
import com.etraveligroup.movie.rental.exceptions.PdfGenerationException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

/**
 * Service for generating PDF documents.
 * This service uses iText library to create PDF files from text input.
 */
@Slf4j
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
        log.info("Starting PDF generation for invoice: {}", invoice != null ? invoice.customer() : "null");
        Context context = new Context();
        context.setVariable("invoice", invoice);
        String html = templateEngine.process("invoice-template", context);
        log.debug("Generated HTML for invoice: {}", html);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
            log.info("PDF generation successful for invoice: {}", invoice != null ? invoice.customer() : "null");
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate PDF from HTML for invoice: {}", invoice != null ? invoice.customer() : "null", e);
            throw new PdfGenerationException("Failed to generate PDF from HTML", e);
        }
    }
}