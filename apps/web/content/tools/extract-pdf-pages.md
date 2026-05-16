# Extract Pages from PDF — Get Exactly the Pages You Need

Long PDF documents are common: a 200-page annual report where you need the financial statements on pages 45-67, a 500-page legal filing where you need a specific exhibit, a textbook chapter you want to send to a colleague. Extracting specific pages from a PDF lets you share or work with exactly the content you need without distributing the entire document.

PDFMaster's page extraction tool lets you select individual pages, ranges, or any combination by clicking page thumbnails or typing page numbers.

## What PDF Page Extraction Does

Page extraction copies the specified pages from a source PDF into a new, standalone PDF document. The source document is not modified. The output PDF contains only the selected pages, preserving all original content — text, images, vector graphics, fonts, links, form fields, and annotations — from those pages.

This is distinct from PDF splitting, which divides a document at regular intervals (every N pages, or at specified page boundaries). Extraction is selective — you choose exactly which pages you want.

## How PDFMaster Extracts PDF Pages

**Interactive page thumbnail view.** Every page of your uploaded PDF is rendered as a thumbnail. Click to toggle individual pages in or out of the selection. Selected pages are highlighted.

**Range input.** Type page numbers and ranges in the input field: `1, 3, 5-10, 15, 20-25` selects pages 1, 3, 5 through 10, 15, and 20 through 25. Commas separate individual pages and ranges.

**Reverse selection.** Toggle "exclude selected" to extract all pages except those you have selected — useful for large documents where you want most pages but need to remove a few.

**Page count confirmation.** Before downloading, a summary shows the total page count in the extraction and a list of included page numbers, allowing you to confirm the selection is correct.

**Output.** A new PDF containing the selected pages in their original order (or reordered if you drag thumbnails). The output preserves all metadata from the original document including title, author, and keywords.

## Common Use Cases

**Extracting contract exhibits.** A signed contract may be hundreds of pages including multiple exhibits. Share a specific exhibit with a counterparty by extracting only those pages — faster to distribute and easier for the recipient to review.

**Pulling financial statements from annual reports.** Annual reports contain executive letters, strategy sections, and financial statements. Analysts who need only the financials can extract the relevant pages.

**Sharing a research paper section.** Academic papers follow a standard structure. Share only the methods or results section with collaborators who need only that portion.

**Creating reference cards from manuals.** Extract the quick-reference pages or specification tables from a large technical manual to create a portable reference document.

**Isolating a form or template.** Multi-form PDF packages (tax packages, application kits) can be separated into individual form PDFs by extracting the relevant pages.

**Evidence and exhibit management.** Legal teams extracting specific pages from large production documents for use as exhibits in depositions, hearings, or trial proceedings.

## Extracting Pages vs Splitting PDFs

| Feature | Extract Pages | Split PDF |
|---------|--------------|-----------|
| Selection method | Hand-picked pages and ranges | Fixed intervals or all-into-singles |
| Use case | Get specific pages | Divide into equal-sized chunks |
| Output files | One PDF with selected pages | Multiple PDFs |
| Source modification | No | No |
| Best for | Non-contiguous selections | Batch division |

Use **extract pages** when you need a specific, non-uniform selection. Use **split** when dividing a document into equal or predictable chunks.

## Extracting Pages vs Deleting Pages

These accomplish opposite goals:

- **Extract pages:** Keep specified pages, discard the rest → produces a smaller document from selected pages
- **Delete pages:** Discard specified pages, keep the rest → produces a smaller document minus selected pages

PDFMaster's extraction tool supports both workflows: select the pages to keep (extract mode) or select the pages to remove (exclude mode, which extracts all unselected pages).

## Tips for Extracting PDF Pages

**Confirm page numbering.** PDF page numbers and the printed page numbers inside the document may differ. PDFMaster uses PDF page numbers (the actual position in the file). If a document has a 10-page preface before page 1 of the content, PDF page 11 is printed page 1.

**Use range syntax for efficiency.** For large selections, ranges are much faster to type than individual page numbers. `5-100` is faster and less error-prone than listing all 96 pages individually.

**Extract before compressing.** If you are going to compress the output PDF, extract first and compress the smaller file. Compression algorithms work slightly better on smaller documents, and you save processing time.

**Preserve form fields.** Extracted pages containing interactive form fields retain their field definitions. The form will remain fillable unless you flatten it after extraction.

**Check links after extraction.** Internal cross-references (links to specific pages within the document) may become invalid after extraction if the target page was not included in the selection. Review any internal links in the extracted document.

## Privacy and Security

Page extraction processes only the file you upload. PDFMaster does not read, analyze, or retain the content of either the selected or unselected pages. The entire PDF is uploaded to process page rendering and content extraction, and the entire file is deleted within 60 minutes of upload along with the output. All files are encrypted in transit (TLS 1.3) and at rest (AES-256). The extraction engine runs in a sandboxed process with no network access — your document cannot reach the internet during processing.
