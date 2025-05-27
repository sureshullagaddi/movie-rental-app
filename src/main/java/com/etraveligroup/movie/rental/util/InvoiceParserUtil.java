package com.etraveligroup.movie.rental.util;

import com.etraveligroup.movie.rental.dto.InvoiceItemDTO;
import com.etraveligroup.movie.rental.dto.InvoiceResponseDTO;
import com.etraveligroup.movie.rental.exceptions.InvoiceParsingException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.etraveligroup.movie.rental.constants.MovieRentalConstants.*;

/**
 * Utility class for parsing invoice text into an InvoiceResponse object.
 * This class is designed to be stateless and should not be instantiated.
 */
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
        if (invoiceText == null || invoiceText.isBlank()) {
            throw new InvoiceParsingException("Invoice text cannot be null or blank");
        }

        String[] lines = invoiceText.split("\n");
        if (lines.length < MIN_INVOICE_LINES) {
            throw new InvoiceParsingException("Invoice text is incomplete or malformed");
        }

        try {
            String customerName = extractCustomerName(lines[0]);
            List<InvoiceItemDTO> items = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            int frequentPoints = 0;

            for (String line : lines) {
                if (line.startsWith("\t")) {
                    items.add(parseInvoiceItem(line));
                } else if (line.startsWith(PREFIX_TOTAL)) {
                    totalAmount = parseBigDecimal(line.replace(PREFIX_TOTAL, "").trim(), "total amount");
                } else if (line.startsWith(PREFIX_POINTS)) {
                    frequentPoints = parseFrequentPoints(line);
                }
            }

            return new InvoiceResponseDTO(customerName, items, totalAmount, frequentPoints);

        } catch (InvoiceParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new InvoiceParsingException("Unexpected error while parsing invoice", e);
        }
    }

    private static String extractCustomerName(String headerLine) {
        if (!headerLine.startsWith(PREFIX_CUSTOMER)) {
            throw new InvoiceParsingException("Missing customer header in invoice");
        }
        return headerLine.replace(PREFIX_CUSTOMER, "").trim();
    }

    private static InvoiceItemDTO parseInvoiceItem(String line) {
        String[] parts = line.trim().split("\t");
        if (parts.length != 2) {
            throw new InvoiceParsingException("Invalid item line format: " + line);
        }
        BigDecimal price = parseBigDecimal(parts[1], "item price");
        return new InvoiceItemDTO(parts[0], price);
    }

    private static BigDecimal parseBigDecimal(String value, String fieldName) {
        try {
            return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new InvoiceParsingException("Failed to parse " + fieldName + ": " + value, e);
        }
    }

    private static int parseFrequentPoints(String line) {
        String digits = line.replaceAll("\\D+", "");
        if (digits.isEmpty()) {
            throw new InvoiceParsingException("Frequent points value missing in line: " + line);
        }
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            throw new InvoiceParsingException("Failed to parse frequent points: " + digits, e);
        }
    }
}