import { auth } from "@clerk/nextjs/server";
import { redirect } from "next/navigation";
import { getServerJwt } from "@/lib/auth/jwt";
import { JobTable } from "@/components/dashboard/JobTable";
import type { Job } from "@/components/dashboard/JobTable";

export const metadata = { title: "Job History", robots: { index: false } };

interface PageProps {
  searchParams: Promise<Record<string, string | string[] | undefined>>;
}

async function fetchJobs(jwt: string | null, params: Record<string, string | string[] | undefined>): Promise<{ jobs: Job[]; total: number }> {
  if (!jwt) return { jobs: [], total: 0 };
  const gatewayUrl = process.env.NEXT_PUBLIC_GATEWAY_URL ?? "http://localhost:8080";

  const page = Number(params.page ?? 0);
  const status = typeof params.status === "string" ? params.status : undefined;
  const op = typeof params.op === "string" ? params.op : undefined;

  const qs = new URLSearchParams({ page: String(page), size: "20", sort: "createdAt,desc" });
  if (status) qs.set("status", status);
  if (op) qs.set("operation", op);

  try {
    const res = await fetch(`${gatewayUrl}/v1/jobs?${qs}`, {
      headers: { Authorization: `Bearer ${jwt}` },
      next: { revalidate: 0 },
    });
    if (!res.ok) return { jobs: [], total: 0 };
    const data = (await res.json()) as { content: Job[]; totalElements: number };
    return { jobs: data.content ?? [], total: data.totalElements ?? 0 };
  } catch {
    return { jobs: [], total: 0 };
  }
}

export default async function JobsPage({ searchParams }: PageProps) {
  const clerkSession = await auth();
  if (!clerkSession.userId) redirect("/sign-in");

  const params = await searchParams;
  const jwt = await getServerJwt();
  const { jobs, total } = await fetchJobs(jwt, params);
  const page = Number(params.page ?? 0);

  return (
    <div className="mx-auto max-w-5xl space-y-6 px-4 py-10">
      <h1 className="text-2xl font-bold text-neutral-900 dark:text-white">Job History</h1>

      <div className="flex gap-3 text-sm">
        <FilterLink label="All" href="/dashboard/jobs" active={!params.status} />
        <FilterLink label="Completed" href="/dashboard/jobs?status=COMPLETED" active={params.status === "COMPLETED"} />
        <FilterLink label="Failed" href="/dashboard/jobs?status=FAILED" active={params.status === "FAILED"} />
        <FilterLink label="Processing" href="/dashboard/jobs?status=PROCESSING" active={params.status === "PROCESSING"} />
      </div>

      <p className="text-sm text-neutral-500">{total} total jobs</p>

      <JobTable jobs={jobs} />

      {total > 20 && (
        <div className="flex gap-3 text-sm">
          {page > 0 && (
            <a href={`/dashboard/jobs?page=${page - 1}`} className="text-blue-600 hover:underline">
              Previous
            </a>
          )}
          {(page + 1) * 20 < total && (
            <a href={`/dashboard/jobs?page=${page + 1}`} className="text-blue-600 hover:underline">
              Next
            </a>
          )}
        </div>
      )}
    </div>
  );
}

function FilterLink({ label, href, active }: { label: string; href: string; active: boolean | string | undefined }) {
  return (
    <a
      href={href}
      className={`rounded-full px-3 py-1 ${active ? "bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-300" : "text-neutral-500 hover:text-neutral-900 dark:hover:text-white"}`}
    >
      {label}
    </a>
  );
}
