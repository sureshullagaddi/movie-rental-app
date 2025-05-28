package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.dto.InvoiceItemDTO;
import com.etraveligroup.movie.rental.dto.InvoiceResponseDTO;
import com.etraveligroup.movie.rental.exceptions.InvoiceParsingException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.etraveligroup.movie.rental.constants.MovieRentalConstants.*;

/**
 * Utility class for parsing invoice text into an InvoiceResponse object.
 * This class is designed to be stateless and should not be instantiated.
 */
@Slf4j
public final class InvoiceParserUtil {

    private static final int MIN_INVOICE_LINES = 3;

    private InvoiceParserUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Parses the given invoice text and returns an InvoiceResponseDTO object.
     *
     * @param invoiceText the text of the invoice to parse
     * @return an InvoiceResponseDTO containing the parsed invoice details
     * @throws InvoiceParsingException if the invoice text is null, blank, or malformed
     */
    public static InvoiceResponseDTO parseInvoiceText(String invoiceText) {
        log.debug("Parsing invoice text: {}", invoiceText);
        if (invoiceText == null || invoiceText.isBlank()) {
            log.error("Invoice text is null or blank");
            throw new InvoiceParsingException("Invoice text cannot be null or blank");
        }

        String[] lines = invoiceText.split("\n");
        if (lines.length < MIN_INVOICE_LINES) {
            log.error("Invoice text is incomplete or malformed: less than {} lines", MIN_INVOICE_LINES);
            throw new InvoiceParsingException("Invoice text is incomplete or malformed");
        }

        try {
            String customerName = extractCustomerName(lines[0]);
            log.debug("Extracted customer name: {}", customerName);
            List<InvoiceItemDTO> items = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            int frequentPoints = 0;

            for (String line : lines) {
                if (line.startsWith("\t")) {
                    log.debug("Parsing invoice item line: {}", line);
                    items.add(parseInvoiceItem(line));
                } else if (line.startsWith(PREFIX_TOTAL)) {
                    log.debug("Parsing total amount line: {}", line);
                    totalAmount = parseBigDecimal(line.replace(PREFIX_TOTAL, "").trim(), "total amount");
                } else if (line.startsWith(PREFIX_POINTS)) {
                    log.debug("Parsing frequent points line: {}", line);
                    frequentPoints = parseFrequentPoints(line);
                }
            }

            log.info("Parsed invoice for customer: {}, items: {}, total: {}, points: {}", customerName, items.size(), totalAmount, frequentPoints);
            return new InvoiceResponseDTO(customerName, items, totalAmount, frequentPoints);

        } catch (InvoiceParsingException e) {
            log.error("Invoice parsing exception: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while parsing invoice", e);
            throw new InvoiceParsingException("Unexpected error while parsing invoice", e);
        }
    }

    private static String extractCustomerName(String headerLine) {
        log.debug("Extracting customer name from header: {}", headerLine);
        if (!headerLine.startsWith(PREFIX_CUSTOMER)) {
            log.error("Missing customer header in invoice: {}", headerLine);
            throw new InvoiceParsingException("Missing customer header in invoice");
        }
        return headerLine.replace(PREFIX_CUSTOMER, "").trim();
    }

    private static InvoiceItemDTO parseInvoiceItem(String line) {
        log.debug("Parsing invoice item: {}", line);
        String[] parts = line.trim().split("\t");
        if (parts.length != 2) {
            log.error("Invalid item line format: {}", line);
            throw new InvoiceParsingException("Invalid item line format: " + line);
        }
        BigDecimal price = parseBigDecimal(parts[1], "item price");
        return new InvoiceItemDTO(parts[0], price);
    }

    private static BigDecimal parseBigDecimal(String value, String fieldName) {
        log.debug("Parsing BigDecimal for {}: {}", fieldName, value);
        try {
            return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            log.error("Failed to parse {}: {}", fieldName, value);
            throw new InvoiceParsingException("Failed to parse " + fieldName + ": " + value, e);
        }
    }

    private static int parseFrequentPoints(String line) {
        log.debug("Parsing frequent points from line: {}", line);
        String digits = line.replaceAll("\\D+", "");
        if (digits.isEmpty()) {
            log.error("Frequent points value missing in line: {}", line);
            throw new InvoiceParsingException("Frequent points value missing in line: " + line);
        }
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            log.error("Failed to parse frequent points: {}", digits);
            throw new InvoiceParsingException("Failed to parse frequent points: " + digits, e);
        }
    }
}