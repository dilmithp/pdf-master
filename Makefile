.PHONY: help dev dev-status dev-logs dev-down dev-clean test build fmt lint web-dev web-build

COMPOSE_FILE := compose/docker-compose.yml

## help: Show this help message
help:
	@grep -E '^## ' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ": "}; {printf "  %-15s %s\n", $$2, $$3}'

## dev: Start local dev stack (postgres, pgvector, redis, rabbitmq, minio, mailhog)
dev:
	docker compose -f $(COMPOSE_FILE) up -d
	@echo "Waiting for services to be healthy..."
	@docker compose -f $(COMPOSE_FILE) wait postgres postgres-vector redis rabbitmq minio 2>/dev/null || \
		docker compose -f $(COMPOSE_FILE) ps

## dev-status: Show status of all dev containers
dev-status:
	docker compose -f $(COMPOSE_FILE) ps

## dev-logs: Tail logs from all dev containers
dev-logs:
	docker compose -f $(COMPOSE_FILE) logs -f

## dev-down: Stop and remove dev containers (data volumes preserved)
dev-down:
	docker compose -f $(COMPOSE_FILE) down

## dev-clean: DESTRUCTIVE — stop containers and delete all data volumes
dev-clean:
	@printf "WARNING: This will DELETE all local dev data (postgres, redis, rabbitmq, minio). Continue? [y/N] " && \
		read ans && [ "$${ans:-N}" = "y" ] && \
		docker compose -f $(COMPOSE_FILE) down -v || echo "Aborted."

## test: Run all backend tests (sets Testcontainers Docker socket env vars for macOS)
test:
	DOCKER_HOST=unix:///$$HOME/Library/Containers/com.docker.docker/Data/docker.raw.sock \
	DOCKER_API_VERSION=1.43 \
	TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock \
	./gradlew test

## build: Build all backend services
build:
	./gradlew build

## fmt: Format all Java sources via Spotless
fmt:
	./gradlew spotlessApply

## lint: Run Spotless check and license audit
lint:
	./gradlew spotlessCheck checkLicense

## web-dev: Start Next.js dev server
web-dev:
	pnpm --filter @pdf-master/web dev

## web-build: Build the Next.js app
web-build:
	pnpm --filter @pdf-master/web build
