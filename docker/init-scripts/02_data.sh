#!/bin/sh

sqlplus -s book_ex/book_ex@//localhost:1521/FREEPDB1 <<'SQL'
WHENEVER SQLERROR EXIT SQL.SQLCODE
@/opt/oracle/scripts/02_data.sql
EXIT
SQL