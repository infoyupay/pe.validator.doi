/**
 * Primary module definition for <strong>pe.validator.doi</strong>.
 * <br>
 * This module provides a complete, dependency-free toolkit for validating
 * Peruvian identity document types (DOI) and their structural formats.
 *
 * <br><br>
 * Core features include:
 * <ul>
 *     <li>Validation of all DOI types recognized by SUNAT and related systems,
 *         such as DNI, RUC, CE, Passport, Refugee ID, Diplomatic ID, PTP, TIN, and others.</li>
 *     <li>Syntactic and structural verification, including support for
 *         <em>strict</em> and <em>sanitized</em> validation modes.</li>
 *     <li>RUC check-digit computation based on the official modulus-11 algorithm.</li>
 *     <li>Built-in sanitization utilities to normalize numeric or alphanumeric
 *         identifiers according to SUNAT ecosystem constraints.</li>
 *     <li>Contextual suitability checks for PLE, PLAME, AFPNet, and FV-3800 use cases.</li>
 * </ul>
 *
 * <br>
 * This module is intentionally minimalistic:
 * <ul>
 *     <li>No network calls are performed.</li>
 *     <li>No SUNAT, RENIEC, or external service is queried.</li>
 *     <li>No external dependencies are required.</li>
 * </ul>
 *
 * <br>
 * As part of the <strong>InfoYupay</strong> ecosystem, this library is designed for use in:
 * <ul>
 *     <li>Backend and asynchronous service layers.</li>
 *     <li>Desktop applications (JavaFX, Swing, etc.).</li>
 *     <li>Data processing pipelines and ETL systems.</li>
 *     <li>Interoperation with the broader Peruvian electronic reporting stack.</li>
 * </ul>
 *
 * <br>
 * The focus of this module is exclusively on the **structure** of DOI values:
 * it determines whether a number is valid according to official formatting rules,
 * but does <em>not</em> determine whether the identifier actually exists in
 * a governmental database.
 *
 * @author David Vidal
 * @since 1.0.0
 */
module pe.validator.doi {
    exports com.infoyupay.validator.doi;
}
