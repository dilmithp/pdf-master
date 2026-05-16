# Contributing

## Branch naming

All feature branches must follow this naming scheme:

| Prefix | When to use |
|--------|-------------|
| `feat/` | New user-visible feature |
| `fix/` | Bug fix |
| `chore/` | Tooling, deps, build scripts, CI |
| `docs/` | Documentation only |
| `refactor/` | Code restructuring with no behaviour change |
| `test/` | Test additions or corrections |

Examples: `feat/pdf-merge-api`, `fix/auth-token-expiry`, `chore/upgrade-spring-boot-3.4`.

Use lowercase kebab-case. Keep the slug under 50 characters.

## Conventional commits

Every commit subject must follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<optional scope>): <imperative summary under 72 chars>
```

Valid types: `feat`, `fix`, `chore`, `docs`, `refactor`, `test`, `ci`, `perf`, `style`.

Scopes map to modules: `gateway`, `auth-service`, `pdf-core`, `pdf-ai`, `web`, `billing`, `esign`, `notification`, `infra`, `build`.

Examples:

```
feat(auth-service): add refresh-token rotation endpoint
fix(pdf-core): handle zero-page PDFs in merge pipeline
ci(github-actions): add matrix ECR push for all services
chore(deps): upgrade testcontainers to 1.20
```

Do not add AI-tool attribution, "Generated with", or similar trailers to commit messages or PR descriptions. Commits represent human authorship decisions.

## Pull request process

1. Open a PR from your feature branch targeting `main`.
2. Fill in every section of the PR template. "N/A" is acceptable only for sections that genuinely do not apply (e.g., Screenshots for a backend-only change).
3. CI must be green before requesting review:
   - `backend` job: Spotless, license audit, build, and all tests pass.
   - `frontend` job: typecheck, lint (Biome), and Vitest pass.
4. At least one reviewer approval is required before merge.
5. Squash-merge is preferred for small changes. Merge commits are acceptable for large feature branches where individual commit history is meaningful.
6. Delete the branch after merge.

## License hygiene

Before adding any new library:

- Check the license. **AGPL and GPL are banned** from the dependency tree.
- `./gradlew checkLicense` must pass. A failing license audit blocks the CI `backend` job.
- If you need a library with a restrictive license, raise it in the PR description and get explicit sign-off before merging.

## Code standards

- Java services use hexagonal architecture: `domain/` (no Spring), `application/`, `adapter/in/`, `adapter/out/`, `config/`.
- 80% line / 70% branch coverage on `domain` and `application` packages.
- TypeScript: strict mode enabled. `import type` for type-only imports.
- React: Server Components by default. `"use client"` only at leaf level.
- No `console.log` in shipped code (Biome enforces; `warn`/`error` are allowed).
- No `TODO` comments in production paths.
- Flyway migrations must include a documented rollback plan in the PR.
