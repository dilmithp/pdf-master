import type { SeoTool } from '@/lib/seo/types';

const NOW = '2026-05-15T00:00:00.000Z';

export const TOOL_SEED: readonly SeoTool[] = [
  {
    slug: 'merge-pdf',
    category: 'organize',
    primaryKeyword: 'merge pdf',
    h1: 'Merge PDF Files Online',
    metaTitle: 'Merge PDF — Combine PDF Files Online for Free, No Signup',
    metaDescription:
      'Merge PDF files online in seconds. Drag, drop, and reorder pages. Files deleted in 60 minutes. Free, secure, and no signup required.',
    ogTitle: 'Merge PDF Files Online — Free & Private',
    ogDescription:
      'Combine multiple PDFs into one in seconds. Drag-to-reorder, no signup, files auto-deleted in 60 minutes.',
    ogImage: '/og/merge-pdf.png',
    oneLineDefinition:
      'Merging PDFs combines two or more PDF files into a single document while preserving the original page order and content.',
    longDescription:
      'Combine multiple PDF documents into a single file without losing quality, formatting, or hyperlinks. Drag files to reorder, remove pages you do not need, and download the merged result. Everything runs through privacy-first processing, with uploaded files deleted from our servers within 60 minutes.',
    howToSteps: [
      {
        name: 'Upload your PDFs',
        text: 'Drag and drop two or more PDF files into the box above, or click to browse.',
      },
      {
        name: 'Reorder pages',
        text: 'Drag the file thumbnails to set the order you want them merged.',
      },
      {
        name: 'Merge and download',
        text: 'Click “Merge PDF” and download the combined document. Original files are deleted within 60 minutes.',
      },
    ],
    faqs: [
      {
        question: 'Is merging PDFs online safe?',
        answer:
          'Yes. Uploads are encrypted in transit (TLS 1.3) and at rest (AES-256). All files are automatically deleted from our servers within 60 minutes.',
      },
      {
        question: 'How many PDFs can I merge at once?',
        answer:
          'Free users can merge up to 20 files at a time. Pro users have no per-job file limit.',
      },
      {
        question: 'Will merging change the quality of my PDFs?',
        answer: 'No. We preserve the original page resolution, fonts, and structure byte-for-byte.',
      },
      {
        question: 'Can I reorder pages before merging?',
        answer: 'Yes. Drag the file thumbnails before clicking “Merge PDF” to set the final order.',
      },
      {
        question: 'Do I need to sign up?',
        answer: 'No. The merge tool is free to use anonymously, with no signup required.',
      },
    ],
    relatedSlugs: ['split-pdf', 'compress-pdf', 'rotate-pdf', 'pdf-to-jpg', 'add-page-numbers'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: {
      es: 'unir-pdf',
      fr: 'fusionner-pdf',
      de: 'pdf-zusammenfuegen',
      pt: 'juntar-pdf',
    },
    publishedAt: NOW,
    updatedAt: NOW,
  },
  {
    slug: 'split-pdf',
    category: 'organize',
    primaryKeyword: 'split pdf',
    h1: 'Split PDF Files Online',
    metaTitle: 'Split PDF — Extract Pages from PDF Online, Free',
    metaDescription:
      'Split a PDF into separate files or extract specific pages. Choose ranges or every Nth page. Files deleted in 60 minutes. Free, no signup.',
    ogTitle: 'Split PDF Files Online — Free & Private',
    ogDescription:
      'Extract pages or split a PDF into multiple files in seconds. No signup, files auto-deleted in 60 minutes.',
    ogImage: '/og/split-pdf.png',
    oneLineDefinition:
      'Splitting a PDF separates one document into multiple files, either by extracting specific page ranges or by splitting at fixed intervals.',
    longDescription:
      'Extract one or more pages from a PDF, or split a single document into multiple separate files. Choose page ranges, every Nth page, or each page as its own file. Original files are deleted within 60 minutes.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the PDF file you want to split.' },
      {
        name: 'Choose split mode',
        text: 'Pick page ranges, fixed intervals, or one file per page.',
      },
      {
        name: 'Split and download',
        text: 'Download your split files as a ZIP archive. Original files are deleted within 60 minutes.',
      },
    ],
    faqs: [
      {
        question: 'Can I extract just one page?',
        answer: 'Yes. Use “Extract single page” mode and pick the page number.',
      },
      {
        question: 'Does splitting reduce file quality?',
        answer: 'No. Page content is copied losslessly to the new files.',
      },
      {
        question: 'How are the split files delivered?',
        answer: 'As a ZIP archive containing each split file, downloadable for 15 minutes.',
      },
      {
        question: 'Can I split a password-protected PDF?',
        answer: 'Yes, after entering the password. We do not store the password.',
      },
      {
        question: 'What is the largest file I can split?',
        answer: '25 MB on free, 500 MB on Pro, and unlimited on Business tiers.',
      },
    ],
    relatedSlugs: ['merge-pdf', 'rotate-pdf', 'compress-pdf', 'extract-pdf-pages', 'pdf-to-jpg'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: {
      es: 'dividir-pdf',
      fr: 'diviser-pdf',
      de: 'pdf-aufteilen',
      pt: 'dividir-pdf',
    },
    publishedAt: NOW,
    updatedAt: NOW,
  },
  {
    slug: 'compress-pdf',
    category: 'optimize',
    primaryKeyword: 'compress pdf',
    h1: 'Compress PDF — Reduce PDF File Size',
    metaTitle: 'Compress PDF — Reduce PDF File Size Online, Free',
    metaDescription:
      'Compress PDF files online to reduce file size while preserving quality. Choose low, medium, or high compression. Files deleted in 60 minutes.',
    ogTitle: 'Compress PDF — Free Online PDF Size Reducer',
    ogDescription:
      'Reduce PDF file size for email, web, or archive. Three quality presets. Files auto-deleted in 60 minutes.',
    ogImage: '/og/compress-pdf.png',
    oneLineDefinition:
      'Compressing a PDF reduces its file size by re-encoding images and removing redundant data, often without visible loss of quality.',
    longDescription:
      'Shrink PDF file size to make it easier to email, upload, or archive. Choose a compression level that balances file size and visual quality. Files are deleted within 60 minutes.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the PDF file you want to compress.' },
      {
        name: 'Choose compression level',
        text: 'Pick low (best quality), medium (recommended), or high (smallest size).',
      },
      {
        name: 'Download compressed file',
        text: 'Download the optimized PDF. Original and result files are deleted within 60 minutes.',
      },
    ],
    faqs: [
      {
        question: 'How much smaller will my PDF be?',
        answer:
          'Typically 30–80 percent smaller, depending on the original content and compression level chosen.',
      },
      {
        question: 'Will compression lose quality?',
        answer:
          'Low compression is visually lossless. Medium and high compression re-encode images and may reduce visible detail.',
      },
      {
        question: 'Can I compress scanned PDFs?',
        answer: 'Yes. Scanned PDFs typically see the largest size reductions.',
      },
      {
        question: 'Is there a file size limit?',
        answer:
          'Free tier supports up to 25 MB. Pro and Business tiers raise this to 500 MB and unlimited respectively.',
      },
      {
        question: 'Are my files secure?',
        answer:
          'Yes. Files are encrypted in transit and at rest, and deleted automatically within 60 minutes.',
      },
    ],
    relatedSlugs: ['merge-pdf', 'split-pdf', 'pdf-to-jpg', 'rotate-pdf', 'add-watermark'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: {
      es: 'comprimir-pdf',
      fr: 'compresser-pdf',
      de: 'pdf-komprimieren',
      pt: 'comprimir-pdf',
    },
    publishedAt: NOW,
    updatedAt: NOW,
  },
  {
    slug: 'rotate-pdf',
    category: 'edit',
    primaryKeyword: 'rotate pdf',
    h1: 'Rotate PDF Pages Online',
    metaTitle: 'Rotate PDF — Rotate PDF Pages Online, Free',
    metaDescription:
      'Rotate one page, several pages, or every page in a PDF. Free, online, and secure. Files deleted in 60 minutes.',
    ogTitle: 'Rotate PDF — Free Online PDF Rotator',
    ogDescription:
      'Rotate PDF pages 90°, 180°, or 270°. Choose per-page or all-pages. No signup required.',
    ogImage: '/og/rotate-pdf.png',
    oneLineDefinition:
      'Rotating a PDF changes the on-screen orientation of one or more pages without altering the underlying page content.',
    longDescription:
      'Fix sideways or upside-down pages in your PDF. Rotate every page at once or pick specific pages and rotation angles. Saves orientation permanently to the file.',
    howToSteps: [
      {
        name: 'Upload your PDF',
        text: 'Drag and drop the file containing the pages you want to rotate.',
      },
      {
        name: 'Select pages and angle',
        text: 'Pick individual pages or all pages, then choose 90°, 180°, or 270°.',
      },
      {
        name: 'Save and download',
        text: 'Apply changes and download the rotated PDF. Files are deleted within 60 minutes.',
      },
    ],
    faqs: [
      {
        question: 'Can I rotate only some pages?',
        answer:
          'Yes. Click each thumbnail to rotate it individually, or use the “rotate all” button.',
      },
      {
        question: 'Does rotating change file size?',
        answer:
          'Rotation changes the page metadata but not page content, so file size stays nearly identical.',
      },
      {
        question: 'Will rotation affect text searchability?',
        answer: 'No. Text remains searchable in any rotation.',
      },
      {
        question: 'Can I rotate password-protected PDFs?',
        answer: 'Yes, after entering the password. The password is not stored.',
      },
      {
        question: 'Is the rotation permanent?',
        answer: 'Yes. Once applied and downloaded, the rotation is saved into the file.',
      },
    ],
    relatedSlugs: ['merge-pdf', 'split-pdf', 'compress-pdf', 'add-page-numbers', 'add-watermark'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'rotar-pdf', fr: 'pivoter-pdf', de: 'pdf-drehen', pt: 'rodar-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
  },
  {
    slug: 'jpg-to-pdf',
    category: 'convert-to-pdf',
    primaryKeyword: 'jpg to pdf',
    h1: 'JPG to PDF — Convert Images to PDF',
    metaTitle: 'JPG to PDF — Convert JPG, PNG, and HEIC to PDF Online',
    metaDescription:
      'Convert JPG, PNG, HEIC, and TIFF images to a single PDF. Batch convert, reorder pages, free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'JPG to PDF — Free Online Image to PDF Converter',
    ogDescription:
      'Combine multiple images into a single PDF in seconds. Drag-to-reorder, free, no signup.',
    ogImage: '/og/jpg-to-pdf.png',
    oneLineDefinition:
      'JPG to PDF conversion wraps one or more JPG, PNG, HEIC, or TIFF images into a single PDF document, one image per page.',
    longDescription:
      'Combine photos, scans, screenshots, and graphics into a single PDF. Reorder images, set page orientation, and adjust margins. Each image becomes its own page.',
    howToSteps: [
      {
        name: 'Upload your images',
        text: 'Drag and drop one or more JPG, PNG, HEIC, or TIFF files.',
      },
      {
        name: 'Reorder and set layout',
        text: 'Drag thumbnails to reorder, then pick orientation and margins.',
      },
      {
        name: 'Convert and download',
        text: 'Convert to PDF and download. Files are deleted within 60 minutes.',
      },
    ],
    faqs: [
      {
        question: 'Which image formats are supported?',
        answer: 'JPG, JPEG, PNG, HEIC, HEIF, WEBP, and TIFF.',
      },
      {
        question: 'Can I put multiple images on one page?',
        answer: 'Yes. Use the “grid” layout option to place 2, 4, or 6 images per page.',
      },
      {
        question: 'Will image quality be preserved?',
        answer:
          'Yes. Images are embedded at their original resolution unless you choose a compression level.',
      },
      {
        question: 'Is there a maximum number of images?',
        answer:
          'Free users can convert up to 30 images per job. Pro users have no per-job image limit.',
      },
      {
        question: 'Can I convert HEIC photos from my iPhone?',
        answer: 'Yes. HEIC and HEIF files are supported and decoded server-side.',
      },
    ],
    relatedSlugs: ['pdf-to-jpg', 'merge-pdf', 'compress-pdf', 'word-to-pdf', 'add-watermark'],
    acceptedInputMimeTypes: [
      'image/jpeg',
      'image/png',
      'image/heic',
      'image/heif',
      'image/webp',
      'image/tiff',
    ],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'jpg-a-pdf', fr: 'jpg-en-pdf', de: 'jpg-zu-pdf', pt: 'jpg-para-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
  },
  {
    slug: 'pdf-to-jpg',
    category: 'convert-from-pdf',
    primaryKeyword: 'pdf to jpg',
    h1: 'PDF to JPG — Convert PDF Pages to Images',
    metaTitle: 'PDF to JPG — Convert PDF to JPG Online, Free',
    metaDescription:
      'Convert each PDF page to a high-quality JPG image. Choose DPI and quality. Free, no signup, files deleted in 60 minutes.',
    ogTitle: 'PDF to JPG — Free Online PDF to Image Converter',
    ogDescription:
      'Export each page of a PDF as a JPG image. Pick resolution and quality. No signup required.',
    ogImage: '/og/pdf-to-jpg.png',
    oneLineDefinition:
      'PDF to JPG conversion exports each page of a PDF document as a separate JPG image at a chosen resolution.',
    longDescription:
      'Turn each page of a PDF into a sharable JPG image. Choose a resolution between 72 and 600 DPI to balance quality and file size. Output is delivered as a ZIP archive.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the PDF file you want to convert.' },
      { name: 'Choose quality', text: 'Pick a resolution and JPG quality level.' },
      {
        name: 'Convert and download',
        text: 'Download the ZIP archive containing one JPG per page.',
      },
    ],
    faqs: [
      {
        question: 'What resolution should I pick?',
        answer:
          '150 DPI is fine for sharing on screen. 300 DPI is recommended for printing or zooming in.',
      },
      {
        question: 'Can I export just one page?',
        answer: 'Yes. Pick a single page range before converting.',
      },
      {
        question: 'Why JPG instead of PNG?',
        answer:
          'JPG produces much smaller files for photographic content. Use “PDF to PNG” instead if you need lossless quality or transparency.',
      },
      {
        question: 'Are scanned PDFs supported?',
        answer: 'Yes. Each scanned page becomes a JPG at the chosen resolution.',
      },
      {
        question: 'Will text be searchable?',
        answer:
          'No. JPG is an image format. To keep text searchable, use “PDF to Word” or “PDF to Markdown”.',
      },
    ],
    relatedSlugs: ['jpg-to-pdf', 'compress-pdf', 'merge-pdf', 'pdf-to-png', 'pdf-to-word'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'image/jpeg',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'pdf-a-jpg', fr: 'pdf-en-jpg', de: 'pdf-zu-jpg', pt: 'pdf-para-jpg' },
    publishedAt: NOW,
    updatedAt: NOW,
  },
  {
    slug: 'word-to-pdf',
    category: 'convert-to-pdf',
    primaryKeyword: 'word to pdf',
    h1: 'Word to PDF — Convert DOCX to PDF',
    metaTitle: 'Word to PDF — Convert DOCX to PDF Online, Free',
    metaDescription:
      'Convert Microsoft Word documents to PDF online with layout, fonts, and images preserved. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'Word to PDF — Free Online DOCX to PDF Converter',
    ogDescription:
      'Convert DOCX or DOC files to PDF in seconds. Layout-preserving, free, no signup.',
    ogImage: '/og/word-to-pdf.png',
    oneLineDefinition:
      'Word to PDF conversion renders a Microsoft Word document (DOCX or DOC) into a PDF while preserving layout, fonts, and embedded media.',
    longDescription:
      'Convert DOCX or DOC files to PDF while preserving the original layout, embedded fonts, images, headers, footers, and page numbers. Output is a standards-compliant PDF/A-compatible document.',
    howToSteps: [
      { name: 'Upload your Word document', text: 'Drag and drop a DOCX or DOC file.' },
      {
        name: 'Pick options',
        text: 'Choose page size, embed fonts, and PDF/A compatibility if needed.',
      },
      {
        name: 'Convert and download',
        text: 'Download the converted PDF. Files are deleted within 60 minutes.',
      },
    ],
    faqs: [
      {
        question: 'Does it support DOC and DOCX?',
        answer: 'Yes. Both legacy DOC and modern DOCX formats are supported.',
      },
      {
        question: 'Will my fonts be preserved?',
        answer:
          'Yes. Fonts are embedded in the resulting PDF for consistent rendering on any device.',
      },
      {
        question: 'Can I convert documents with images or tables?',
        answer: 'Yes. Tables, images, headers, footers, and footnotes are all preserved.',
      },
      {
        question: 'Can I convert to PDF/A for archival?',
        answer: 'Yes. Toggle “PDF/A compatible” before converting.',
      },
      {
        question: 'Are my files private?',
        answer:
          'Yes. Files are encrypted in transit and at rest, and deleted automatically within 60 minutes.',
      },
    ],
    relatedSlugs: ['pdf-to-word', 'jpg-to-pdf', 'merge-pdf', 'compress-pdf', 'add-watermark'],
    acceptedInputMimeTypes: [
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      'application/msword',
    ],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'word-a-pdf', fr: 'word-en-pdf', de: 'word-zu-pdf', pt: 'word-para-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
  },
  {
    slug: 'pdf-to-word',
    category: 'convert-from-pdf',
    primaryKeyword: 'pdf to word',
    h1: 'PDF to Word — Convert PDF to Editable DOCX',
    metaTitle: 'PDF to Word — Convert PDF to DOCX Online, Free',
    metaDescription:
      'Convert PDF to editable Microsoft Word (DOCX) with layout preserved. Free, no signup, files deleted in 60 minutes.',
    ogTitle: 'PDF to Word — Free Online PDF to DOCX Converter',
    ogDescription:
      'Turn PDFs into editable Word documents. Layout-preserving conversion. Free, no signup.',
    ogImage: '/og/pdf-to-word.png',
    oneLineDefinition:
      'PDF to Word conversion transforms a PDF into an editable DOCX file, reconstructing paragraphs, tables, and images.',
    longDescription:
      'Convert PDFs into editable Microsoft Word documents. Our converter preserves paragraph flow, headings, tables, and images, and OCRs scanned pages so you can edit the text.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the PDF you want to convert.' },
      {
        name: 'Optionally enable OCR',
        text: 'If your PDF is scanned, enable OCR to extract editable text.',
      },
      {
        name: 'Convert and download',
        text: 'Download the editable DOCX. Files are deleted within 60 minutes.',
      },
    ],
    faqs: [
      {
        question: 'Does it work with scanned PDFs?',
        answer: 'Yes. Enable OCR before converting to extract editable text from scans.',
      },
      {
        question: 'Will tables and images be preserved?',
        answer: 'Yes. Tables and images are reconstructed in the DOCX output.',
      },
      {
        question: 'Which Word versions are supported?',
        answer:
          'Output is DOCX, compatible with Word 2007 and newer, Google Docs, and LibreOffice Writer.',
      },
      {
        question: 'Is the conversion accurate?',
        answer: 'Highly accurate for born-digital PDFs. Scanned PDFs depend on OCR quality.',
      },
      {
        question: 'Are my files secure?',
        answer: 'Yes. Files are encrypted and deleted automatically within 60 minutes.',
      },
    ],
    relatedSlugs: ['word-to-pdf', 'pdf-to-jpg', 'pdf-to-markdown', 'merge-pdf', 'compress-pdf'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'pdf-a-word', fr: 'pdf-en-word', de: 'pdf-zu-word', pt: 'pdf-para-word' },
    publishedAt: NOW,
    updatedAt: NOW,
  },
  {
    slug: 'add-page-numbers',
    category: 'edit',
    primaryKeyword: 'add page numbers to pdf',
    h1: 'Add Page Numbers to PDF',
    metaTitle: 'Add Page Numbers to PDF — Free Online Tool',
    metaDescription:
      'Add page numbers to PDF files online. Choose position, style, font, and starting number. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'Add Page Numbers to PDF — Free Online Tool',
    ogDescription:
      'Insert page numbers in any position with custom font, size, and starting number. Free, no signup.',
    ogImage: '/og/add-page-numbers.png',
    oneLineDefinition:
      'Adding page numbers to a PDF overlays sequential numbers on each page in a chosen position, style, and starting value.',
    longDescription:
      'Number the pages of any PDF in seconds. Pick header or footer, choose alignment, font, and size, set a starting number, and optionally skip the first N pages (e.g., cover and TOC).',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the file you want to number.' },
      {
        name: 'Configure numbering',
        text: 'Pick position, style, font, size, and starting number.',
      },
      {
        name: 'Apply and download',
        text: 'Download the numbered PDF. Files are deleted within 60 minutes.',
      },
    ],
    faqs: [
      {
        question: 'Can I skip the first few pages?',
        answer: 'Yes. Set the starting page so cover and TOC pages remain unnumbered.',
      },
      {
        question: 'Can I use Roman numerals?',
        answer: 'Yes. Choose Arabic (1, 2, 3) or Roman (i, ii, iii) numeral styles.',
      },
      {
        question: 'What positions are available?',
        answer: 'Top-left, top-center, top-right, bottom-left, bottom-center, and bottom-right.',
      },
      {
        question: 'Will it affect existing page content?',
        answer: 'No. Page numbers are added as a new layer; existing content is untouched.',
      },
      {
        question: 'Can I number a password-protected PDF?',
        answer: 'Yes, after providing the password, which is never stored.',
      },
    ],
    relatedSlugs: ['add-watermark', 'rotate-pdf', 'merge-pdf', 'split-pdf', 'compress-pdf'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: {
      es: 'numerar-paginas-pdf',
      fr: 'numeroter-pdf',
      de: 'pdf-seitenzahlen',
      pt: 'numerar-paginas-pdf',
    },
    publishedAt: NOW,
    updatedAt: NOW,
  },
  {
    slug: 'add-watermark',
    category: 'edit',
    primaryKeyword: 'add watermark to pdf',
    h1: 'Add Watermark to PDF',
    metaTitle: 'Add Watermark to PDF — Free Online Tool',
    metaDescription:
      'Watermark PDFs with text or images. Set opacity, rotation, position, and pages. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'Add Watermark to PDF — Free Online Tool',
    ogDescription:
      'Add text or image watermarks to any PDF. Configure opacity, rotation, and which pages to mark.',
    ogImage: '/og/add-watermark.png',
    oneLineDefinition:
      'Adding a watermark to a PDF overlays text or an image on every selected page to indicate ownership, status, or confidentiality.',
    longDescription:
      'Apply a text or image watermark across some or all pages of a PDF. Control opacity, rotation, position, and which pages get watermarked. Useful for drafts, confidentiality marks, and ownership stamps.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the file you want to watermark.' },
      {
        name: 'Set your watermark',
        text: 'Type text or upload an image. Choose opacity, rotation, and position.',
      },
      {
        name: 'Apply and download',
        text: 'Download the watermarked PDF. Files are deleted within 60 minutes.',
      },
    ],
    faqs: [
      {
        question: 'Text or image watermarks?',
        answer: 'Both. Pick text and configure font/color, or upload a PNG/JPG image.',
      },
      {
        question: 'Can I watermark only some pages?',
        answer: 'Yes. Specify a page range or pick odd/even/all.',
      },
      {
        question: 'Is the watermark permanent?',
        answer: 'It is embedded in the new file. The original file is unchanged on your device.',
      },
      {
        question: 'Can I rotate the watermark?',
        answer: 'Yes. Set any rotation between -90° and 90°.',
      },
      {
        question: 'Does it support transparent PNGs?',
        answer: 'Yes. Transparent PNG watermarks are supported and preserved.',
      },
    ],
    relatedSlugs: ['add-page-numbers', 'rotate-pdf', 'merge-pdf', 'compress-pdf', 'protect-pdf'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: {
      es: 'marca-de-agua-pdf',
      fr: 'filigrane-pdf',
      de: 'pdf-wasserzeichen',
      pt: 'marca-d-agua-pdf',
    },
    publishedAt: NOW,
    updatedAt: NOW,
  },
] as const satisfies ReadonlyArray<SeoTool>;

export function getToolBySlug(slug: string): SeoTool | undefined {
  return TOOL_SEED.find((t) => t.slug === slug);
}

export function getRelatedTools(slugs: readonly string[]): SeoTool[] {
  return slugs.map(getToolBySlug).filter((t): t is SeoTool => t !== undefined);
}
