import type { Plan } from "@/lib/billing/plans";
import { SubscribeButton } from "./SubscribeButton";

interface PlanCardProps {
  readonly plan: Plan;
}

export function PlanCard({ plan }: PlanCardProps) {
  const isHighlighted = plan.highlighted;

  return (
    <article
      className={`flex flex-col rounded-2xl p-8 ${
        isHighlighted
          ? "border-2 border-blue-500 bg-white shadow-xl dark:bg-neutral-900"
          : "border border-neutral-200 bg-white shadow-sm dark:border-neutral-800 dark:bg-neutral-900"
      }`}
    >
      {isHighlighted && (
        <span className="mb-4 self-start rounded-full bg-blue-100 px-3 py-1 text-xs font-semibold uppercase tracking-wide text-blue-700 dark:bg-blue-900 dark:text-blue-300">
          Most popular
        </span>
      )}

      <h2 className="text-xl font-bold text-neutral-900 dark:text-white">{plan.name}</h2>
      <p className="mt-1 text-sm text-neutral-500">{plan.description}</p>

      <div className="my-6">
        {plan.monthlyPrice === 0 ? (
          <span className="text-4xl font-bold text-neutral-900 dark:text-white">Free</span>
        ) : (
          <>
            <span className="text-4xl font-bold text-neutral-900 dark:text-white">
              ${plan.monthlyPrice}
            </span>
            <span className="text-sm text-neutral-500"> / month</span>
          </>
        )}
      </div>

      <ul className="mb-8 flex-1 space-y-3">
        {plan.features.map((feature) => (
          <li key={feature.label} className="flex items-start gap-2 text-sm">
            {feature.included ? (
              <svg
                className="mt-0.5 h-4 w-4 flex-shrink-0 text-green-500"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                aria-hidden="true"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
              </svg>
            ) : (
              <svg
                className="mt-0.5 h-4 w-4 flex-shrink-0 text-neutral-300 dark:text-neutral-600"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                aria-hidden="true"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            )}
            <span className={feature.included ? "text-neutral-700 dark:text-neutral-300" : "text-neutral-400 dark:text-neutral-600"}>
              {feature.label}
            </span>
          </li>
        ))}
      </ul>

      <SubscribeButton
        priceId={plan.stripePriceIdMonthly}
        planName={plan.name}
        highlighted={isHighlighted}
      />
    </article>
  );
}
