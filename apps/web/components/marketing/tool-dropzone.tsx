import type { SeoTool } from '@/lib/seo/types';

interface ToolDropzoneProps {
  tool: SeoTool;
}

export function ToolDropzone({ tool }: ToolDropzoneProps) {
  const acceptedExt = tool.acceptedInputMimeTypes
    .map((m) => {
      if (m === 'application/pdf') return 'PDF';
      if (m.startsWith('image/')) return m.replace('image/', '').toUpperCase();
      if (m.includes('wordprocessingml')) return 'DOCX';
      if (m === 'application/msword') return 'DOC';
      if (m.includes('spreadsheetml')) return 'XLSX';
      if (m.includes('presentationml')) return 'PPTX';
      return m;
    })
    .join(', ');

  return (
    <section
      aria-label={`${tool.h1} tool`}
      className="rounded-2xl border-2 border-dashed border-neutral-300 bg-neutral-50 p-10 text-center dark:border-neutral-700 dark:bg-neutral-900"
    >
      <p className="text-lg font-medium">Drop your {acceptedExt} files here</p>
      <p className="mt-1 text-sm text-neutral-600 dark:text-neutral-400">
        or click to browse. Up to {tool.maxFreeFileSizeMb}&nbsp;MB on the free tier.
      </p>
      <button
        type="button"
        className="mt-6 rounded-md bg-neutral-900 px-5 py-2.5 font-medium text-white dark:bg-white dark:text-neutral-900"
      >
        Select files
      </button>
      <p className="mt-4 text-xs text-neutral-500 dark:text-neutral-500">
        Files are encrypted in transit and deleted from our servers within 60 minutes.
      </p>
    </section>
  );
}
