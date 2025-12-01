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

import java.util.Objects;

/**
 * Utility class for computing and validating Peruvian RUC (Registro Único de Contribuyente) numbers.<br>
 * <br>
 * A RUC consists of 11 digits and contains:<br>
 * - A two-digit prefix indicating the taxpayer category.<br>
 * - An eight-digit identity component.<br>
 * - A check digit computed using SUNAT’s modulo-11 algorithm.<br>
 * <br>
 * This class provides helpers for:<br>
 * - Computing the check digit (strict or sanitized).<br>
 * - Validating full RUC numbers.<br>
 * - Handling sanitized versus non-sanitized input according to SUNAT norms.<br>
 * <br>
 * All methods are null-safe where applicable, and sanitization rules follow the same
 * constraints used in Peruvian electronic filing systems (PLE, PLAME, AFPNet, FV3800).<br>
 *
 * @author David Vidal
 * @version 1.0
 */
public final class RUCUtils {

    /**
     * Private constructor to prevent instantiation.<br>
     * This class only exposes static utility methods.
     */
    private RUCUtils() {
        // utility class
    }

    /**
     * Computes the RUC check digit using SUNAT's modulo-11 algorithm.<br>
     * <br>
     * If {@code strict} is {@code true}, the raw value is used as provided.<br>
     * If {@code strict} is {@code false}, the value is sanitized through {@link DoiType#RUC}
     * before computing the digit.<br>
     * <br>
     * A valid string for computing the digit must contain at least the first 10 digits of the RUC.
     *
     * @param raw    the raw RUC string
     * @param strict whether sanitization should be disabled
     * @return the computed check digit as a character
     */
    public static char computeCheckDigit(String raw, boolean strict) {
        String value = strict ? raw : DoiType.RUC.sanitize(raw);
        return computeCheckDigit(value);
    }

    /**
     * Computes the check digit of a Peruvian RUC using SUNAT's positional weights:<br>
     * 5, 4, 3, 2, 7, 6, 5, 4, 3, 2<br>
     * <br>
     * The provided string must contain at least 10 digits (positions 0–9). If the
     * string has 11 digits, the last digit is ignored during computation.<br>
     * <br>
     * Rules:<br>
     * - If result = 11 → check digit = '1'<br>
     * - If result = 10 → check digit = '0'<br>
     * - Otherwise → check digit = (result)<br>
     *
     * @param s the numeric RUC string (strict mode)
     * @return the computed check digit as a character
     */
    public static char computeCheckDigit(String s) {
        Objects.requireNonNull(s, "Cannot compute check digit using null RUC.");

        if (s.length() < 10) {
            throw new IllegalArgumentException("To compute a RUC check digit, at least 10 digits are required.");
        }
        if (s.length() > 11) {
            throw new IllegalArgumentException("RUC length cannot exceed 11 digits.");
        }

        int sum = 0;

        // SUNAT positional weights: 5,4,3,2,7,6,5,4,3,2
        for (int i = 0, x = 6; i < 10; i++) {
            if (i == 4) x = 8;
            char c = s.charAt(i);
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException(
                        String.format("Invalid RUC character '%c' at index %d", c, i)
                );
            }
            sum += (c - '0') * --x;
        }

        int residue = sum % 11;
        int check = 11 - residue;

        if (check == 11) return '1';
        if (check == 10) return '0';
        return (char) (check + '0');
    }

    /**
     * Validates a full 11-digit RUC in strict mode (no sanitization).<br>
     * <br>
     * Validation rules:<br>
     * - Must contain exactly 11 digits.<br>
     * - Prefix must be one of: 10, 15, 16, 17, 20.<br>
     * - Check digit must match SUNAT’s modulo-11 computation.<br>
     *
     * @param ruc the RUC string to validate
     * @return {@code true} if valid, {@code false} otherwise
     */
    public static boolean isRUCValid(String ruc) {
        if (ruc == null || ruc.length() != 11) return false;
        if (!ruc.chars().allMatch(Character::isDigit)) return false;

        int prefix = Integer.parseInt(ruc.substring(0, 2));
        if (!isValidPrefix(prefix)) return false;

        return computeCheckDigit(ruc) == ruc.charAt(10);
    }

    /**
     * Validates a RUC using strict or sanitized mode.<br>
     * <br>
     * - If {@code strict} is {@code true}, validation uses the raw value.<br>
     * - If {@code strict} is {@code false}, the value is sanitized through {@link DoiType#RUC}.<br>
     * <br>
     * If the value is null or blank, the method always returns {@code false}.
     *
     * @param ruc    the RUC string to validate
     * @param strict whether sanitization should be disabled
     * @return {@code true} if the RUC is valid under the selected mode
     */
    public static boolean isRUCValid(String ruc, boolean strict) {
        if (ruc == null || ruc.isBlank()) return false;
        String value = strict ? ruc : DoiType.RUC.sanitize(ruc);
        return isRUCValid(value);
    }

    /**
     * Valid prefix values according to SUNAT:<br>
     * - 10 → Natural person<br>
     * - 15 → Succession / marital society / military & police / foreigners<br>
     * - 16 → Valid but reserved (undocumented)<br>
     * - 17 → Natural persons registered 1993–2000<br>
     * - 20 → Legal entities and consortiums<br>
     *
     * @param prefix two-digit numeric prefix
     * @return {@code true} if prefix is valid
     */
    private static boolean isValidPrefix(int prefix) {
        return prefix == 10 || prefix == 15 || prefix == 16
                || prefix == 17 || prefix == 20;
    }
}
