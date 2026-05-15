/**
 * Domain layer for the gateway service.
 *
 * <p>Pure business model and invariants — no Spring, no JPA, no I/O. The gateway has minimal domain
 * concepts (it is primarily a thin edge layer), but value objects representing routing rules,
 * correlation identifiers, or rate-limit policies live here. Code in this package must remain
 * framework-free and independently testable.
 */
package com.pdfmaster.gateway.domain;
