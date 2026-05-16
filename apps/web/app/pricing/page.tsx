import { PLANS } from "@/lib/billing/plans";
import { PlanCard } from "@/components/pricing/PlanCard";
import { BRAND } from "@/lib/brand";
import type { Metadata } from "next";

export const metadata: Metadata = {
  title: `Pricing | ${BRAND.name}`,
  description: "Simple, transparent pricing. Start free, upgrade when you need more power.",
};

export default function PricingPage() {
  const cancelled = false; // server component — read via searchParams if needed

  return (
    <div className="mx-auto max-w-6xl px-4 py-20">
      <div className="mb-12 text-center">
        <h1 className="text-4xl font-bold tracking-tight text-neutral-900 dark:text-white">
          Simple, transparent pricing
        </h1>
        <p className="mt-4 text-lg text-neutral-500">
          Start free. No credit card required. Upgrade when you need more power.
        </p>
        {cancelled && (
          <p className="mt-4 rounded-lg bg-amber-50 px-4 py-2 text-sm text-amber-700 dark:bg-amber-950 dark:text-amber-300">
            Checkout was cancelled — your plan was not changed.
          </p>
        )}
      </div>

      <div className="grid grid-cols-1 gap-8 md:grid-cols-3">
        {PLANS.map((plan) => (
          <PlanCard key={plan.id} plan={plan} />
        ))}
      </div>

      <div className="mt-16 text-center">
        <h2 className="mb-4 text-xl font-semibold text-neutral-900 dark:text-white">
          Frequently asked questions
        </h2>
        <dl className="mx-auto max-w-2xl space-y-6 text-left text-sm">
          {FAQ.map((item) => (
            <div key={item.q}>
              <dt className="font-medium text-neutral-900 dark:text-white">{item.q}</dt>
              <dd className="mt-1 text-neutral-500">{item.a}</dd>
            </div>
          ))}
        </dl>
      </div>
    </div>
  );
}

const FAQ = [
  {
    q: "Do I need a credit card to start?",
    a: "No. The Free plan requires no payment information. Add a card only when you upgrade.",
  },
  {
    q: "Are my files really deleted after 60 minutes?",
    a: "Yes. All uploaded files carry an auto-delete tag and are purged from S3 within 60 minutes, on every plan.",
  },
  {
    q: "Can I cancel at any time?",
    a: "Absolutely. Cancel from the billing portal — your plan reverts to Free at the end of the current billing period.",
  },
  {
    q: "What counts as an operation?",
    a: "Each distinct PDF action (merge, split, compress, convert, OCR, sign, etc.) counts as one operation.",
  },
  {
    q: "Is there an annual discount?",
    a: "Yes — annual billing saves roughly 20% compared to monthly on Pro and Team plans.",
  },
] as const;
