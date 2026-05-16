# OCR PDF — Make Scanned Documents Searchable and Selectable

A scanned PDF is, at its core, just a sequence of image files bundled into a PDF container. The text you see on screen is drawn as pixels — the PDF file has no idea it contains words, sentences, or paragraphs. You cannot select a word, copy a sentence, search for a term, or have a screen reader read the content aloud. OCR changes all of this.

PDFMaster's OCR tool applies Optical Character Recognition to every page of a scanned PDF, embedding an invisible searchable text layer that makes the document fully functional while preserving the original scan appearance.

## What OCR Is and How It Works

Optical Character Recognition is the computational process of detecting text in image data. Modern OCR engines analyze the pattern of pixels on a page, compare them against trained character models, and output the most probable character sequence for each detected text region.

Contemporary OCR accuracy for clean, high-resolution scans exceeds 99% character accuracy for most Latin scripts. The recognized text is not displayed — it is embedded as a hidden text layer beneath the original page image, so the document looks identical but supports search, selection, and accessibility features.

**Four stages of OCR processing:**

1. **Image preprocessing.** The scanned image is deskewed (rotation corrected), denoised, and contrast-enhanced for best recognition accuracy.
2. **Layout analysis.** The page is analyzed to identify text regions, columns, tables, images, and other zones. Multi-column layouts and tables are handled separately from flowing text.
3. **Character recognition.** The OCR engine processes each text region, recognizing character sequences using trained neural network models.
4. **Text layer embedding.** The recognized text is embedded as invisible glyphs positioned to align with the corresponding characters in the scan image, using the PDF's invisible text rendering mode.

## Language Support

PDFMaster uses Tesseract OCR with LSTM-based recognition models, supporting 100+ languages including:

**Latin scripts:** English, Spanish, French, German, Portuguese, Italian, Dutch, Polish, Czech, Romanian, and many more.

**Cyrillic scripts:** Russian, Ukrainian, Bulgarian, Serbian, and related languages.

**Arabic and RTL scripts:** Arabic, Hebrew, Persian (Farsi), Urdu.

**East Asian scripts:** Simplified Chinese, Traditional Chinese, Japanese (including kanji, hiragana, katakana), Korean.

**Other scripts:** Greek, Thai, Vietnamese, Hindi (Devanagari), and more.

For best accuracy, specify the primary language of your document. For multilingual documents, select all relevant languages — the engine will apply models for each.

## Common Use Cases

**Digitizing paper document archives.** Law firms, healthcare providers, government agencies, and financial institutions that have scanned years of paper records into image-only PDFs can OCR the entire archive to make it fully searchable and discoverable.

**Making contracts and legal documents searchable.** Executed contracts are often scanned originals. OCR allows lawyers and paralegals to search for specific clauses, terms, or dates across a large document repository.

**Academic paper archive conversion.** Historical academic journals and conference proceedings exist only as scanned PDFs. OCR makes decades of research literature full-text searchable and indexable by academic search engines.

**Invoice and receipt processing.** Accounting teams processing scanned invoices can use OCR as a pre-processing step before data extraction — enabling automated reading of totals, dates, and vendor names.

**Accessibility compliance.** Government websites, universities, and public organizations are required by law (ADA, Section 508, EN 301 549) to make documents accessible to screen reader users. OCR converts image-only PDFs into screen-reader-compatible documents.

**Personal document management.** Individuals scanning personal documents (tax records, bank statements, medical records) can OCR them to make their personal archive searchable.

## OCR Quality: What Affects Accuracy

| Factor | Impact on accuracy |
|--------|-------------------|
| Scan resolution | 300 DPI minimum for good accuracy; 600 DPI for forms/tables |
| Image skew/rotation | Pre-processing corrects up to ~5°; severe skew reduces accuracy |
| Font type | Printed fonts: >99%; Handwriting: 70-85% (varies widely) |
| Paper quality | Yellowed, stained, or degraded paper reduces accuracy |
| Language selection | Selecting the correct language is essential for non-Latin scripts |
| Compression artifacts | JPEG artifacts in scanned images reduce accuracy |

## OCR PDF vs Alternative Text Extraction Methods

| Method | Works on scans? | Accuracy | Preserves layout? |
|--------|-----------------|----------|------------------|
| PDFMaster OCR | Yes | 99%+ for clean scans | Yes |
| pdftotext (PDFBox) | No (image PDFs) | N/A | Partial |
| Copy-paste from viewer | No (image PDFs) | N/A | No |
| Google Drive "Open with" | Yes | Good | Partial |
| Adobe Acrobat OCR | Yes | 99%+ | Yes |

## Tips for Best OCR Results

**Scan at 300 DPI or higher.** This is the most impactful improvement you can make. 200 DPI scans produce mediocre accuracy. 300 DPI is the minimum for acceptable results. 600 DPI is recommended for forms with small text.

**Scan in grayscale, not color.** Grayscale scans are often higher quality at the same file size as color scans. The additional color information in color scans does not improve OCR accuracy.

**Deskew before uploading.** Even a 1-2 degree rotation in the scan significantly reduces OCR accuracy. PDFMaster applies automatic deskew, but it works best on slight tilts — prevent severe skew at the scanning stage.

**Select the correct language.** Using the wrong language model is one of the most common causes of poor OCR accuracy. Always specify the actual language of the document.

**Verify the results for critical documents.** For legal, financial, or medical documents where accuracy is essential, review the recognized text by selecting and copying a representative sample after OCR.

## Privacy and Security

OCR processing involves analyzing the actual content of your documents — characters, words, sentences. PDFMaster performs all OCR on its own servers with strict data isolation. Your document content is never shared with third-party OCR services, never used to improve models, and never retained beyond the 60-minute automatic deletion window. All processing occurs in network-isolated containers with no internet egress. Your document content cannot leave PDFMaster's infrastructure during OCR processing.
