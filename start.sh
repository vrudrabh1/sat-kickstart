#!/usr/bin/env bash
# start.sh — starts the sat-kickstart backend and frontend concurrently
set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"
BACKEND="$ROOT/backend"
FRONTEND="$ROOT/frontend"

# ── Colours ──────────────────────────────────────────────────────────────────
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'
CYAN='\033[0;36m'; BOLD='\033[1m'; RESET='\033[0m'

log()  { echo -e "${BOLD}[start]${RESET} $*"; }
ok()   { echo -e "${GREEN}✓${RESET} $*"; }
warn() { echo -e "${YELLOW}⚠${RESET}  $*"; }
err()  { echo -e "${RED}✗${RESET}  $*"; }

# ── Load root local config (HOST_IP, etc.) ────────────────────────────────────
[ -f "$ROOT/.env.local" ] && set -a && source "$ROOT/.env.local" && set +a

# ── Pre-flight checks ─────────────────────────────────────────────────────────
check_command() {
  if ! command -v "$1" &>/dev/null; then
    err "Required command not found: $1"
    echo "  Install it and re-run this script."
    exit 1
  fi
  ok "$1 found"
}

log "Checking prerequisites…"

# ── Auto-detect Java if not on PATH (common with Homebrew installs) ────────────
if ! command -v java &>/dev/null || ! java -version &>/dev/null 2>&1; then
  # Try Homebrew OpenJDK locations (Apple Silicon and Intel)
  for candidate in \
      /opt/homebrew/opt/openjdk/bin \
      /opt/homebrew/opt/openjdk@21/bin \
      /opt/homebrew/opt/openjdk@17/bin \
      /usr/local/opt/openjdk/bin \
      /usr/local/opt/openjdk@21/bin \
      /usr/local/opt/openjdk@17/bin; do
    if [ -x "$candidate/java" ]; then
      export PATH="$candidate:$PATH"
      export JAVA_HOME="$(dirname "$candidate")"
      warn "Java not on PATH — using $candidate"
      break
    fi
  done
fi

check_command java
check_command node
check_command npm

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/{print $2}' | cut -d. -f1)
if [ "$JAVA_VERSION" -lt 17 ] 2>/dev/null; then
  err "Java 17+ required (found Java $JAVA_VERSION)"
  exit 1
fi

# ── Ensure mvnw is executable (can lose the bit after git clone) ──────────────
[ -f "$BACKEND/mvnw" ] && chmod +x "$BACKEND/mvnw"

# ── .env setup ────────────────────────────────────────────────────────────────
if [ ! -f "$BACKEND/.env" ]; then
  if [ -f "$BACKEND/.env.example" ]; then
    cp "$BACKEND/.env.example" "$BACKEND/.env"
    warn "Created backend/.env from .env.example — edit it with your DB credentials if needed."
  fi
fi

if [ ! -f "$FRONTEND/.env.local" ]; then
  if [ -f "$FRONTEND/.env.example" ]; then
    cp "$FRONTEND/.env.example" "$FRONTEND/.env.local"
    ok "Created frontend/.env.local from .env.example"
  fi
fi

# ── Install frontend deps ──────────────────────────────────────────────────────
if [ ! -d "$FRONTEND/node_modules" ]; then
  log "Installing frontend dependencies…"
  (cd "$FRONTEND" && npm install --silent)
  ok "Frontend dependencies installed"
fi

# ── Cleanup on exit ───────────────────────────────────────────────────────────
BACKEND_PID=""
FRONTEND_PID=""

cleanup() {
  echo ""
  log "Shutting down…"
  [ -n "$BACKEND_PID" ]  && kill "$BACKEND_PID"  2>/dev/null && ok "Backend stopped"
  [ -n "$FRONTEND_PID" ] && kill "$FRONTEND_PID" 2>/dev/null && ok "Frontend stopped"
  exit 0
}
trap cleanup SIGINT SIGTERM

# ── Start backend ─────────────────────────────────────────────────────────────
log "Starting ${CYAN}Spring Boot backend${RESET} on port 8080…"
(
  cd "$BACKEND"
  # Export .env vars if the file exists
  [ -f .env ] && set -a && source .env && set +a
  # Dev profile uses H2 — unset PostgreSQL overrides so application-dev.properties wins
  unset SPRING_DATASOURCE_URL SPRING_DATASOURCE_USERNAME SPRING_DATASOURCE_PASSWORD SPRING_DATASOURCE_DRIVER_CLASS_NAME
  ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev -q 2>&1 | sed "s/^/${CYAN}[backend]${RESET} /"
) &
BACKEND_PID=$!

# ── Start frontend ────────────────────────────────────────────────────────────
log "Starting ${GREEN}React frontend${RESET} on port 5173…"
(
  cd "$FRONTEND"
  npm run dev 2>&1 | sed "s/^/${GREEN}[frontend]${RESET} /"
) &
FRONTEND_PID=$!

# ── Ready ─────────────────────────────────────────────────────────────────────
echo ""
echo -e "  ${BOLD}Backend:${RESET}   ${CYAN}http://localhost:8080${RESET}  (dev profile — H2 in-memory DB)"
echo -e "  ${BOLD}H2 console:${RESET}${CYAN}http://localhost:8080/h2-console${RESET}"
echo -e "  ${BOLD}Frontend:${RESET}  ${GREEN}http://localhost:5173${RESET}"
if [ -n "$HOST_IP" ]; then
  echo -e "  ${BOLD}Network:${RESET}   ${GREEN}http://${HOST_IP}:5173${RESET}"
fi
echo ""
echo -e "  Press ${BOLD}Ctrl+C${RESET} to stop both servers."
echo ""

# Wait for either process to exit
wait $BACKEND_PID $FRONTEND_PID
