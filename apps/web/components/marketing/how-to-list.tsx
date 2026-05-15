import type { HowToStep } from '@/lib/seo/types';

interface HowToListProps {
  title: string;
  steps: readonly HowToStep[];
}

export function HowToList({ title, steps }: HowToListProps) {
  return (
    <section className="mt-12">
      <h2 className="text-2xl font-semibold tracking-tight">{title}</h2>
      <ol className="mt-6 space-y-4">
        {steps.map((step, idx) => (
          <li key={step.name} className="flex gap-4">
            <span
              aria-hidden
              className="flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-neutral-900 text-sm font-semibold text-white dark:bg-white dark:text-neutral-900"
            >
              {idx + 1}
            </span>
            <div>
              <h3 className="font-medium">{step.name}</h3>
              <p className="mt-1 text-neutral-600 dark:text-neutral-400">{step.text}</p>
            </div>
          </li>
        ))}
      </ol>
    </section>
  );
}
