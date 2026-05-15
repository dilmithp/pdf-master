import { BRAND } from '@/lib/brand';
import Link from 'next/link';

export default function MarketingLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex min-h-screen flex-col">
      <header className="border-b border-neutral-200 dark:border-neutral-800">
        <nav className="mx-auto flex max-w-6xl items-center justify-between px-4 py-4">
          <Link href="/" className="text-lg font-semibold tracking-tight">
            {BRAND.name}
          </Link>
          <ul className="hidden gap-6 text-sm sm:flex">
            <li>
              <Link href="/tools">All tools</Link>
            </li>
            <li>
              <Link href="/pricing">Pricing</Link>
            </li>
            <li>
              <Link href="/api">Developers</Link>
            </li>
          </ul>
        </nav>
      </header>
      <main className="flex-1">{children}</main>
      <footer className="border-t border-neutral-200 py-8 text-sm text-neutral-600 dark:border-neutral-800 dark:text-neutral-400">
        <div className="mx-auto max-w-6xl px-4">
          <p>
            © {new Date().getFullYear()} {BRAND.legalName}. Files deleted in 60 minutes.
          </p>
        </div>
      </footer>
    </div>
  );
}
