import { BRAND } from '@/lib/brand';
import { TOOL_SEED } from '@/lib/tools/seed';
import type { Metadata } from 'next';
import Link from 'next/link';

export const metadata: Metadata = {
  title: 'All PDF Tools',
  description: `Browse every ${BRAND.name} PDF tool. Merge, split, compress, convert, sign, and more — privately, no signup required.`,
  alternates: { canonical: '/tools' },
};

export default function ToolsIndexPage() {
  return (
    <div className="mx-auto max-w-6xl px-4 py-16">
      <header>
        <h1 className="text-4xl font-semibold tracking-tight">All PDF tools</h1>
        <p className="mt-4 max-w-2xl text-neutral-600 dark:text-neutral-400">
          Every tool below is free, private, and works in your browser. Files are deleted from our
          servers within 60 minutes.
        </p>
      </header>
      <ul className="mt-10 grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
        {TOOL_SEED.map((tool) => (
          <li key={tool.slug}>
            <Link
              href={`/tools/${tool.slug}`}
              className="block rounded-lg border border-neutral-200 p-4 transition-colors hover:border-neutral-300 dark:border-neutral-800 dark:hover:border-neutral-700"
            >
              <h2 className="font-medium">{tool.h1}</h2>
              <p className="mt-1 line-clamp-2 text-sm text-neutral-600 dark:text-neutral-400">
                {tool.oneLineDefinition}
              </p>
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}
