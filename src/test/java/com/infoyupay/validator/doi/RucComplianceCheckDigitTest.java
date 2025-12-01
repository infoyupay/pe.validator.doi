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
 * Compliance tests ensuring that the RUC modulus-11 algorithm matches
 * SUNAT's official specification across known valid examples.
 *
 * @author David Vidal
 * @version 1.0
 */
public class RucComplianceCheckDigitTest {

    /**
     * Validates that the well-known RUC for InfoYupay passes check digit verification.
     */
    @Test
    @DisplayName("computeCheckDigit(): official InfoYupay example must compute '7'")
    void testOfficialExample() {
        assertThat(RUCUtils.computeCheckDigit("20607854247")).isEqualTo('7');
    }

    /**
     * Ensures that lenient mode properly sanitizes before computing.
     */
    @Test
    @DisplayName("computeCheckDigit(strict=false): sanitization before validation")
    void testLenientCheckDigit() {
        assertThat(RUCUtils.computeCheckDigit("20-60785424-7", false)).isEqualTo('7');
    }
}
