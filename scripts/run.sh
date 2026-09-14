#!/usr/bin/env bash
# Fixed Shell Runner.
# Executed via: ./scripts/run.sh
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
python3 "$DIR/runner.py" "$@"
