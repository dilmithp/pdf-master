import { BRAND } from '@/lib/brand';
import type { Metadata } from 'next';
import type { ReactNode } from 'react';

export const metadata: Metadata = {
  robots: { index: true, follow: true },
};

interface LegalLayoutProps {
  children: ReactNode;
}

export default function LegalLayout({ children }: LegalLayoutProps) {
  return (
    <div className="min-h-screen bg-white dark:bg-neutral-950">
      {/* Brand header */}
      <header className="border-b border-neutral-200 dark:border-neutral-800">
        <div className="mx-auto max-w-4xl px-4 py-4">
          <a
            href="/"
            className="text-xl font-bold text-neutral-900 dark:text-neutral-100 hover:opacity-80 transition-opacity"
          >
            {BRAND.name}
          </a>
        </div>
      </header>

      {/* Legal content */}
      <main className="mx-auto max-w-4xl px-4 py-12">
        {children}
      </main>

      {/* Footer */}
      <footer className="border-t border-neutral-200 dark:border-neutral-800 mt-16">
        <div className="mx-auto max-w-4xl px-4 py-8 text-sm text-neutral-500 dark:text-neutral-400">
          <p>
            {BRAND.companyEntity} &mdash; {BRAND.legalAddress}
          </p>
          <p className="mt-2">
            Questions? Contact{' '}
            <a
              href={`mailto:${BRAND.dpoEmail}`}
              className="underline hover:text-neutral-700 dark:hover:text-neutral-200"
            >
              {BRAND.dpoEmail}
            </a>
          </p>
        </div>
      </footer>
    </div>
  );
}
