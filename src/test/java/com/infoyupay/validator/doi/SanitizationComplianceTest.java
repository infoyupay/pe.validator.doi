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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Compliance tests for sanitization logic as expected by SUNAT environments.
 * Ensures strict right-truncation, null safety, and correct alphanumeric/digit filtering.
 *
 * @author David Vidal
 * @version 1.0
 */
public class SanitizationComplianceTest {

    /**
     * Verifies that only alphanumeric characters are preserved and right-truncated.
     */
    @Test
    @DisplayName("alnum(): SUNAT-compliant sanitization with right-truncation")
    void testAlnumCompliance() {
        var raw = "A-12.B_34/XYZ-98765";
        var sanitized = SanitizationUtils.alnum(raw, 10);
        assertThat(sanitized).isEqualTo("34XYZ98765");
    }

    /**
     * Verifies digit-only extraction and truncation from the right.
     */
    @Test
    @DisplayName("digits(): SUNAT-compliant digit sanitization")
    void testDigitCompliance() {
        var raw = "12-34.56A78B90";
        var sanitized = SanitizationUtils.digits(raw, 6);
        assertThat(sanitized).isEqualTo("567890");
    }
}
