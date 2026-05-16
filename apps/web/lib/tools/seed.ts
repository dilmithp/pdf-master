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
    op: 'merge',
    acceptMimes: ['application/pdf'],
    minFiles: 2,
    maxFiles: 20,
    maxBytes: 50 * 1024 * 1024,
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
    op: 'split',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
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
    op: 'compress',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 10,
    maxBytes: 50 * 1024 * 1024,
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
    op: 'rotate',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 10,
    maxBytes: 50 * 1024 * 1024,
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
    op: 'jpg-to-pdf',
    acceptMimes: ['image/jpeg', 'image/png', 'image/heic', 'image/heif', 'image/webp', 'image/tiff'],
    minFiles: 1,
    maxFiles: 100,
    maxBytes: 50 * 1024 * 1024,
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
    op: 'pdf-to-jpg',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 10,
    maxBytes: 50 * 1024 * 1024,
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
    op: 'word-to-pdf',
    acceptMimes: [
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      'application/msword',
    ],
    minFiles: 1,
    maxFiles: 10,
    maxBytes: 50 * 1024 * 1024,
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
    op: 'pdf-to-word',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 10,
    maxBytes: 50 * 1024 * 1024,
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
    op: 'add-page-numbers',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 10,
    maxBytes: 50 * 1024 * 1024,
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
    op: 'add-watermark',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 10,
    maxBytes: 50 * 1024 * 1024,
  },
  // --- 15 additional tools ---
  {
    slug: 'pdf-to-pdfa',
    category: 'convert-from-pdf',
    primaryKeyword: 'pdf to pdf/a',
    h1: 'Convert PDF to PDF/A — Archival-Compliant Format',
    metaTitle: 'PDF to PDF/A — Convert PDF to ISO Archival Standard Online',
    metaDescription:
      'Convert PDF files to PDF/A-1b, PDF/A-2b, or PDF/A-3b for long-term archival. Embed fonts, remove encryption. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'PDF to PDF/A — Free Online Archival PDF Converter',
    ogDescription:
      'Create ISO-compliant PDF/A files for long-term archival. Fonts embedded, encryption removed. No signup.',
    ogImage: '/og/pdf-to-pdfa.png',
    oneLineDefinition:
      'PDF/A conversion produces an ISO 19005-compliant file with all fonts embedded and external dependencies removed, ensuring documents remain readable indefinitely.',
    longDescription:
      'Government agencies, law firms, and records managers require PDF/A for long-term document preservation. Our tool converts any PDF to PDF/A-1b, PDF/A-2b, or PDF/A-3b, embedding all fonts, removing encryption, and stripping external references — all server-side, with files deleted within 60 minutes.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the file you want to archive.' },
      { name: 'Choose PDF/A level', text: 'Select PDF/A-1b, PDF/A-2b, or PDF/A-3b based on your compliance requirement.' },
      { name: 'Convert and download', text: 'Download the archival PDF. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'What is the difference between PDF/A-1b and PDF/A-2b?', answer: 'PDF/A-1b is based on PDF 1.4 and is the most widely accepted. PDF/A-2b adds support for JPEG 2000, layers, and embedded files (PDF 1.7 base).' },
      { question: 'Will encryption be removed?', answer: 'Yes. PDF/A prohibits encryption, so it is automatically removed during conversion.' },
      { question: 'Are all fonts embedded automatically?', answer: 'Yes. All font subsets are embedded, including those referenced from system fonts.' },
      { question: 'Can I verify the output is compliant?', answer: 'Yes. We validate the output with an embedded verifier and report any remaining issues.' },
      { question: 'Is this accepted by courts and government agencies?', answer: 'PDF/A-1b is the most universally accepted format for legal and government submissions. Check with the receiving body for their specific requirement.' },
    ],
    relatedSlugs: ['word-to-pdf', 'merge-pdf', 'compress-pdf', 'protect-pdf', 'redact-pdf'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'pdf-a-pdfa', fr: 'pdf-en-pdfa', de: 'pdf-zu-pdfa', pt: 'pdf-para-pdfa' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'pdf-to-pdfa',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'redact-pdf',
    category: 'security',
    primaryKeyword: 'redact pdf',
    h1: 'Redact PDF — Permanently Remove Sensitive Text and Images',
    metaTitle: 'Redact PDF — Permanently Black Out Sensitive Information Online',
    metaDescription:
      'Permanently redact text, images, and metadata from PDF files. True redaction — not just black boxes over text. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'Redact PDF — Permanent, Secure PDF Redaction Tool',
    ogDescription:
      'Black out names, SSNs, financial data, and images permanently. True redaction, not cosmetic. No signup.',
    ogImage: '/og/redact-pdf.png',
    oneLineDefinition:
      'PDF redaction permanently removes selected text, images, and metadata from a PDF so the underlying data cannot be recovered — unlike overlaying a black rectangle.',
    longDescription:
      'True redaction overwrites the underlying PDF content stream — it cannot be reversed by removing a visual overlay. Select text strings, regex patterns, page areas, or images to redact. Metadata (author, creation date, custom properties) is also stripped on request. Essential for FOIA responses, legal discovery, and HIPAA compliance.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the document containing sensitive content.' },
      { name: 'Mark content for redaction', text: 'Click to select text, draw rectangles over images, or enter regex patterns for bulk redaction.' },
      { name: 'Apply and download', text: 'Apply redactions permanently and download the sanitized PDF. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'Is the redacted content truly unrecoverable?', answer: 'Yes. We overwrite the PDF content stream at the selected positions, not just place a visual overlay. The original bytes are gone.' },
      { question: 'Can I redact by keyword or pattern?', answer: 'Yes. Enter a search term or regex pattern (e.g. SSN, phone number) to redact all matching occurrences at once.' },
      { question: 'Does redaction remove metadata?', answer: 'Yes. Toggle "strip metadata" to remove author, creation date, and any custom XMP properties.' },
      { question: 'Can I redact scanned PDFs?', answer: 'Yes. Enable OCR before redacting so text in scanned pages is recognized and matched.' },
      { question: 'Is this compliant for legal discovery?', answer: 'Our redaction is forensically sound. Always consult your legal counsel for specific jurisdiction requirements.' },
    ],
    relatedSlugs: ['protect-pdf', 'pdf-to-pdfa', 'compress-pdf', 'add-watermark', 'merge-pdf'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'redactar-pdf', fr: 'biffer-pdf', de: 'pdf-schwaerzen', pt: 'redigir-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'redact',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'compare-pdf',
    category: 'ai',
    primaryKeyword: 'compare pdf files',
    h1: 'Compare PDF Files — Highlight Differences Between Two Documents',
    metaTitle: 'Compare PDF Files — Find Differences Online, Free',
    metaDescription:
      'Compare two PDF files and highlight added, removed, and changed text. Side-by-side diff view. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'Compare PDF Files — Free Online PDF Diff Tool',
    ogDescription:
      'Spot every change between two PDF versions. Color-coded diff, side-by-side view. No signup.',
    ogImage: '/og/compare-pdf.png',
    oneLineDefinition:
      'PDF comparison analyzes two PDF documents side-by-side and highlights insertions, deletions, and formatting changes between them.',
    longDescription:
      'See exactly what changed between two PDF versions — useful for contract redlines, regulatory submissions, and document version control. Added content appears in green, removed content in red, and moved blocks are indicated separately. Download a marked-up PDF or a plain-text diff report.',
    howToSteps: [
      { name: 'Upload the original PDF', text: 'Drag and drop the baseline document.' },
      { name: 'Upload the revised PDF', text: 'Add the newer version you want to compare against.' },
      { name: 'Review and download', text: 'Browse the color-coded diff and download the marked-up report.' },
    ],
    faqs: [
      { question: 'Does it compare scanned PDFs?', answer: 'Yes. OCR is applied automatically to scanned pages before comparison.' },
      { question: 'Can I compare PDFs with different page counts?', answer: 'Yes. Added and removed pages are flagged in the diff.' },
      { question: 'What output formats are available?', answer: 'A marked-up PDF showing inline changes, or a plain-text change report.' },
      { question: 'Does it detect moved paragraphs?', answer: 'Yes. Moved blocks are identified separately from pure additions and deletions.' },
      { question: 'Is my content sent to a third-party AI?', answer: 'No. Comparison is performed on our own infrastructure. Your content is never sent to external AI providers.' },
    ],
    relatedSlugs: ['merge-pdf', 'split-pdf', 'redact-pdf', 'pdf-to-word', 'add-watermark'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'comparar-pdf', fr: 'comparer-pdf', de: 'pdf-vergleichen', pt: 'comparar-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'compare',
    acceptMimes: ['application/pdf'],
    minFiles: 2,
    maxFiles: 2,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'e-sign-pdf',
    category: 'security',
    primaryKeyword: 'sign pdf online',
    h1: 'Sign PDF Online — Add Electronic Signature to PDF',
    metaTitle: 'Sign PDF Online — Electronic Signature, Free & Legally Binding',
    metaDescription:
      'Add a legally binding electronic signature to a PDF online. Draw, type, or upload your signature. Free, no signup required. Files deleted in 60 minutes.',
    ogTitle: 'Sign PDF Online — Free Electronic Signature Tool',
    ogDescription:
      'Draw, type, or upload your signature and apply it to any PDF. eIDAS and ESIGN Act compliant. No signup.',
    ogImage: '/og/e-sign-pdf.png',
    oneLineDefinition:
      'An electronic PDF signature is a legally recognized mark applied to a PDF document to indicate agreement, replacing a handwritten signature.',
    longDescription:
      'Sign any PDF document electronically without printing. Draw your signature on a touchscreen, type it in a handwriting font, or upload an image of your wet signature. Position the signature anywhere on the page, add a date stamp, and download the signed document. Signatures meet eIDAS (EU) and ESIGN Act (US) legal standards for simple electronic signatures.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the PDF you need to sign.' },
      { name: 'Create your signature', text: 'Draw with a mouse or finger, type your name, or upload a signature image.' },
      { name: 'Position and download', text: 'Drag the signature to the correct location, resize if needed, and download the signed PDF.' },
    ],
    faqs: [
      { question: 'Is an electronic signature legally binding?', answer: 'Yes. Simple electronic signatures are recognized under the EU eIDAS regulation and the US ESIGN Act for most commercial documents.' },
      { question: 'Can I sign on a phone or tablet?', answer: 'Yes. Draw your signature with your finger on any touch-enabled device.' },
      { question: 'Can I add a date stamp?', answer: 'Yes. Enable "add date" and it is automatically placed next to your signature.' },
      { question: 'Can multiple people sign the same document?', answer: 'Yes. Pro users can send documents to multiple signers and track completion status.' },
      { question: 'Is the signed document tamper-evident?', answer: 'Yes. A cryptographic hash of the signed document is embedded so any alteration after signing is detectable.' },
    ],
    relatedSlugs: ['protect-pdf', 'redact-pdf', 'add-watermark', 'merge-pdf', 'pdf-to-pdfa'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'firmar-pdf', fr: 'signer-pdf', de: 'pdf-unterschreiben', pt: 'assinar-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'esign',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'pdf-to-html',
    category: 'convert-from-pdf',
    primaryKeyword: 'pdf to html',
    h1: 'PDF to HTML — Convert PDF to Web Page',
    metaTitle: 'PDF to HTML — Convert PDF to HTML Online, Free',
    metaDescription:
      'Convert PDF files to HTML web pages while preserving layout, text, and images. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'PDF to HTML — Free Online PDF to Web Page Converter',
    ogDescription:
      'Turn PDFs into searchable, responsive HTML pages. Headings, tables, and images preserved. No signup.',
    ogImage: '/og/pdf-to-html.png',
    oneLineDefinition:
      'PDF to HTML conversion transforms a PDF document into an HTML page with semantic markup, enabling web publishing and search engine indexing.',
    longDescription:
      'Publish PDF content on the web without requiring readers to download a file. Our converter extracts paragraphs, headings, tables, and images from the PDF and maps them to semantic HTML5 elements. The resulting HTML is responsive and can be embedded directly into any web page or CMS.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the PDF you want to convert to HTML.' },
      { name: 'Choose output options', text: 'Select whether to embed images inline (base64) or export them as separate files.' },
      { name: 'Download HTML', text: 'Download a ZIP containing the HTML file and any image assets.' },
    ],
    faqs: [
      { question: 'Are images preserved?', answer: 'Yes. Images are extracted and included either inline as base64 or as separate PNG/JPG files in the ZIP archive.' },
      { question: 'Is the HTML responsive?', answer: 'Yes. The output uses relative widths and a simple mobile-friendly layout.' },
      { question: 'Can I convert scanned PDFs?', answer: 'Yes. OCR is applied first, and the recognized text is used for the HTML output.' },
      { question: 'Will my PDF hyperlinks become HTML links?', answer: 'Yes. Internal and external hyperlinks are converted to HTML anchor elements.' },
      { question: 'What about complex multi-column layouts?', answer: 'Multi-column layouts are linearized into single-column HTML for maximum readability and compatibility.' },
    ],
    relatedSlugs: ['pdf-to-word', 'html-to-pdf', 'compress-pdf', 'pdf-to-jpg', 'merge-pdf'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'text/html',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'pdf-a-html', fr: 'pdf-en-html', de: 'pdf-zu-html', pt: 'pdf-para-html' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'pdf-to-html',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'html-to-pdf',
    category: 'convert-to-pdf',
    primaryKeyword: 'html to pdf',
    h1: 'HTML to PDF — Convert Web Pages to PDF',
    metaTitle: 'HTML to PDF — Convert Web Pages and HTML Files to PDF Online',
    metaDescription:
      'Convert HTML files or paste HTML code to produce a pixel-perfect PDF. Supports CSS, web fonts, and media queries. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'HTML to PDF — Free Online HTML and Web Page to PDF Converter',
    ogDescription:
      'Paste HTML or upload a file and get a print-ready PDF with CSS, fonts, and images intact. No signup.',
    ogImage: '/og/html-to-pdf.png',
    oneLineDefinition:
      'HTML to PDF conversion renders an HTML document — including CSS, web fonts, and JavaScript-generated content — into a printable, shareable PDF file.',
    longDescription:
      'Produce print-ready PDFs directly from HTML: paste a snippet, upload a file, or enter a URL. Our headless-browser renderer handles CSS Grid, Flexbox, web fonts, SVG, and even CSS print media queries — so the PDF looks exactly as it would in a browser. Ideal for invoices, reports, certificates, and any programmatic document generation workflow.',
    howToSteps: [
      { name: 'Provide your HTML', text: 'Paste HTML code, upload an .html file, or enter a public URL.' },
      { name: 'Set page options', text: 'Choose paper size (A4, Letter, Legal), margins, and orientation.' },
      { name: 'Convert and download', text: 'Download the rendered PDF. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'Does it support external CSS and fonts?', answer: 'Yes. External stylesheets and web fonts are fetched during rendering. Self-host fonts for reliable results.' },
      { question: 'Can I convert a live web page by URL?', answer: 'Yes. Enter a publicly accessible URL and the page is fetched and rendered to PDF.' },
      { question: 'Is JavaScript executed?', answer: 'Yes. A headless browser executes JavaScript before rendering, so dynamic content is captured.' },
      { question: 'What page sizes are supported?', answer: 'A4, A3, Letter, Legal, Tabloid, and custom dimensions in mm or inches.' },
      { question: 'Can I add headers and footers?', answer: 'Yes. Define header/footer HTML with {pageNumber} and {totalPages} placeholders.' },
    ],
    relatedSlugs: ['pdf-to-html', 'word-to-pdf', 'compress-pdf', 'merge-pdf', 'add-watermark'],
    acceptedInputMimeTypes: ['text/html'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'html-a-pdf', fr: 'html-en-pdf', de: 'html-zu-pdf', pt: 'html-para-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'html-to-pdf',
    acceptMimes: ['text/html'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 10 * 1024 * 1024,
  },
  {
    slug: 'excel-to-pdf',
    category: 'convert-to-pdf',
    primaryKeyword: 'excel to pdf',
    h1: 'Excel to PDF — Convert Spreadsheets to PDF',
    metaTitle: 'Excel to PDF — Convert XLSX to PDF Online, Free',
    metaDescription:
      'Convert Excel spreadsheets (XLSX, XLS, CSV) to PDF. Preserve formatting, formulas display, and charts. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'Excel to PDF — Free Online Spreadsheet to PDF Converter',
    ogDescription:
      'Convert XLSX or XLS to PDF with charts and formatting preserved. No signup required.',
    ogImage: '/og/excel-to-pdf.png',
    oneLineDefinition:
      'Excel to PDF conversion renders a spreadsheet into a PDF document, preserving formatting, charts, and calculated values without requiring Microsoft Office.',
    longDescription:
      'Share spreadsheets in a format that anyone can view without Microsoft Excel. Convert XLSX, XLS, and CSV files to PDF with cell formatting, borders, conditional formatting, and embedded charts all preserved. Choose which sheets to include and set the page orientation and scaling.',
    howToSteps: [
      { name: 'Upload your spreadsheet', text: 'Drag and drop an XLSX, XLS, or CSV file.' },
      { name: 'Choose sheets and layout', text: 'Select which sheets to include, set orientation (landscape/portrait), and scale to fit.' },
      { name: 'Convert and download', text: 'Download the PDF. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'Does it preserve charts and graphs?', answer: 'Yes. Charts embedded in the spreadsheet are rendered accurately in the PDF.' },
      { question: 'Can I convert just one sheet?', answer: 'Yes. Select specific sheets before converting.' },
      { question: 'Will formulas be shown or evaluated?', answer: 'Formula results (values) are shown, not the formula expressions themselves.' },
      { question: 'Is CSV supported?', answer: 'Yes. CSV files are converted using a default table layout.' },
      { question: 'What is the maximum spreadsheet size?', answer: 'Free tier: 25 MB and up to 10,000 rows. Pro removes these limits.' },
    ],
    relatedSlugs: ['word-to-pdf', 'pptx-to-pdf', 'compress-pdf', 'merge-pdf', 'pdf-to-word'],
    acceptedInputMimeTypes: [
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      'application/vnd.ms-excel',
      'text/csv',
    ],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'excel-a-pdf', fr: 'excel-en-pdf', de: 'excel-zu-pdf', pt: 'excel-para-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'excel-to-pdf',
    acceptMimes: [
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      'application/vnd.ms-excel',
      'text/csv',
    ],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'pptx-to-pdf',
    category: 'convert-to-pdf',
    primaryKeyword: 'pptx to pdf',
    h1: 'PowerPoint to PDF — Convert PPTX to PDF Online',
    metaTitle: 'PowerPoint to PDF — Convert PPTX to PDF Online, Free',
    metaDescription:
      'Convert PowerPoint presentations (PPTX, PPT) to PDF. Each slide becomes a page, animations flattened. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'PowerPoint to PDF — Free Online PPTX to PDF Converter',
    ogDescription:
      'Convert PPTX presentations to PDF with fonts and images preserved. One slide per page. No signup.',
    ogImage: '/og/pptx-to-pdf.png',
    oneLineDefinition:
      'PowerPoint to PDF conversion renders each presentation slide as a PDF page, preserving fonts, images, and layout without requiring PowerPoint.',
    longDescription:
      'Share presentations with anyone, on any device, without needing PowerPoint installed. Convert PPTX or PPT files to PDF with all slide content, embedded fonts, and images preserved. Animations are flattened to their final state. Optionally include speaker notes as a separate page per slide.',
    howToSteps: [
      { name: 'Upload your presentation', text: 'Drag and drop a PPTX or PPT file.' },
      { name: 'Choose options', text: 'Optionally include speaker notes and select slides to export.' },
      { name: 'Convert and download', text: 'Download the PDF. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'Are embedded videos preserved?', answer: 'Videos are replaced with their poster frame (first frame) in the PDF.' },
      { question: 'Can I include speaker notes?', answer: 'Yes. Toggle "include notes" to add a notes page after each slide.' },
      { question: 'Does it support themes and custom fonts?', answer: 'Yes. Embedded fonts are included in the PDF output.' },
      { question: 'What is the maximum file size?', answer: '25 MB on free tier, 500 MB on Pro.' },
      { question: 'Can I convert only selected slides?', answer: 'Yes. Specify a slide range before converting.' },
    ],
    relatedSlugs: ['word-to-pdf', 'excel-to-pdf', 'compress-pdf', 'merge-pdf', 'jpg-to-pdf'],
    acceptedInputMimeTypes: [
      'application/vnd.openxmlformats-officedocument.presentationml.presentation',
      'application/vnd.ms-powerpoint',
    ],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'pptx-a-pdf', fr: 'pptx-en-pdf', de: 'pptx-zu-pdf', pt: 'pptx-para-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'pptx-to-pdf',
    acceptMimes: [
      'application/vnd.openxmlformats-officedocument.presentationml.presentation',
      'application/vnd.ms-powerpoint',
    ],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'crop-pdf',
    category: 'edit',
    primaryKeyword: 'crop pdf',
    h1: 'Crop PDF Pages Online',
    metaTitle: 'Crop PDF — Remove Margins and Crop PDF Pages Online, Free',
    metaDescription:
      'Crop PDF pages to remove white margins, trim content, or change page dimensions. Apply to one or all pages. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'Crop PDF Pages — Free Online PDF Cropping Tool',
    ogDescription:
      'Remove margins or crop any region from PDF pages. Apply to one page or all pages at once. No signup.',
    ogImage: '/og/crop-pdf.png',
    oneLineDefinition:
      'PDF cropping adjusts the visible crop box of a PDF page, effectively trimming away unwanted margins or borders without altering the underlying page content.',
    longDescription:
      'Remove oversized margins from scanned documents, trim presentation slides, or extract a specific region from a PDF page. Set crop margins in millimeters or drag handles on the preview. Apply the same crop to every page or configure each page individually.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the file whose pages you want to crop.' },
      { name: 'Set the crop area', text: 'Drag the handles on the preview or enter precise margin values.' },
      { name: 'Apply and download', text: 'Download the cropped PDF. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'Does cropping remove hidden content?', answer: 'Cropping adjusts the visible area (CropBox) but the underlying page content outside the crop is still in the file. Use redaction to permanently remove content.' },
      { question: 'Can I crop different pages differently?', answer: 'Yes. Switch between per-page and apply-to-all modes in the editor.' },
      { question: 'Will cropping change the file size?', answer: 'Minimally. The content outside the CropBox remains in the file but is hidden.' },
      { question: 'Can I crop to an exact page size?', answer: 'Yes. Choose a preset (A4, Letter, etc.) or enter custom dimensions.' },
      { question: 'Is cropping reversible?', answer: 'Only from the original file. Once downloaded and the original discarded, the crop is effectively permanent.' },
    ],
    relatedSlugs: ['rotate-pdf', 'resize-pdf', 'split-pdf', 'compress-pdf', 'add-watermark'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'recortar-pdf', fr: 'rogner-pdf', de: 'pdf-zuschneiden', pt: 'cortar-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'crop',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'resize-pdf',
    category: 'edit',
    primaryKeyword: 'resize pdf',
    h1: 'Resize PDF Pages — Change PDF Page Size',
    metaTitle: 'Resize PDF — Change PDF Page Size Online, Free',
    metaDescription:
      'Resize PDF pages to A4, Letter, Legal, or any custom size. Scale or pad content to fit. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'Resize PDF Pages — Free Online PDF Page Size Changer',
    ogDescription:
      'Scale PDF pages to A4, Letter, Legal, or custom dimensions. Content scaled or padded. No signup.',
    ogImage: '/og/resize-pdf.png',
    oneLineDefinition:
      'PDF resizing changes the physical dimensions of every page in a document to a target paper size, scaling or padding the content to fit.',
    longDescription:
      'Change PDF pages from one paper size to another — for example, convert a US Letter document to A4 for European printing, or scale oversized engineering drawings down to A4. Choose to scale content proportionally or center it with white padding on the target page.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the PDF with the wrong page size.' },
      { name: 'Choose target size', text: 'Select a preset (A4, Letter, A3, Legal) or enter custom dimensions.' },
      { name: 'Download resized PDF', text: 'Download the resized file. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'What preset sizes are available?', answer: 'A3, A4, A5, B4, B5, Letter, Legal, Tabloid, and custom dimensions in mm or inches.' },
      { question: 'Will the content be scaled or padded?', answer: 'You choose: scale proportionally (content fills the page) or center with white padding (content unchanged, added whitespace).' },
      { question: 'Can I resize just a few pages?', answer: 'Yes. Specify a page range to resize only selected pages.' },
      { question: 'Does resizing affect image quality?', answer: 'Scaling up may reduce sharpness; scaling down does not. Vector text remains crisp at any size.' },
      { question: 'Is this different from cropping?', answer: 'Yes. Resizing changes the physical page dimensions; cropping changes the visible area within existing dimensions.' },
    ],
    relatedSlugs: ['crop-pdf', 'rotate-pdf', 'compress-pdf', 'split-pdf', 'add-watermark'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'redimensionar-pdf', fr: 'redimensionner-pdf', de: 'pdf-groesse-aendern', pt: 'redimensionar-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'resize',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'repair-pdf',
    category: 'optimize',
    primaryKeyword: 'repair pdf',
    h1: 'Repair PDF — Fix Corrupted or Damaged PDF Files',
    metaTitle: 'Repair PDF — Fix Corrupted PDF Files Online, Free',
    metaDescription:
      'Repair damaged, corrupted, or partially downloaded PDF files. Recover readable pages from broken PDFs. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'Repair PDF — Free Online PDF Recovery Tool',
    ogDescription:
      'Fix broken, corrupted, or partially downloaded PDFs and recover readable content. No signup.',
    ogImage: '/og/repair-pdf.png',
    oneLineDefinition:
      'PDF repair reconstructs a damaged or corrupted PDF file by recovering its internal structure, cross-reference table, and readable content streams.',
    longDescription:
      'Recover content from PDFs that will not open, display errors, or were incompletely downloaded. Our repair engine attempts to reconstruct the cross-reference table, recover object streams, and produce a valid PDF containing as many readable pages as possible. Works on files corrupted by storage failures, incomplete transfers, and malformed writers.',
    howToSteps: [
      { name: 'Upload the damaged PDF', text: 'Drag and drop the file that is corrupted or will not open.' },
      { name: 'Attempt repair', text: 'The tool analyzes and reconstructs the PDF structure automatically.' },
      { name: 'Download repaired PDF', text: 'Download any recoverable pages. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'Can it repair any corrupted PDF?', answer: 'Repair success depends on the extent of corruption. Completely overwritten files cannot be recovered.' },
      { question: 'What kinds of errors can it fix?', answer: 'Missing or corrupt cross-reference tables, truncated files, broken object streams, and malformed PDF structure.' },
      { question: 'Will I get all pages back?', answer: 'In most cases yes. Severely damaged files may only yield partial recovery.' },
      { question: 'Can it recover a password-protected PDF I cannot open?', answer: 'No. Password-protected PDFs require the correct password; encryption is not bypassed.' },
      { question: 'Is the repaired PDF guaranteed to open everywhere?', answer: 'The output follows the PDF specification and passes our internal validator, but compatibility with all third-party readers is not guaranteed.' },
    ],
    relatedSlugs: ['compress-pdf', 'merge-pdf', 'split-pdf', 'rotate-pdf', 'pdf-to-word'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'reparar-pdf', fr: 'reparer-pdf', de: 'pdf-reparieren', pt: 'reparar-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'repair',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'pdf-to-epub',
    category: 'convert-from-pdf',
    primaryKeyword: 'pdf to epub',
    h1: 'PDF to EPUB — Convert PDF to eBook Format',
    metaTitle: 'PDF to EPUB — Convert PDF to EPUB eBook Online, Free',
    metaDescription:
      'Convert PDF books and documents to EPUB format for Kindle, Kobo, Apple Books, and more. Reflowable text, no signup. Files deleted in 60 minutes.',
    ogTitle: 'PDF to EPUB — Free Online PDF to eBook Converter',
    ogDescription:
      'Convert PDFs to reflowable EPUB for any e-reader. Text reflows to screen size. No signup.',
    ogImage: '/og/pdf-to-epub.png',
    oneLineDefinition:
      'PDF to EPUB conversion extracts text and images from a PDF and packages them into a reflowable EPUB 3 eBook that adapts to any screen size.',
    longDescription:
      'Make PDFs readable on Kindle, Kobo, Apple Books, and any EPUB-compatible reader. Unlike PDFs — which are fixed-layout — EPUB reflows text to fit any screen size, making it far more comfortable to read on phones and e-ink readers. Supports table of contents extraction, chapter detection, and basic image embedding.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the PDF book or document you want to convert.' },
      { name: 'Set metadata', text: 'Optionally enter title, author, and language for the EPUB metadata.' },
      { name: 'Convert and download', text: 'Download the EPUB file. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'Which e-readers support EPUB?', answer: 'Apple Books, Kobo, Google Play Books, PocketBook, and most readers except Kindle (which requires MOBI or KEPUB).' },
      { question: 'Does it work with Kindle?', answer: 'Not natively — use Amazon Send to Kindle or convert the EPUB to MOBI using Calibre after downloading.' },
      { question: 'Are images included?', answer: 'Yes. Images are embedded at their original resolution.' },
      { question: 'Can it detect chapters?', answer: 'Yes. Heading-style formatting in the PDF is used to generate a chapter table of contents.' },
      { question: 'How does it handle scanned PDFs?', answer: 'OCR is applied first. The recognized text is flowed into the EPUB; quality depends on scan resolution.' },
    ],
    relatedSlugs: ['pdf-to-word', 'pdf-to-html', 'compress-pdf', 'merge-pdf', 'split-pdf'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/epub+zip',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'pdf-a-epub', fr: 'pdf-en-epub', de: 'pdf-zu-epub', pt: 'pdf-para-epub' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'pdf-to-epub',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'protect-pdf',
    category: 'security',
    primaryKeyword: 'protect pdf with password',
    h1: 'Protect PDF with Password',
    metaTitle: 'Protect PDF — Add Password to PDF Online, Free',
    metaDescription:
      'Password-protect a PDF to restrict opening, printing, and editing. AES-256 encryption. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'Protect PDF with Password — Free Online PDF Encryption Tool',
    ogDescription:
      'Lock any PDF with AES-256 encryption. Set open password, print restriction, and copy restriction. No signup.',
    ogImage: '/og/protect-pdf.png',
    oneLineDefinition:
      'PDF password protection applies AES-256 encryption to a PDF file so that only recipients with the correct password can open, print, or edit it.',
    longDescription:
      'Prevent unauthorized access to sensitive PDFs. Set an owner password to restrict editing and printing, or a user password that requires the recipient to enter a password before opening. All encryption uses AES-256 (PDF 1.7 spec). The password is never stored on our servers.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the PDF you want to protect.' },
      { name: 'Set your password', text: 'Enter an open password, and optionally restrict printing and editing.' },
      { name: 'Download protected PDF', text: 'Download the encrypted PDF. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'What encryption standard is used?', answer: 'AES-256 as defined in the PDF 1.7 and PDF 2.0 specifications.' },
      { question: 'Can I restrict printing without a password?', answer: 'Yes. Set an owner password and disable the print permission — the file opens without a password but printing is blocked.' },
      { question: 'Is my password stored?', answer: 'Never. The password is used only during encryption and is not logged or retained.' },
      { question: 'Can I unlock a PDF I already own?', answer: 'Yes. Use the "Unlock PDF" tool with your correct password.' },
      { question: 'Is AES-256 strong enough for confidential documents?', answer: 'Yes. AES-256 is used by governments and financial institutions worldwide. No known practical attacks exist against correctly implemented AES-256.' },
    ],
    relatedSlugs: ['redact-pdf', 'e-sign-pdf', 'add-watermark', 'merge-pdf', 'compress-pdf'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'proteger-pdf', fr: 'proteger-pdf', de: 'pdf-sperren', pt: 'proteger-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'protect',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'ocr-pdf',
    category: 'ai',
    primaryKeyword: 'ocr pdf',
    h1: 'OCR PDF — Make Scanned PDFs Searchable',
    metaTitle: 'OCR PDF — Convert Scanned PDF to Searchable Text Online, Free',
    metaDescription:
      'Apply OCR to scanned PDFs to make text searchable and selectable. Supports 100+ languages. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'OCR PDF — Free Online Scanned PDF to Searchable PDF',
    ogDescription:
      'Turn image-only scanned PDFs into searchable, selectable, and copy-paste-able documents. 100+ languages. No signup.',
    ogImage: '/og/ocr-pdf.png',
    oneLineDefinition:
      'OCR (Optical Character Recognition) converts the page images in a scanned PDF into a searchable, selectable text layer embedded in the same PDF file.',
    longDescription:
      'Scanned documents are images — they cannot be searched, copied, or indexed by search engines. Our OCR engine recognizes text in 100+ languages and embeds an invisible searchable text layer beneath the original scan, producing a PDF that looks identical but behaves like a born-digital document. Accuracy exceeds 99% for high-quality scans.',
    howToSteps: [
      { name: 'Upload your scanned PDF', text: 'Drag and drop the image-only PDF you want to make searchable.' },
      { name: 'Select the language', text: 'Choose the primary language(s) in the document for best accuracy.' },
      { name: 'Download searchable PDF', text: 'Download the OCRed PDF. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'How accurate is the OCR?', answer: 'Over 99% character accuracy for clean 300+ DPI scans. Accuracy drops for handwritten text, decorative fonts, and low-resolution scans.' },
      { question: 'Which languages are supported?', answer: 'Over 100 languages including Arabic, Chinese, Cyrillic scripts, and all major Latin-based languages.' },
      { question: 'Does OCR alter the visual appearance of the PDF?', answer: 'No. The original scan is preserved pixel-for-pixel; only an invisible text layer is added.' },
      { question: 'Can I copy and paste text after OCR?', answer: 'Yes. The recognized text can be selected, copied, and pasted in any PDF reader.' },
      { question: 'Does this work on multi-page scans?', answer: 'Yes. All pages are processed, including mixed pages where some are digital and some are scanned.' },
    ],
    relatedSlugs: ['pdf-to-word', 'redact-pdf', 'compress-pdf', 'merge-pdf', 'pdf-to-html'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'ocr-pdf', fr: 'ocr-pdf', de: 'pdf-ocr', pt: 'ocr-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'ocr',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
  {
    slug: 'extract-pdf-pages',
    category: 'organize',
    primaryKeyword: 'extract pages from pdf',
    h1: 'Extract Pages from PDF',
    metaTitle: 'Extract PDF Pages — Extract Specific Pages from PDF Online, Free',
    metaDescription:
      'Extract individual pages or page ranges from a PDF into a new document. Free, no signup. Files deleted in 60 minutes.',
    ogTitle: 'Extract PDF Pages — Free Online PDF Page Extractor',
    ogDescription:
      'Pick specific pages or page ranges and extract them into a new PDF. No signup required.',
    ogImage: '/og/extract-pdf-pages.png',
    oneLineDefinition:
      'PDF page extraction copies selected pages from a source PDF into a new, smaller PDF document without modifying the original.',
    longDescription:
      'Pull exactly the pages you need from a large PDF report, book, or manual. Specify individual page numbers, ranges (e.g. 1-5, 12, 20-25), or click thumbnails to select interactively. The extracted pages are delivered as a new PDF preserving the original content, fonts, and links.',
    howToSteps: [
      { name: 'Upload your PDF', text: 'Drag and drop the PDF from which you want to extract pages.' },
      { name: 'Select pages', text: 'Click page thumbnails or enter page numbers and ranges.' },
      { name: 'Extract and download', text: 'Download the new PDF containing only your selected pages. Files are deleted within 60 minutes.' },
    ],
    faqs: [
      { question: 'Does extraction modify the original file?', answer: 'No. A new PDF is created; the original is unchanged on your device.' },
      { question: 'Can I select non-contiguous pages?', answer: 'Yes. Select any combination of individual pages and ranges.' },
      { question: 'Is page quality preserved?', answer: 'Yes. Pages are copied losslessly from the source document.' },
      { question: 'Can I extract from a password-protected PDF?', answer: 'Yes, after providing the correct password, which is never stored.' },
      { question: 'How is this different from splitting?', answer: 'Splitting divides the PDF at regular intervals; extraction lets you hand-pick exactly which pages go into the new PDF.' },
    ],
    relatedSlugs: ['split-pdf', 'merge-pdf', 'rotate-pdf', 'compress-pdf', 'crop-pdf'],
    acceptedInputMimeTypes: ['application/pdf'],
    outputMimeType: 'application/pdf',
    maxFreeFileSizeMb: 25,
    maxPaidFileSizeMb: 500,
    localizedSlugs: { es: 'extraer-paginas-pdf', fr: 'extraire-pages-pdf', de: 'pdf-seiten-extrahieren', pt: 'extrair-paginas-pdf' },
    publishedAt: NOW,
    updatedAt: NOW,
    op: 'extract-pages',
    acceptMimes: ['application/pdf'],
    minFiles: 1,
    maxFiles: 1,
    maxBytes: 50 * 1024 * 1024,
  },
] as const satisfies ReadonlyArray<SeoTool>;

export function getToolBySlug(slug: string): SeoTool | undefined {
  return TOOL_SEED.find((t) => t.slug === slug);
}

export function getRelatedTools(slugs: readonly string[]): SeoTool[] {
  return slugs.map(getToolBySlug).filter((t): t is SeoTool => t !== undefined);
}
