#!/usr/bin/env bash
set -e

mkdir -p /logs
touch /logs/postgresql.log
chmod 777 /logs
chmod 666 /logs/postgresql.log

tail -F /logs/postgresql.log &

exec /usr/local/bin/docker-entrypoint.sh "$@"