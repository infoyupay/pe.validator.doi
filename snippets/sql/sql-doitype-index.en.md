# SQL Snippets for DOI Types

This directory contains canonical SQL definitions for **DOI type enumerations** across multiple database engines.

Each section includes a link to its `.sql` file.

---

## PostgreSQL  
🔗 **[postgresql-doitype.sql](./postgresql-doitype.sql)**

Native ENUM + sample table.

---

## Oracle (PL/SQL)  
🔗 **[oracle-doitype.sql](./oracle-doitype.sql)**

Package of constants + CHECK constraint.

---

## SQL Server (T‑SQL)  
🔗 **[sqlserver-doitype.sql](./sqlserver-doitype.sql)**

Standard CHECK constraint.

---

## MySQL / MariaDB  
🔗 **[mysql-doitype.sql](./mysql-doitype.sql)**

Native ENUM ideal for small catalogs.

---

## SQLite  
🔗 **[sqlite-doitype.sql](./sqlite-doitype.sql)**

TEXT + CHECK constraint for ENUM simulation.

---

Generated for **pe-validator-doi** to help developers integrate DOI type validation into diverse database environments.
