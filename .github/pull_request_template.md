## Summary

<!-- What does this PR do and why? 1-3 sentences. -->

## Changes

<!-- Bullet list of notable changes. -->

-

## Testing

<!-- What did you run? What passed? Include commands and any relevant output. -->

```
./gradlew test
pnpm --filter @pdf-master/web test
```

## Screenshots

<!-- For UI changes: before/after screenshots or screen recordings. Leave blank for non-UI changes. -->

## Rollback plan

<!-- How do you revert if this breaks production? Include migration rollback steps if applicable. -->

## Checklist

- [ ] Tests added or updated for all changed behaviour
- [ ] ArchUnit rules pass (`./gradlew test`)
- [ ] No new AGPL/GPL dependencies introduced (`./gradlew checkLicense` passes)
- [ ] No `console.log` in shipped code (Biome enforces)
- [ ] No `TODO` comments left in production paths
- [ ] No secrets, credentials, or PII in code or test fixtures
- [ ] PR subject follows conventional commits (`feat:`, `fix:`, `chore:`, `docs:`, `refactor:`, `test:`, `ci:`)
- [ ] Flyway migration includes a documented rollback plan (if applicable)
- [ ] New marketing pages meet CWV targets in Lighthouse CI (if applicable)
