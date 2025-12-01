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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DNI compliance tests ensuring strict and lenient validation matches
 * SUNAT structural rules.
 *
 * @author David Vidal
 * @version 1.0
 */
public class DniComplianceTest {

    /**
     * Valid DNI numbers under strict SUNAT rules.
     *
     * @param dni tested dni number.
     */
    @ParameterizedTest
    @ValueSource(strings = {"12345678", "87654321"})
    @DisplayName("DNI strict validation must accept 8-digit numeric strings")
    void testValidDniStrict(String dni) {
        assertThat(DoiType.DNI.validateNumber(dni, true)).isTrue();
    }

    /**
     * Invalid DNI examples under strict rules.
     *
     * @param dni tested dni number.
     */
    @ParameterizedTest
    @ValueSource(strings = {"1234567", "123456789", "A2345678", "", " "})
    @DisplayName("DNI strict validation must reject malformed numbers")
    void testInvalidDniStrict(String dni) {
        assertThat(DoiType.DNI.validateNumber(dni, true)).isFalse();
    }
}
