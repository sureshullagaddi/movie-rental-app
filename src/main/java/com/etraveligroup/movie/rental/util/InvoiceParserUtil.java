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

    private InvoiceParserUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static InvoiceResponseDTO parseInvoiceText(String invoiceText) {
        if (invoiceText == null || invoiceText.isBlank()) {
            throw new InvoiceParsingException("Invoice text cannot be null or blank");
        }

        try {
            String[] lines = invoiceText.split("\n");
            if (lines.length < 3) {
                throw new InvoiceParsingException("Invoice text is incomplete or malformed");
            }

            String customer = lines[0].replace(PREFIX_CUSTOMER, "").trim();
            List<InvoiceItemDTO> items = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;
            int frequentPoints = 0;

            for (String line : lines) {
                if (line.startsWith("\t")) {
                    String[] parts = line.trim().split("\t");
                    if (parts.length != 2) {
                        throw new InvoiceParsingException("Invalid item line format: " + line);
                    }
                    BigDecimal price = new BigDecimal(parts[1]).setScale(2, RoundingMode.HALF_UP);
                    items.add(new InvoiceItemDTO(parts[0], price));
                } else if (line.startsWith(PREFIX_TOTAL)) {
                    total = new BigDecimal(line.replace(PREFIX_TOTAL, "").trim()).setScale(2, RoundingMode.HALF_UP);
                } else if (line.startsWith(PREFIX_POINTS)) {
                    frequentPoints = Integer.parseInt(line.replaceAll("\\D+", ""));
                }
            }

            return new InvoiceResponseDTO(customer, items, total, frequentPoints);

        } catch (NumberFormatException e) {
            throw new InvoiceParsingException("Failed to parse numeric value in invoice", e);
        } catch (Exception e) {
            throw new InvoiceParsingException("Unexpected error while parsing invoice", e);
        }
    }
}
