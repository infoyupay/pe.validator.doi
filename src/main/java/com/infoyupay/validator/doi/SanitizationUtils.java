/*
 * pe.validator.doi
 * COPYLEFT 2025
 * Ingenieria Informatica Yupay SACS
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 *  with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.infoyupay.validator.doi;

/**
 * Utility class providing sanitization helpers for identity document values.<br>
 * <br>
 * These methods remove all characters that are not considered valid for a specific
 * category (alphanumeric or digits). The resulting sanitized value preserves only
 * the allowed characters and, when applicable, returns the last <em>N</em> characters
 * according to Peruvian regulatory specifications (e.g., SUNAT, PLAME, PLE, FV3800),
 * which often require keeping the rightmost part of the value when the input exceeds
 * the defined maximum length.<br>
 * <br>
 * All methods are {@code null}-safe: a {@code null} input is treated as an empty
 * string. No normalization or case transformation is performed; sanitization only
 * filters invalid characters and applies optional truncation.<br>
 * <br>
 * This class is a pure utility holder and is not intended to be instantiated or extended.
 *
 * @author David Vidal
 * @version 1.0
 */
public final class SanitizationUtils {

    /**
     * Private constructor to prevent instantiation.<br>
     * This class contains only static utility methods and should not be instantiated.
     */
    private SanitizationUtils() {
        // utility class
    }

    /**
     * Sanitizes the provided value by keeping only Unicode alphanumeric characters
     * (letters and digits) as defined by {@link Character#isLetterOrDigit(char)}.<br>
     * <br>
     * The resulting string is optionally truncated from the left, preserving only the
     * last {@code maxLength} characters when {@code maxLength} is greater than zero.
     * This behavior is consistent with Peruvian regulatory data formats where exceeding
     * values must retain the rightmost segment (e.g., SUNAT field constraints).<br>
     * <br>
     * If {@code rawNumber} is {@code null}, an empty string is returned.
     *
     * @param rawNumber the original value to sanitize; may be {@code null}
     * @param maxLength the maximum allowed length, or a non-positive value to disable truncation
     * @return the sanitized alphanumeric string; never {@code null}
     */
    public static String alnum(String rawNumber, int maxLength) {
        if (rawNumber == null) {
            return "";
        }

        var builder = new StringBuilder(rawNumber.length());

        for (int i = 0; i < rawNumber.length(); i++) {
            char c = rawNumber.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                builder.append(c);
            }
        }

        int length = builder.length();
        return maxLength > 0 && length > maxLength
                ? builder.substring(length - maxLength)
                : builder.toString();
    }

    /**
     * Sanitizes the provided value by keeping only Unicode decimal digits
     * as defined by {@link Character#isDigit(char)}.<br>
     * <br>
     * The resulting string is optionally truncated from the left, preserving only the
     * last {@code maxLength} characters when {@code maxLength} is greater than zero.
     * This matches the behavior expected in scenarios where numeric identifiers must
     * retain their rightmost digits when exceeding allowed lengths.<br>
     * <br>
     * If {@code rawNumber} is {@code null}, an empty string is returned.
     *
     * @param rawNumber the original value to sanitize; may be {@code null}
     * @param maxLength the maximum allowed length, or a non-positive value to disable truncation
     * @return the sanitized numeric string; never {@code null}
     */
    public static String digits(String rawNumber, int maxLength) {
        if (rawNumber == null) {
            return "";
        }

        var builder = new StringBuilder(rawNumber.length());

        for (int i = 0; i < rawNumber.length(); i++) {
            char c = rawNumber.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }

        int length = builder.length();
        return maxLength > 0 && length > maxLength
                ? builder.substring(length - maxLength)
                : builder.toString();
    }
}

