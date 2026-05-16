# Resize PDF Pages — Change Page Dimensions to Any Paper Size

PDF documents created for one paper size often need to be adapted for another — a US Letter document needs to be distributed in Europe where A4 is standard, an A3 engineering drawing needs to fit on an A4 sheet, or a square social media post needs to be placed on a standard page. PDF page resizing changes the physical dimensions of each page to the target size, optionally scaling or padding the content to fit.

PDFMaster's resize tool supports all standard paper sizes and custom dimensions, with control over how content is adapted to the new page size.

## Understanding PDF Page Dimensions

A PDF page's physical dimensions are defined by its MediaBox — a rectangle specifying the page's width and height in points (1 point = 1/72 inch). Standard paper sizes map to specific MediaBox dimensions:

| Paper Size | Width (mm) | Height (mm) | Width (pt) | Height (pt) |
|------------|-----------|------------|-----------|------------|
| A4 | 210 | 297 | 595.28 | 841.89 |
| A3 | 297 | 420 | 841.89 | 1190.55 |
| Letter | 215.9 | 279.4 | 612 | 792 |
| Legal | 215.9 | 355.6 | 612 | 1008 |
| Tabloid/Ledger | 279.4 | 431.8 | 792 | 1224 |

When you resize a PDF, the MediaBox dimensions are changed to match the target size. The content can be:

- **Scaled proportionally** to fill the target page (content may be larger or smaller than original)
- **Centered with padding** — content remains at original scale, white space is added around it if the target is larger
- **Cropped** — content is scaled to fill the target and any overflow is clipped

## How PDFMaster Resizes PDF Pages

**Paper size selection.** Choose from all standard ISO (A-series, B-series) and North American (Letter, Legal, Tabloid) sizes, or enter custom dimensions in millimeters or inches.

**Orientation.** Resize to portrait or landscape regardless of the original orientation.

**Content handling.** Select how the content is adapted: proportional scale-to-fit (content fills the page without distortion), center-and-pad (original scale preserved, page enlarged around content), or scale-and-fill (content fills target with potential cropping if aspect ratios differ).

**Page range.** Apply the resize to all pages or specify a range.

## Common Use Cases

**US Letter to A4 conversion.** North American documents are typically created on Letter paper (8.5" × 11"). When shared with European recipients, the slightly different dimensions can cause printing issues (A4 is taller and narrower). Resizing to A4 ensures the document prints correctly on European printers.

**Engineering drawing scale-down.** Technical drawings are often created at A1 or A0 size. Resizing to A4 produces a portable reference copy. Combined with proportional scaling, all dimensions remain legible.

**Presentation slide adaptation.** Presentations exported as PDFs at 16:9 slide dimensions need resizing to A4 or Letter for document workflows that expect standard page sizes.

**Poster reduction for proofing.** Large-format posters (A0, 36" × 48") can be scaled down to A4 for low-cost print proofing before committing to expensive large-format printing.

**Social media graphics to document.** Square or custom-ratio images or PDFs can be placed on standard pages by resizing with center-and-pad.

## PDF Resize vs PDF Crop

| | Resize | Crop |
|---|--------|------|
| What changes | Physical page dimensions (MediaBox) | Visible area (CropBox) |
| Content scale | Optionally scaled | Not scaled |
| Content lost | Only if scale-and-fill with overflow | Cropped region hidden (not deleted) |
| Use case | Change paper size | Remove margins |

Use **resize** when you need a different paper size. Use **crop** when you need to trim margins from within the existing page size.

## PDF Resize vs Printing at a Different Scale

An alternative approach to resizing is simply printing the PDF with a different scale factor (e.g., "fit to page" in the print dialog). The problem with this approach is that the original file dimensions remain unchanged, and every recipient's printer must apply the same scaling — leading to inconsistency. Proper PDF resizing embeds the correct dimensions in the file itself, so the PDF prints at the right size on any printer without configuration.

## Tips for Resizing PDFs

**Use proportional scaling for text-heavy documents.** Scaling content proportionally ensures text remains in the correct size relationship to the page — important for documents where margins and text size are deliberately chosen.

**Use center-and-pad for diagrams and forms.** When the content should remain at its original scale (forms with fixed field sizes, scale drawings), padding preserves the original content dimensions and adds whitespace to reach the target page size.

**Check page counts after resizing.** Resizing does not reflowing text — if content is scaled down, it all fits on the same pages. If you need the text to reflow (e.g., for pagination), convert to DOCX, resize there, and re-export to PDF.

**Verify print margins.** After resizing, open the PDF and check that content is not too close to the page edges for your printer's minimum margin requirements (typically 5–10mm).

## Privacy and Security

PDFMaster performs all PDF resizing in isolated, network-sandboxed processing containers on our own infrastructure. Your document is never sent to a third party. Files are encrypted during transit (TLS 1.3) and while stored (AES-256), and are automatically deleted within 60 minutes of upload. We do not retain document content, file names, or any personally identifiable information derived from your documents.
