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
/**
 * Provides core utilities for validating Peruvian identification document
 * types (DOI) in accordance with SUNAT's structural and subsystem-specific
 * requirements.<br>
 * <br>
 * This package defines the canonical enumeration of DOI types used across
 * major SUNAT reporting platforms, including:
 * <ul>
 *  <li><strong>PLE</strong>: Electronic accounting ledgers</li>
 *  <li><strong>PLAME</strong>: Payroll and employment reporting</li>
 *  <li><strong>AFPNet</strong>: Pension fund contribution filings</li>
 *  <li><strong>FV-3800</strong>: Beneficial ownership declaration (Beneficiario Final)</li>
 * </ul>
 * The validation model includes:<br>
 * <ul>
 *  <li>Strict structural patterns for each DOI type (regex-based)</li>
 *  <li>Context-aware suitability rules for SUNAT subsystems</li>
 *  <li>Sanitization utilities to normalize user input while preserving
 *   traceability</li>
 *  <li>Metadata describing foreign-issued documents and admissibility for
 *   non-domiciled subjects</li>
 *  </ul>
 * <br>
 * This package is intentionally minimalistic and UI-agnostic. Developers may
 * integrate it with JavaFX, Swing, SWT, QtJambi or Android through optional
 * examples available in the {@code snippets/} directory.
 *
 * @author David Vidal
 * @version 1.0
 */
package com.infoyupay.validator.doi;