# Convert PDF to PDF/A — The Definitive Guide to ISO-Compliant Archival

Long-term digital preservation is one of the most demanding challenges for institutions managing records. Converting PDF to PDF/A is the most widely accepted solution to the problem: ISO standard 19005 defines precisely what must be in an archival document so that it can be rendered by any conforming reader, in any year, indefinitely into the future.

This guide explains exactly what PDF/A is, why conversion matters, when to use each conformance level, and how to convert any PDF to PDF/A in three steps using PDFMaster.

## Why Convert PDF to PDF/A?

Standard PDFs are designed for reliable on-screen rendering — today. They may depend on system fonts, JavaScript actions, embedded DRM, or external URI references. All of these create fragility. A PDF that renders correctly today can become unreadable in ten years if a dependency disappears.

PDF/A removes every fragility:

- **Fonts embedded** — no dependency on system typefaces
- **Colors device-independent** — all colors expressed in absolute ICC color spaces
- **No encryption** — archival copies must be accessible without keys that may be lost
- **No external references** — no JavaScript, no links to remote resources
- **Metadata standardized** — XMP metadata is required and validated

The practical result is a self-contained document that any conforming reader can reproduce exactly, without any external infrastructure.

## How PDF/A Compliance Works Technically

PDF/A is not a separate file format — it is a constrained profile of PDF. The file extension remains `.pdf`. The difference is in the internal structure:

The document must include a document-level XMP metadata block containing the `pdfaid:conformance` and `pdfaid:part` fields declaring the conformance level. All fonts must be embedded (not just referenced). All color spaces must be anchored to an output intent with an embedded ICC profile. No transparency is permitted in PDF/A-1 (it is allowed in PDF/A-2 and PDF/A-3). No encryption. No JavaScript.

A validating reader or pre-press tool can check the XMP declaration and verify every requirement programmatically. PDFMaster includes automated validation after conversion and surfaces any remaining issues.

## Choosing the Right PDF/A Level

| Level | Base Spec | Key Additions | Best For |
|-------|-----------|---------------|----------|
| PDF/A-1b | PDF 1.4 | Fonts embedded, no transparency, no encryption | Legal filings, government archives |
| PDF/A-2b | PDF 1.7 | JPEG 2000, layers, optional content, embedded files | Technical drawings, multi-layer documents |
| PDF/A-3b | PDF 1.7 | Arbitrary file attachments (e.g. XML invoice) | E-invoicing (ZUGFeRD, Factur-X), hybrid documents |

When in doubt, choose **PDF/A-1b**. It is the most universally accepted level by courts, regulatory bodies, and archival systems worldwide. European e-invoicing standards (Factur-X, ZUGFeRD 2.x) require PDF/A-3b with an embedded XML payload.

## Common Use Cases

**Legal firms and courts.** Many jurisdictions require PDF/A for electronic filing. PDF/A-1b is the dominant requirement in US federal courts (CM/ECF) and EU court systems.

**Records and document management.** ISO 15489 (records management) is satisfied by storing documents in PDF/A. The format is also endorsed by the Library of Congress for digital preservation.

**Healthcare records.** Electronic health records converted to PDF/A ensure patient records remain readable over decades, regardless of the EHR system that generated them.

**Academic publishing and research.** Dissertations, theses, and long-term research data are commonly archived in PDF/A-2b, which supports embedded files and JPEG 2000 compression.

**E-invoicing.** Factur-X and ZUGFeRD 2.x invoices embed a machine-readable XML file inside a PDF/A-3b document, satisfying both human-readable and automated-processing requirements simultaneously.

## PDF/A vs Standard PDF — Comparison

| Feature | Standard PDF | PDF/A-1b | PDF/A-2b |
|---------|-------------|----------|----------|
| Font embedding | Optional | Mandatory | Mandatory |
| Transparency | Allowed | Prohibited | Allowed |
| Encryption | Allowed | Prohibited | Prohibited |
| JavaScript | Allowed | Prohibited | Prohibited |
| Embedded files | Allowed | Prohibited | Optional |
| Layers (OCG) | Allowed | Prohibited | Allowed |
| Color management | Optional | Mandatory ICC | Mandatory ICC |
| XMP metadata | Optional | Mandatory | Mandatory |

## Tips and Tricks for Best Results

**Audit your source PDF first.** Documents with heavy transparency flattening (common in Illustrator-generated PDFs) take longer to convert and may produce larger files. Pre-flatten in the authoring application if possible.

**Use PDF/A-2b for scanned documents.** PDF/A-2b supports JPEG 2000 compression, which delivers better image quality at the same file size compared to the JPEG DCT allowed in PDF/A-1.

**Check the output intent.** If your document contains spot colors or custom ICC profiles, verify that the embedded output intent reflects the intended rendering environment.

**Do not remove metadata you need.** PDF/A standardizes metadata but does not prescribe content. Preserve document title, author, and keywords — they aid future discovery.

**Validate before distributing.** Use the validation report PDFMaster provides after conversion. Some PDF generators produce unusual constructs that may require manual review even after automated conversion.

## Privacy and Security

PDFMaster converts your documents entirely on our own infrastructure. Files are transmitted over TLS 1.3 and stored encrypted at rest with AES-256. All uploaded and converted files are automatically deleted from our servers within 60 minutes of upload, with no exceptions and no manual overrides required. We never analyze your document content for any purpose other than conversion. Conversion logs contain only anonymized job metadata — no document content, no file names.

Your PDF/A output is returned directly to your browser and is never shared with any third party.
