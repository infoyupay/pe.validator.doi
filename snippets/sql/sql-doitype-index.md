# Snippets SQL para Tipos de DOI

Este directorio contiene definiciones SQL canónicas para **enumeraciones de tipos de DOI** en múltiples motores de base de datos.

Cada sección incluye un enlace a su archivo `.sql`.

---

## PostgreSQL  
🔗 **[postgresql-doitype.sql](./postgresql-doitype.sql)**

ENUM nativo + ejemplo de tabla.

---

## Oracle (PL/SQL)  
🔗 **[oracle-doitype.sql](./oracle-doitype.sql)**

Package de constantes + restricción CHECK.

---

## SQL Server (T-SQL)  
🔗 **[sqlserver-doitype.sql](./sqlserver-doitype.sql)**

Restricción CHECK estándar.

---

## MySQL / MariaDB  
🔗 **[mysql-doitype.sql](./mysql-doitype.sql)**

ENUM nativo, ideal para catálogos pequeños.

---

## SQLite  
🔗 **[sqlite-doitype.sql](./sqlite-doitype.sql)**

TEXT + CHECK constraint como emulación de ENUM.

---

Generado para **pe-validator-doi** con el fin de ayudar a desarrolladores a integrar la validación de tipos de DOI en diversos entornos de bases de datos.
