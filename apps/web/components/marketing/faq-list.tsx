import type { Faq } from '@/lib/seo/types';

interface FaqListProps {
  faqs: readonly Faq[];
}

export function FaqList({ faqs }: FaqListProps) {
  return (
    <section className="mt-12">
      <h2 className="text-2xl font-semibold tracking-tight">Frequently asked questions</h2>
      <dl className="mt-6 divide-y divide-neutral-200 dark:divide-neutral-800">
        {faqs.map((faq) => (
          <details key={faq.question} className="group py-4">
            <summary className="flex cursor-pointer list-none items-center justify-between font-medium">
              <span>{faq.question}</span>
              <span
                aria-hidden
                className="ml-4 text-neutral-400 transition-transform group-open:rotate-45"
              >
                +
              </span>
            </summary>
            <p className="mt-2 text-neutral-600 dark:text-neutral-400">{faq.answer}</p>
          </details>
        ))}
      </dl>
    </section>
  );
}
