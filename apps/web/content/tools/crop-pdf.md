# Crop PDF Pages — Remove Margins and Trim Content Online

PDF files often contain unnecessary white space — scanner borders around scanned documents, wide margins in presentation exports, or reference marks from pre-press workflows that should not appear in the final document. Cropping a PDF removes this excess content by adjusting the page's visible boundary, producing a clean, properly sized document.

PDFMaster's PDF cropping tool lets you trim any PDF page with pixel-level precision using a visual drag interface or precise millimeter values.

## What Happens When You Crop a PDF?

A PDF page has multiple defined boxes that control different aspects of its rendering:

- **MediaBox** — the full physical page size, including printer marks
- **CropBox** — the portion of the page that viewers display (defaults to MediaBox if absent)
- **BleedBox** — the area to which page content can bleed in pre-press
- **TrimBox** — the final intended size after trimming in print production
- **ArtBox** — the meaningful content area for third-party use

When you crop a PDF, PDFMaster adjusts the **CropBox** to the region you define. Anything outside the CropBox becomes invisible in any standard PDF viewer. The underlying page content outside the crop is technically still in the file — this is how the crop can be reversed from the original file. If you need the content permanently removed, use the redact tool instead.

## How PDFMaster PDF Cropping Works

**Visual page preview.** Each page renders as an interactive thumbnail. Drag the crop handles inward from any edge to define the visible area. The preview updates in real-time.

**Precise margin input.** Enter exact margin values in millimeters for left, right, top, and bottom edges. Useful when you need to trim to an exact standard size (A4, Letter) from a larger scan.

**Per-page or apply-to-all.** Apply the same crop to every page at once (useful for consistent scanner margins on a multi-page scan) or configure each page individually.

**Crop to content.** The "auto-crop" option detects the smallest bounding box that contains all page content and removes white space outside it automatically.

## Common Use Cases

**Removing scanner borders.** Flatbed scanners often capture the entire glass surface, leaving black borders around the document. Auto-cropping removes these in one click across all pages.

**Trimming presentation slides.** Presentations exported to PDF as A4 pages often have thick white margins around the 16:9 slide content. Cropping to the slide content area produces a cleaner document at the correct aspect ratio.

**Removing pre-press crop marks.** PDFs prepared for commercial printing include crop marks, bleed areas, and color registration marks outside the intended page boundary. Cropping to the TrimBox removes all of these.

**Extracting a specific region.** When a PDF page contains multiple forms, diagrams, or tables and you need only one of them, crop the page to the target region. Use split-by-page first if needed.

**Creating consistent page sizes.** When merging PDFs from multiple sources with different page dimensions, crop all pages to a standard size before merging to produce a uniform document.

## PDF Crop vs PDF Resize

These two operations are often confused:

| Operation | What changes | Content scaling | File size impact |
|-----------|-------------|----------------|----------------|
| Crop | CropBox boundary | None (content unchanged) | Minimal |
| Resize | MediaBox dimensions | Content scaled to fit | Varies |

Cropping removes a portion of the visible area from the edges of the page. Content scale does not change. Resizing changes the physical dimensions of the page and optionally scales all content to fit. If you need to make a Letter-sized document into A4-sized with scaled content, use resize. If you just need to trim excess margins, use crop.

## Tips for Precise PDF Cropping

**Use the auto-crop feature first.** For scanned documents with consistent margins, auto-crop removes white space reliably without manual adjustment. Review a few pages before accepting.

**Check facing pages separately.** In books and brochures with alternating left/right pages, inner margins (gutter) and outer margins differ. Apply different crops to odd and even pages if needed.

**Preserve crop symmetry for professional documents.** When cropping a document intended for distribution, maintain equal left and right margins (and top and bottom margins) for a balanced appearance.

**Export at the correct DPI after cropping.** If you subsequently export pages to images, set the DPI to match your intended use (72 DPI for screen, 150 DPI for email, 300 DPI for print).

**Keep the original file.** Since cropping only adjusts the CropBox, you can restore the full page by re-opening the original. Never discard the pre-crop original until you are certain the output meets your needs.

## Privacy and Security

PDF cropping is performed entirely on PDFMaster's servers with no external network access from the processing environment. Your document content is never inspected, analyzed, or retained. All files are encrypted in transit with TLS 1.3 and at rest with AES-256, and automatically deleted within 60 minutes of upload. The 60-minute deletion is automatic and unconditional — no manual request or account action is required.
