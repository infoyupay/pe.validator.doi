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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Usage context for DOI validation and formatting across
 * SUNAT-related electronic systems.<br>
 * <br>
 * These contexts determine which DOI types are allowed and
 * which official document codes must be used when exporting
 * records to PLAME, PLE, AFPNet or FV3800.
 *
 * @author David Vidal
 * @version 1.0
 */
public enum UsageContext {

    /**
     * Programa de Libros Electr&oacute;nicos (PLE).<br>
     * <br>
     * PLE is SUNAT's unified platform for electronic bookkeeping and regulatory
     * records, covering books and annexes such as:<br>
     * - Sales and Purchases ledgers<br>
     * - General Ledger and Journal<br>
     * - Cash and Banks<br>
     * - Fixed Assets<br>
     * - Inventory and Permanent Stock (Kardex)<br>
     * - Cost records<br>
     * - and other tax-related registers<br>
     * <br>
     * The structural rules for identity documents (DOI) are defined once under
     * “Validaciones Generales” and apply uniformly across all books. These rules
     * include allowed DOI types, length constraints and basic format checks. The
     * {@link DoiType} enum implements these structural validations.<br>
     * <br>
     * What varies by book or annex is the *contextual admissibility* of each DOI
     * type. For example, the issuer in the Sales ledger must always be a RUC,
     * while other participants may use alternative DOI types where permitted.
     */
    PLE,

    /**
     * Payroll and employment reporting system (Planilla Electr&oacute;nica).
     */
    PLAME,

    /**
     * Pension fund contribution system.
     */
    AFP_NET,

    /**
     * Formato Virtual 3800 (FV 3800) – Declaraci&oacute;n de Beneficiario Final.<br>
     * <br>
     * SUNAT’s filing format used to disclose the chain of ownership and control
     * of an entity, identifying both the direct and indirect beneficial owners.<br>
     * <br>
     * DOI requirements follow the same identity document catalog defined in
     * SUNAT’s “Validaciones Generales”. Specific DOI types may be mandatory
     * depending on the participant’s role (e.g., legal entity vs. natural person).
     * The {@link DoiType} enum provides the structural validation for these values.
     */
    FV_3800;

    /**
     * Returns the list of DOI types that are suitable for this specific
     * SUNAT reporting context.<br>
     * <br>
     * This method provides an ergonomic, context-centric API for determining
     * which identification document types are accepted in a given subsystem
     * (PLE, PLAME, AFPNet or FV-3800). The suitability of each DOI type is
     * determined by the subsystem-specific identifiers defined in
     * {@link DoiType}.<br>
     * <br>
     * Defined in {@code UsageContext} for improved API readability and
     * developer ergonomics. Since {@code UsageContext} is an enum, this
     * method is naturally null-safe and avoids the need for defensive
     * null checks.
     *
     * @return a list containing all DOI types that are valid for this usage
     * context
     */
    public List<DoiType> listSuitableDoi() {
        return Arrays.stream(DoiType.values())
                .filter(dt -> dt.isSuitableFor(this))
                .collect(Collectors.toList());
    }
}

