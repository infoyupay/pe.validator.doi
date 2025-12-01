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
 * Enumeration of Peruvian identity document types (DOI) used across
 * SUNAT-related electronic filing systems such as PLAME, PLE, AFPNet
 * and FV3800.<br>
 * <br>
 * Each enum constant defines:<br>
 * - A short code used in internal mappings.<br>
 * - Official SUNAT identifiers for PLAME, PLE, AFPNet and FV3800.<br>
 * - A validation regular expression defining allowed input structure.<br>
 * - A sanitization strategy through {@link #sanitize(String)} that removes
 * invalid characters and applies SUNAT-compliant truncation rules.<br>
 * <br>
 * This enum centralizes DOI semantics to ensure consistent validation and
 * transformation across all Peruvian tax, payroll, and accounting systems.
 *
 * @author David Vidal
 * @version 1.0
 */
public enum DoiType {
    /**
     * Generic identifier type used when the document does not match any official
     * SUNAT-recognized category.<br>
     * <br>
     * Accepted by PLAME (code 0) and usable as a fallback in systems where a
     * non-standard DOI is still permitted. Sanitization keeps alphanumeric
     * characters up to 15 positions.
     */
    OTHERS("OTR", "", "0", "", "", "\\p{Alnum}{1,15}",
            false, false) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.alnum(rawNumber, 15);
        }
    },

    /**
     * Documento Nacional de Identidad (DNI).<br>
     * <br>
     * The primary Peruvian national identification document. Used across all SUNAT
     * electronic systems (PLAME, PLE, AFPNet, FV3800). Always exactly 8 numeric
     * digits. Sanitization keeps digits and truncates to 8 if necessary.
     */
    DNI("DNI", "01", "1", "0", "01", "\\d{8}",
            false, false) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.digits(rawNumber, 8);
        }
    },

    /**
     * Carn&eacute; Militar y Policial (PNP/FF.AA).<br>
     * <br>
     * Alphanumeric identifier used for police and military personnel. Accepted in
     * PLAME and AFPNet. Sanitization keeps alphanumeric characters up to 15.
     */
    PNP("PNP", "02", "0", "2", "", "\\p{Alnum}{1,15}",
            false, false) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.alnum(rawNumber, 15);
        }
    },

    /**
     * Carn&eacute; de Extranjería (CE).<br>
     * <br>
     * Identity document issued to foreign residents in Peru. Accepted by all major
     * SUNAT subsystems except where RUC is mandatory. Alphanumeric, up to 12 characters.
     */
    CE("CEX", "04", "4", "1", "04", "\\p{Alnum}{1,12}",
            true, false) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.alnum(rawNumber, 12);
        }
    },

    /**
     * Registro &Uacute;nico de Contribuyente (RUC).<br>
     * <br>
     * Peruvian taxpayer identification number used in all tax-related operations.
     * Must contain 11 digits: a valid prefix (10,15,16,17,20), nine identity digits,
     * and a check digit computed using SUNAT's modulo-11 algorithm.<br>
     * <br>
     * Sanitization keeps only digits up to 11. Validation uses {@link RUCUtils}.
     */
    RUC("RUC", "06", "6", "", "06", "((10)|(15)|(16)|(17)|(20))\\d{9}",
            false, false) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.digits(rawNumber, 11);
        }

        /**
         * RUC numbers require specialized validation using SUNAT’s
         * modulo-11 check digit. This override retains the default
         * behavior for structural validation and delegates semantic
         * validation to {@link com.infoyupay.validator.doi.RUCUtils}.
         */
        @Override
        public boolean validateNumber(String number, boolean strict) {
            if (number == null || number.isBlank()) return false;
            return RUCUtils.isRUCValid(number, strict);
        }
    },

    /**
     * Passport number (Peruvian or foreign).<br>
     * <br>
     * Accepted in PLAME, PLE, AFPNet and FV3800. Alphanumeric, up to 12 characters.
     * Sanitization removes invalid characters and truncates from the left if needed.
     */
    PASSPORT("PAS", "07", "7", "4", "07", "\\p{Alnum}{1,12}",
            true, false) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.alnum(rawNumber, 12);
        }
    },

    /**
     * Carn&eacute; de Refugiado (REF).<br>
     * <br>
     * Identity document issued to individuals under refugee status. Used in several
     * SUNAT subsystems where alternative identity documents are permitted.
     * Alphanumeric, up to 15 characters.
     */
    REFUGEE("REF", "09", "0", "9", "", "\\p{Alnum}{1,15}",
            true, false) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.alnum(rawNumber, 15);
        }
    },

    /**
     * C&eacute;dula de Identidad Diplom&aacute;tica (CDI).<br>
     * <br>
     * Issued to accredited diplomatic personnel. Accepted by SUNAT systems where
     * non-RUC identifiers are allowed. Alphanumeric, up to 15 characters.
     */
    DIPLOMATIC("CDI", "22", "0", "7", "", "\\p{Alnum}{1,15}",
            true, false) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.alnum(rawNumber, 15);
        }
    },

    /**
     * Permiso Temporal de Permanencia (PTP).<br>
     * <br>
     * Transitional document used for foreign citizens under the temporary residence
     * regularization program. Alphanumeric, up to 15 characters.
     */
    PTP("PTP", "23", "0", "6", "", "\\p{Alnum}{1,15}",
            true, false) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.alnum(rawNumber, 15);
        }
    },

    /**
     * Foreign identification document (ID).<br>
     * <br>
     * Generic identifier used for foreign individuals whose country-issued ID does
     * not fall under CE or PASSPORT categories. Accepted in AFPNet (code 8).
     * Alphanumeric, up to 15 characters.
     */
    ID("ID", "24", "0", "8", "02", "\\p{Alnum}{1,15}",
            true, true) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.alnum(rawNumber, 15);
        }
    },

    /**
     * Carn&eacute; del Permiso Temporal de Permanencia (C. PTP).<br>
     * <br>
     * Variant of the PTP document used in some government registries. Accepted in
     * AFPNet (code 10). Alphanumeric, maximum 15 characters.
     */
    ID_PTP("C. PTP", "26", "0", "10", "", "\\p{Alnum}{1,15}",
            true, false) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.alnum(rawNumber, 15);
        }
    },

    /**
     * Taxpayer Identification Number (TIN) for foreign entities or individuals
     * without a Peruvian RUC.<br>
     * <br>
     * Commonly used in cross-border operations and in SUNAT electronic ledgers
     * where a foreign tax ID must be reported (e.g., PLE type 0, FV3800 01).
     * Alphanumeric, up to 15 characters.
     */
    TIN("TIN", "", "0", "", "01", "\\p{Alnum}{1,15}",
            true, true) {
        @Override
        public String sanitize(String rawNumber) {
            return SanitizationUtils.alnum(rawNumber, 15);
        }
    };

    private final String shortName;
    private final String plameId;
    private final String pleId;
    private final String afpId;
    private final String fv3800Id;
    private final String regex;
    private final boolean foreign;
    private final boolean acceptedForNonDomiciled;

    /**
     * Creates a DOI (Document of Identification) type definition with its associated
     * SUNAT subsystem identifiers, validation pattern, and classification flags.<br>
     * <br>
     * Parameters map directly to official codes used across Peruvian electronic
     * reporting platforms:<br>
     * - <strong>PLAME</strong>: Payroll and employment declaration system.<br>
     * - <strong>PLE</strong>: Electronic accounting ledgers (Libro Diario,
     * Registro de Ventas, Registro de Compras, etc.).<br>
     * - <strong>AFPNet</strong>: Pension fund contribution reporting.<br>
     * - <strong>FV3800</strong>: Declared beneficiary and ownership-chain
     * identification form (Declaración de Beneficiario Final).<br>
     * <br>
     * Each DOI type defines:<br>
     * - A strict structural validation regex that must match the sanitized value.<br>
     * - A sanitization rule (via {@link #sanitize(String)}) aligned with SUNAT
     * formatting requirements.<br>
     * - Classification flags for foreign document types and admissibility when
     * declaring non-domiciled subjects.<br>
     * <br>
     * <strong>foreign</strong> indicates whether the identification document is,
     * by nature, issued to foreign individuals (e.g., Passport, CE, TIN). This is
     * independent of fiscal domicile.<br>
     * <br>
     * <strong>acceptedForNonDomiciled</strong> specifies whether SUNAT accepts this
     * DOI type for declarations involving non-domiciled subjects. Only specific
     * document types (Passport, Foreign ID, TIN) are admissible in such contexts
     * across systems like FV-3800, FE-CR, or PLE Ventas when the counterparty is
     * non-domiciled.<br>
     *
     * @param shortName               short descriptive code for the DOI type
     * @param plameId                 identifier used in PLAME filings; empty when not applicable
     * @param pleId                   identifier used in PLE electronic ledgers
     * @param afpId                   identifier for AFPNet pension reporting
     * @param fv3800Id                identifier used in FV-3800 declarations
     * @param regex                   strict validation regex defining valid DOI structure
     * @param foreign                 whether this DOI type corresponds to a foreign-issued document
     * @param acceptedForNonDomiciled whether SUNAT accepts this DOI type for
     *                                non-domiciled subject declarations
     */
    DoiType(String shortName, String plameId, String pleId,
            String afpId, String fv3800Id, String regex,
            boolean foreign, boolean acceptedForNonDomiciled) {
        this.shortName = shortName;
        this.plameId = plameId;
        this.pleId = pleId;
        this.afpId = afpId;
        this.fv3800Id = fv3800Id;
        this.regex = regex;
        this.foreign = foreign;
        this.acceptedForNonDomiciled = acceptedForNonDomiciled;
    }

    /**
     * Returns the short internal code used for display or mapping.
     *
     * @return short identifier for the DOI type
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * PLAME document type identifier defined by SUNAT.<br>
     * This value is used when generating payroll (planilla) submissions.
     *
     * @return PLAME identifier, or an empty string when not applicable
     */
    public String getPlameId() {
        return plameId;
    }

    /**
     * PLE (Libro Electrónico) document type identifier.<br>
     * Used in electronic accounting books submissions.
     *
     * @return PLE identifier, or an empty string when not applicable
     */
    public String getPleId() {
        return pleId;
    }

    /**
     * AFPNet identifier used for pension fund electronic submissions.
     *
     * @return AFPNet identifier, or an empty string when not applicable
     */
    public String getAfpId() {
        return afpId;
    }

    /**
     * FV3800 identifier used in the “Registros de Compras y Ventas”
     * consolidated declaration format.
     *
     * @return FV3800 identifier, or an empty string when not applicable
     */
    public String getFv3800Id() {
        return fv3800Id;
    }

    /**
     * Returns the regular expression used to validate the raw structure of
     * this DOI type in strict mode (no sanitization).
     *
     * @return regex pattern defining the valid structure
     */
    public String getRegex() {
        return regex;
    }

    /**
     * Indicates whether this DOI type corresponds to a document inherently issued
     * to foreign individuals.<br>
     * <br>
     * This flag reflects the nature of the identification document itself, not
     * the fiscal domicile of the person. For example:<br>
     * - Passport, Foreign ID, TIN, CE, PTP, Refugee and Diplomatic IDs are foreign
     * by document type.<br>
     * - DNI, RUC, and PNP IDs are domestic documents.<br>
     * <br>
     * This classification is useful for systems that require specifying the
     * country of issuance (e.g., T-Registro) or when filtering foreign document
     * types in user interfaces.
     *
     * @return {@code true} if the document type is inherently foreign;
     * {@code false} otherwise
     */
    public boolean isForeign() {
        return foreign;
    }

    /**
     * Indicates whether SUNAT allows this DOI type to be used when declaring
     * non-domiciled subjects across its reporting platforms.<br>
     * <br>
     * Only a very limited set of document types are admissible for
     * non-domiciled individuals, typically:<br>
     * - Foreign Identification (ID)<br>
     * - Taxpayer Identification Number (TIN)<br>
     * <br>
     * Other foreign documents such as CE, PTP, Refugee or Diplomatic IDs are
     * intended for residents in Peru and are not valid for identifying
     * non-domiciled subjects in systems like FV-3800, PLE Ventas, FE-CR, or
     * informational returns related to non-domiciled taxation.<br>
     *
     * @return {@code true} if the DOI type is accepted for non-domiciled
     * declarations; {@code false} otherwise
     */
    public boolean isAcceptedForNonDomiciled() {
        return acceptedForNonDomiciled;
    }

    /**
     * Sanitizes a DOI number by removing invalid characters and applying
     * maximum-length constraints consistent with SUNAT rules.<br>
     * Implementations differ by DOI type.
     *
     * @param rawNumber raw DOI value as entered by a human
     * @return sanitized value; never {@code null}
     */
    public abstract String sanitize(String rawNumber);

    /**
     * Validates the DOI number structure using this type’s regex.<br>
     * If {@code strict} is {@code false}, the value is sanitized before matching.<br>
     * <br>
     * Structural validation does <strong>not</strong> imply that the document
     * exists or is valid in SUNAT’s registry; it only checks format correctness.
     *
     * @param number raw DOI number
     * @param strict whether sanitization should be disabled
     * @return {@code true} if structurally valid
     */
    public boolean validateNumber(String number, boolean strict) {
        if (number == null || number.isBlank()) return false;
        String input = strict ? number : sanitize(number);
        return input.matches(this.regex);
    }

    /**
     * Determines whether this DOI type is suitable for use in the specified
     * SUNAT reporting context.<br>
     * <br>
     * Each DOI type defines its own set of subsystem identifiers
     * (PLAME, PLE, AFPNet, FV-3800). A DOI type is considered suitable for a
     * given context when the corresponding identifier is not blank. This reflects
     * SUNAT's official validation rules, where accepted DOI types vary across
     * platforms and declaration formats.<br>
     * <br>
     * Examples:<br>
     * - A RUC is always suitable for PLE and FV-3800, but never for AFPNet.<br>
     * - A Passport or TIN is valid for FV-3800 (when declaring non-domiciled
     *   subjects), but not for PLAME or AFPNet.<br>
     * - A Carné de Extranjería (CE) is acceptable for PLAME but not for
     *   declarations involving non-domiciled entities.<br>
     * <br>
     * This method performs no sanitization; callers are expected to validate or
     * sanitize the DOI value separately if required.<br>
     *
     * @param context the SUNAT reporting context to evaluate; must not be null
     * @return {@code true} if this DOI type is accepted in the specified context;
     *         {@code false} otherwise
     * @throws NullPointerException     if {@code context} is null
     * @throws IllegalArgumentException if the context is not recognized
     */
    public boolean isSuitableFor(UsageContext context) {
        Objects.requireNonNull(context, "context must not be null to determine type suitability.");
        switch (context) {
            case PLE:
                return !pleId.isBlank();
            case PLAME:
                return !plameId.isBlank();
            case AFP_NET:
                return !afpId.isBlank();
            case FV_3800:
                return !fv3800Id.isBlank();
        }
        throw new IllegalArgumentException(String
                .format("Invalid context to determine %s suitability of type %s", context, name()));
    }
}
