#!/usr/bin/env bash
set -euo pipefail

echo "Initializing application databases..."

: "${POSTGRES_PASSWORD:?POSTGRES_PASSWORD is required}"

DATABASES=(
    "IAM_DB_NAME"
)

for DB_NAME_VAR in "${DATABASES[@]}"; do
    DB_NAME="${!DB_NAME_VAR:?$DB_NAME_VAR is required}"

    echo "Initializing database: $DB_NAME"

    psql \
        --username "$POSTGRES_USER" \
        --dbname "$POSTGRES_DB" \
        --set ON_ERROR_STOP=1 \
        --set db_name="$DB_NAME" \
        --set db_password="$POSTGRES_PASSWORD" <<'EOSQL'

SELECT format(
    'CREATE ROLE %I LOGIN PASSWORD %L',
    :'db_name',
    :'db_password'
)
WHERE NOT EXISTS (
    SELECT 1 FROM pg_roles WHERE rolname = :'db_name'
)
\gexec

SELECT format(
    'CREATE DATABASE %I OWNER %I',
    :'db_name',
    :'db_name'
)
WHERE NOT EXISTS (
    SELECT 1 FROM pg_database WHERE datname = :'db_name'
)
\gexec

SELECT format('Database %I initialized successfully.', :'db_name');

EOSQL

    echo "Database $DB_NAME initialization completed."

done

echo "All application databases initialized successfully."