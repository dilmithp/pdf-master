import type { SeoTool } from '@/lib/seo/types';
import Link from 'next/link';

interface RelatedToolsProps {
  tools: readonly SeoTool[];
}

export function RelatedTools({ tools }: RelatedToolsProps) {
  if (tools.length === 0) return null;
  return (
    <section className="mt-12">
      <h2 className="text-2xl font-semibold tracking-tight">Related PDF tools</h2>
      <ul className="mt-6 grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
        {tools.map((tool) => (
          <li key={tool.slug}>
            <Link
              href={`/tools/${tool.slug}`}
              className="block rounded-lg border border-neutral-200 p-4 transition-colors hover:border-neutral-300 dark:border-neutral-800 dark:hover:border-neutral-700"
            >
              <h3 className="font-medium">{tool.h1}</h3>
              <p className="mt-1 line-clamp-2 text-sm text-neutral-600 dark:text-neutral-400">
                {tool.oneLineDefinition}
              </p>
            </Link>
          </li>
        ))}
      </ul>
    </section>
  );
}
