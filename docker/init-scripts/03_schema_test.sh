#!/bin/sh

sqlplus -s book_ex_test/book_ex_test@//localhost:1521/FREEPDB1 <<'SQL'
WHENEVER SQLERROR EXIT SQL.SQLCODE
@/opt/oracle/scripts/01_schema.sql
EXIT
SQL