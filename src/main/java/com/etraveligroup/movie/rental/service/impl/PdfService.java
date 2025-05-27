package com.etraveligroup.movie.rental.service.impl;

import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    /**
     * Generates a PDF document from the provided text.
     * @param text the text to be included in the PDF
     * @return a byte array containing the PDF document
     */
    public byte[] generatePdfFromText(String text) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            document.add(new Paragraph(text));
            document.close();
            return out.toByteArray();
        } catch (IOException | java.io.IOException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
