export interface Job {
  readonly id: string;
  readonly operation: string;
  readonly status: "PENDING" | "PROCESSING" | "COMPLETED" | "FAILED";
  readonly createdAt: string;
  readonly fileName?: string;
}

interface JobTableProps {
  readonly jobs: readonly Job[];
}

const STATUS_STYLES: Record<Job["status"], string> = {
  PENDING: "bg-neutral-100 text-neutral-600 dark:bg-neutral-800 dark:text-neutral-400",
  PROCESSING: "bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-300",
  COMPLETED: "bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-300",
  FAILED: "bg-red-100 text-red-700 dark:bg-red-900 dark:text-red-300",
};

function formatDate(iso: string): string {
  return new Date(iso).toLocaleDateString("en-US", {
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}

export function JobTable({ jobs }: JobTableProps) {
  if (jobs.length === 0) {
    return (
      <div className="rounded-xl border border-neutral-200 bg-white p-8 text-center text-sm text-neutral-400 dark:border-neutral-800 dark:bg-neutral-900">
        No jobs yet. Upload a PDF to get started.
      </div>
    );
  }

  return (
    <div className="overflow-hidden rounded-xl border border-neutral-200 dark:border-neutral-800">
      <table className="w-full text-sm">
        <thead>
          <tr className="border-b border-neutral-200 bg-neutral-50 text-left text-xs font-medium uppercase text-neutral-500 dark:border-neutral-800 dark:bg-neutral-900">
            <th className="px-4 py-3">File</th>
            <th className="px-4 py-3">Operation</th>
            <th className="px-4 py-3">Status</th>
            <th className="px-4 py-3">Date</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-neutral-100 bg-white dark:divide-neutral-800 dark:bg-neutral-900">
          {jobs.map((job) => (
            <tr key={job.id} className="hover:bg-neutral-50 dark:hover:bg-neutral-800/50">
              <td className="max-w-[200px] truncate px-4 py-3 text-neutral-700 dark:text-neutral-300">
                {job.fileName ?? "—"}
              </td>
              <td className="px-4 py-3 capitalize text-neutral-700 dark:text-neutral-300">
                {job.operation.toLowerCase().replace(/_/g, " ")}
              </td>
              <td className="px-4 py-3">
                <span className={`rounded-full px-2 py-0.5 text-xs font-medium ${STATUS_STYLES[job.status]}`}>
                  {job.status}
                </span>
              </td>
              <td className="px-4 py-3 text-neutral-500">{formatDate(job.createdAt)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
