# Local Dev Stack

This directory contains the Docker Compose configuration for running the PDF Master infrastructure dependencies locally. The Java services themselves are **not** in this compose file — run them separately with `./gradlew :services:<svc>:bootRun`.

## Prerequisites

- **Docker Desktop** (macOS/Windows) or **Colima** (macOS/Linux alternative)
  - Colima: `brew install colima && colima start --memory 4 --disk 60`
- Docker Compose v2 (`docker compose` not `docker-compose`)
- Make

## Quickstart

```bash
make dev          # pull images and start all services in the background
make dev-status   # verify all containers are healthy
```

All services start detached. Allow ~30 seconds for healthchecks to pass on first run (image pulls may take longer).

## Service Ports

| Service         | Port(s)       | UI / Notes                              |
|-----------------|---------------|-----------------------------------------|
| PostgreSQL      | 5432          | Shared cluster; per-service schemas     |
| PostgreSQL+pgvector | 5433     | Used by pdf-ai-service only             |
| Redis           | 6379          |                                         |
| RabbitMQ        | 5672, 15672   | Management UI: http://localhost:15672   |
| MinIO           | 9000, 9001    | Console: http://localhost:9001          |
| Mailhog         | 1025, 8025    | Web UI: http://localhost:8025           |

Default credentials are in `.env.example` at the repo root.

## Environment Variables

Copy `.env.example` to `.env` and source it (or load it in your IDE run configuration) before starting any Java service:

```bash
cp .env.example .env
# edit .env if you need to override any values
set -a && source .env && set +a
```

## Testcontainers (macOS Docker Desktop raw socket)

When running `make test` on macOS, the Makefile sets these environment variables automatically to point Testcontainers at the Docker Desktop raw socket:

```
DOCKER_HOST=unix://~/Library/Containers/com.docker.docker/Data/docker.raw.sock
DOCKER_API_VERSION=1.43
TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock
```

If you use Colima, override `DOCKER_HOST` to `unix://${HOME}/.colima/default/docker.sock` instead.

## Useful Commands

```bash
make dev-logs     # tail live logs from all containers
make dev-down     # stop containers (data volumes preserved)
make dev-status   # show container health status
```

## Destroying All Local Data

```bash
make dev-clean    # prompts for confirmation, then deletes all volumes
```

This is irreversible for local dev data (no production data is stored here). Use it when you need a clean slate or are switching branches that have conflicting migrations.

## Troubleshooting

**Containers restart-looping on first start**
Run `make dev-logs` to identify the failing service. Most common cause: port conflict. Check with `lsof -i :<PORT>`.

**minio-init exits non-zero**
The `minio-init` container runs once and is expected to exit. An exit code of 0 means success. If it exits non-zero, check its logs: `docker compose -f compose/docker-compose.yml logs minio-init`.

**pg_isready fails / postgres not accepting connections**
The init SQL runs on first start only. If the volume already exists from a previous run with a different password, the env vars are ignored. Run `make dev-clean` to reset.

**RabbitMQ healthcheck takes 30+ seconds**
This is normal on first start. The `start_period` is set to 30s. Subsequent restarts are faster.
