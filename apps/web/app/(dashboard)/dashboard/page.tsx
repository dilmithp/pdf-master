import { auth } from "@clerk/nextjs/server";
import { redirect } from "next/navigation";
import Link from "next/link";
import { getServerJwt } from "@/lib/auth/jwt";
import { JobTable } from "@/components/dashboard/JobTable";
import type { Job } from "@/components/dashboard/JobTable";

export const metadata = { title: "Dashboard", robots: { index: false } };

async function fetchRecentJobs(jwt: string | null): Promise<Job[]> {
  if (!jwt) return [];
  const gatewayUrl = process.env.NEXT_PUBLIC_GATEWAY_URL ?? "http://localhost:8080";
  try {
    const res = await fetch(`${gatewayUrl}/v1/jobs?limit=5&sort=createdAt,desc`, {
      headers: { Authorization: `Bearer ${jwt}` },
      next: { revalidate: 0 },
    });
    if (!res.ok) return [];
    const data = (await res.json()) as { content: Job[] };
    return data.content ?? [];
  } catch {
    return [];
  }
}

export default async function DashboardPage() {
  const clerkSession = await auth();
  if (!clerkSession.userId) redirect("/sign-in");

  const jwt = await getServerJwt();
  const jobs = await fetchRecentJobs(jwt);

  return (
    <div className="mx-auto max-w-5xl space-y-8 px-4 py-10">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-neutral-900 dark:text-white">Dashboard</h1>
        <Link
          href="/dashboard/jobs"
          className="text-sm text-blue-600 hover:underline dark:text-blue-400"
        >
          View all jobs
        </Link>
      </div>

      <section>
        <h2 className="mb-4 text-lg font-semibold text-neutral-800 dark:text-neutral-200">
          Recent jobs
        </h2>
        <JobTable jobs={jobs} />
      </section>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
        <Link
          href="/dashboard/billing"
          className="rounded-xl border border-neutral-200 bg-white p-5 shadow-sm hover:shadow-md dark:border-neutral-800 dark:bg-neutral-900"
        >
          <p className="text-sm text-neutral-500">Current plan</p>
          <p className="mt-1 text-lg font-semibold text-neutral-900 dark:text-white">Free</p>
          <p className="mt-1 text-xs text-blue-600 dark:text-blue-400">Manage billing →</p>
        </Link>
        <Link
          href="/dashboard/settings"
          className="rounded-xl border border-neutral-200 bg-white p-5 shadow-sm hover:shadow-md dark:border-neutral-800 dark:bg-neutral-900"
        >
          <p className="text-sm text-neutral-500">Account settings</p>
          <p className="mt-1 text-xs text-blue-600 dark:text-blue-400">Update profile →</p>
        </Link>
        <Link
          href="/pricing"
          className="rounded-xl border border-blue-200 bg-blue-50 p-5 shadow-sm hover:shadow-md dark:border-blue-900 dark:bg-blue-950"
        >
          <p className="text-sm text-blue-700 dark:text-blue-300">Upgrade to Pro</p>
          <p className="mt-1 text-lg font-semibold text-blue-800 dark:text-blue-200">$9 / month</p>
          <p className="mt-1 text-xs text-blue-600 dark:text-blue-400">Unlimited + AI →</p>
        </Link>
      </div>
    </div>
  );
}
