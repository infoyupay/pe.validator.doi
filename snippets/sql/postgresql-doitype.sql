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

-- PostgreSQL ENUM type for DOI types
CREATE TYPE doi_type AS ENUM (
    'OTHERS',
    'DNI',
    'PNP',
    'CE',
    'RUC',
    'PASSPORT',
    'REFUGEE',
    'DIPLOMATIC',
    'PTP',
    'ID',
    'ID_PTP',
    'TIN'
    );

-- Example usage in a table
CREATE TABLE person (
                        id SERIAL PRIMARY KEY,
                        name TEXT NOT NULL,
                        doi_type doi_type NOT NULL,
                        doi_number VARCHAR(32) NOT NULL
);
