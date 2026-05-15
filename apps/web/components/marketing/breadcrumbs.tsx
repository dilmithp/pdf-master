import type { Breadcrumb } from '@/lib/seo/types';
import Link from 'next/link';

interface BreadcrumbsProps {
  crumbs: readonly Breadcrumb[];
}

export function Breadcrumbs({ crumbs }: BreadcrumbsProps) {
  return (
    <nav aria-label="Breadcrumb" className="mb-6 text-sm text-neutral-600 dark:text-neutral-400">
      <ol className="flex flex-wrap items-center gap-2">
        {crumbs.map((crumb, idx) => {
          const isLast = idx === crumbs.length - 1;
          return (
            <li key={crumb.url} className="flex items-center gap-2">
              {isLast ? (
                <span aria-current="page" className="text-neutral-900 dark:text-neutral-100">
                  {crumb.name}
                </span>
              ) : (
                <Link
                  href={crumb.url.startsWith('http') ? '/' : crumb.url}
                  className="hover:underline"
                >
                  {crumb.name}
                </Link>
              )}
              {!isLast && (
                <span aria-hidden className="text-neutral-400">
                  /
                </span>
              )}
            </li>
          );
        })}
      </ol>
    </nav>
  );
}
