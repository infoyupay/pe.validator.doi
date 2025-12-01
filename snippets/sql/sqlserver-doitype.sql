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

CREATE TABLE Person
(
    Id        INT IDENTITY PRIMARY KEY,
    Name      NVARCHAR(200) NOT NULL,
    DoiType   NVARCHAR(16)  NOT NULL,
    DoiNumber NVARCHAR(32)  NOT NULL,
    CONSTRAINT CK_DoiType CHECK (DoiType IN (
                                             'OTHERS', 'DNI', 'PNP', 'CE', 'RUC', 'PASSPORT',
                                             'REFUGEE', 'DIPLOMATIC', 'PTP', 'ID', 'ID_PTP', 'TIN'
        ))
);
