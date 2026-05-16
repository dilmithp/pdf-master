import { auth } from "@clerk/nextjs/server";
import { redirect } from "next/navigation";
import { manageSubscription } from "@/app/(dashboard)/actions";
import { UsageChart } from "@/components/dashboard/UsageChart";
import { PLANS } from "@/lib/billing/plans";

export const metadata = { title: "Billing", robots: { index: false } };

export default async function BillingPage() {
  const clerkSession = await auth();
  if (!clerkSession.userId) redirect("/sign-in");

  const freePlan = PLANS.find((p) => p.id === "free");

  return (
    <div className="mx-auto max-w-3xl space-y-8 px-4 py-10">
      <h1 className="text-2xl font-bold text-neutral-900 dark:text-white">Billing</h1>

      <section className="rounded-xl border border-neutral-200 bg-white p-6 shadow-sm dark:border-neutral-800 dark:bg-neutral-900">
        <div className="flex items-start justify-between">
          <div>
            <p className="text-sm text-neutral-500">Current plan</p>
            <p className="mt-1 text-xl font-semibold text-neutral-900 dark:text-white">
              {freePlan?.name ?? "Free"}
            </p>
            <p className="mt-1 text-sm text-neutral-500">{freePlan?.description}</p>
          </div>
          <a
            href="/pricing"
            className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700"
          >
            Upgrade
          </a>
        </div>
      </section>

      <section className="rounded-xl border border-neutral-200 bg-white p-6 shadow-sm dark:border-neutral-800 dark:bg-neutral-900">
        <h2 className="mb-4 text-lg font-semibold text-neutral-800 dark:text-neutral-200">
          Usage this month
        </h2>
        <UsageChart used={3} total={10} label="Operations" />
      </section>

      <section className="rounded-xl border border-neutral-200 bg-white p-6 shadow-sm dark:border-neutral-800 dark:bg-neutral-900">
        <h2 className="mb-4 text-lg font-semibold text-neutral-800 dark:text-neutral-200">
          Subscription management
        </h2>
        <p className="mb-4 text-sm text-neutral-500">
          Manage payment methods, view invoices, or cancel your subscription via the Stripe portal.
        </p>
        <form action={manageSubscription}>
          <button
            type="submit"
            className="rounded-lg border border-neutral-300 px-4 py-2 text-sm font-medium text-neutral-700 hover:bg-neutral-50 dark:border-neutral-700 dark:text-neutral-300 dark:hover:bg-neutral-800"
          >
            Manage subscription
          </button>
        </form>
      </section>
    </div>
  );
}
