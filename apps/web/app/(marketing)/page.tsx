import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';
import Link from 'next/link';

export const metadata: Metadata = {
  title: `${BRAND.name} — Edit, Convert, and Sign PDFs Online`,
  description:
    'Privacy-first PDF productivity. Merge, split, compress, convert, sign, and chat with PDFs. Files deleted in 60 minutes. No signup required.',
  alternates: { canonical: '/' },
};

export default function HomePage() {
  return (
    <div className="mx-auto max-w-6xl px-4 py-16">
      <section className="text-center">
        <h1 className="text-balance text-4xl font-semibold tracking-tight sm:text-5xl">
          The PDF platform built for people who care about their documents.
        </h1>
        <p className="mx-auto mt-6 max-w-2xl text-pretty text-lg text-neutral-600 dark:text-neutral-400">
          Merge, split, compress, convert, sign, and chat with PDFs — privately. Files are deleted
          from our servers within 60 minutes. No signup required.
        </p>
        <div className="mt-10 flex flex-wrap justify-center gap-3">
          <Link
            href="/tools"
            className="rounded-md bg-neutral-900 px-5 py-2.5 font-medium text-white dark:bg-white dark:text-neutral-900"
          >
            Browse all tools
          </Link>
          <Link
            href="/pricing"
            className="rounded-md border border-neutral-300 px-5 py-2.5 font-medium dark:border-neutral-700"
          >
            See pricing
          </Link>
        </div>
      </section>
    </div>
  );
}
