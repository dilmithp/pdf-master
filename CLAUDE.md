# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project context

PDF productivity SaaS (iLovePDF / Smallpdf / Sejda competitor) with three wedges: privacy-first, AI-native, developer-friendly. The full architectural vision, scope, and constraints live in a master build prompt the user maintains separately — always ask for the relevant section if not provided.

## Architecture (planned, not all built yet)

- **Frontend monorepo** at `apps/web` — Next.js 16 (App Router, RSC default, React Compiler), Tailwind v4, shadcn/ui, pnpm workspace.
- **Backend services** at `services/*` (not yet scaffolded) — 9 Spring Boot 3.3+ microservices on Java 17: `gateway`, `auth-service`, `pdf-core-service`, `pdf-convert-service`, `pdf-ocr-service`, `pdf-ai-service`, `esign-service`, `billing-service`, `notification-service`. One Postgres cluster with per-service schemas, **no cross-service DB reads**.
- **Async-by-default** for PDF ops: HTTP returns `202 + jobId`, RabbitMQ jobs, KEDA-autoscaled workers. Sync REST only for trivially fast ops.
- **File lifecycle is the privacy moat**: presigned direct S3 uploads (never proxied), `auto-delete=true` tag, 60-min S3 TTL, sandboxed workers (`--read-only`, `--cap-drop=ALL`, no egress).

## Tech stack (locked — do not propose alternatives without strong cause)

- Web: Next.js 16, React 19, TS 5 strict, Tailwind v4 (`@tailwindcss/postcss`), Biome (not ESLint+Prettier), Vitest + Testing Library + Playwright, pnpm 9.
- Backend: Java 17/21, Spring Boot 3.3+ (WebFlux for I/O, MVC for CRUD), Gradle 8 Kotlin DSL, Postgres 16 + Redis 7 + RabbitMQ + pgvector.
- Infra: AWS (us-east-1 + eu-west-1 GDPR), EKS + Helm + ArgoCD, Cloudflare CDN/WAF, OpenTelemetry → Grafana Cloud.
- PDF libs: **Apache PDFBox 3.x** primary (Apache 2.0), qpdf CLI (Apache 2.0), LibreOffice headless sidecar (MPL 2.0), Tess4J (Apache 2.0).

## License hygiene (non-negotiable)

**iText 9 (AGPL) and Ghostscript (AGPL) are banned** from the dependency tree unless the user explicitly confirms a paid commercial license. Surface license questions before adding any new PDF/processing library. CI must run a license audit and fail on AGPL/GPL transitive deps.

## SEO is the GTM

`apps/web/app/(marketing)/**` is the SEO surface. Per-route rules:

- Every tool/convert/use-case page goes through `generateStaticParams` + `generateMetadata`.
- Required on-page elements (in order): H1 with primary kw → **working tool above the fold (no signup)** → trust strip → 600–1,200 words unique content → How-to → FAQ (5–8) → related tools → social proof.
- Required JSON-LD on every tool page: `SoftwareApplication`, `FAQPage`, `HowTo`, `BreadcrumbList`. `Organization` is in the root layout.
- CWV targets (P75 CrUX): LCP < 2.0s, INP < 200ms, CLS < 0.05. Lighthouse CI gates merges on marketing routes.
- hreflang for `en`, `es`, `fr`, `de`, `pt` (highest PDF tool search volumes).
- GEO (AI Overviews / Perplexity / ChatGPT citation): clear definitional sentence near top, comparison tables, stable descriptive slugs, `llms.txt` at root.

Shared SEO types live in `apps/web/lib/seo/types.ts` — schema generators consume them.

## Common commands

```bash
pnpm install              # install workspace deps
pnpm dev                  # run apps/web in dev mode
pnpm --filter @pdf-master/web dev     # same, explicit
pnpm build                # build all workspaces
pnpm typecheck            # tsc --noEmit across workspaces
pnpm test                 # vitest, all workspaces
pnpm --filter @pdf-master/web test:e2e   # Playwright E2E
pnpm lint                 # biome check .
pnpm lint:fix             # biome check --write .
```

Run a single Vitest test: `pnpm --filter @pdf-master/web exec vitest run path/to/file.test.ts`.

## Code conventions

- **TypeScript:** strict mode, `verbatimModuleSyntax`, `noUncheckedIndexedAccess`, `exactOptionalPropertyTypes` all on. Use `import type` for types.
- **React:** Server Components by default. `"use client"` only at the leaf level — never at the top of the tree.
- **Mutations:** Prefer Server Actions over bespoke API routes for simple cases.
- **A11y:** WCAG 2.1 AA minimum.
- **No `console.log`** in shipped code (Biome enforces; `warn`/`error` allowed).
- **Java (when scaffolded):** Hexagonal layout — `domain/` (no Spring), `application/`, `adapter/in/`, `adapter/out/`, `config/`. 80% line / 70% branch coverage on domain + application. Forward-only Flyway migrations.

## Rules of engagement (binding — from master build prompt §11)

1. Never invent infrastructure — ask if unsure whether a service / env var / config exists.
2. Production-grade only — no `TODO` comments, no swallowed exceptions, no hardcoded secrets.
3. Security defaults mandatory — every endpoint authenticated unless explicitly public; every upload validated (magic-byte, not Content-Type).
4. Tests with the code — no PR without tests.
5. Performance budget — no >10% p95 latency regression without justification.
6. SEO budget — new marketing page must hit CWV in Lighthouse CI before merge.
7. License hygiene — see above.
8. Reversibility — every DB migration ships with a documented rollback plan.
9. Boring first — battle-tested over novel unless measurably justified.
10. Ask before scope creep — if a task expands beyond what was asked, stop and confirm.

## Implementation sequence (for any feature)

1. Restate the requirement.
2. List files to create / modify.
3. Outline tests.
4. Generate code.
5. Generate tests.
6. Summarize verification steps the user should run locally.
