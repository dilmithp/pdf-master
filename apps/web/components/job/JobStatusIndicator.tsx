"use client";

import { useEffect } from "react";
import { usePollJobStatus } from "@/lib/jobs/usePollJobStatus";
import type { JobStatus } from "@/lib/api/gateway";

interface JobStatusIndicatorProps {
  jobId: string;
  jwt?: string;
  onSuccess: (outputKey: string) => void;
  onError: (message: string) => void;
}

const STATUS_MESSAGES: Record<JobStatus, string> = {
  QUEUED: "Your job is queued — processing will begin shortly.",
  RUNNING: "Processing your file...",
  SUCCEEDED: "Processing complete.",
  FAILED: "Processing failed.",
};

export function JobStatusIndicator({ jobId, jwt, onSuccess, onError }: JobStatusIndicatorProps) {
  const { status, outputKey, error, isPolling } = usePollJobStatus(jobId, jwt);

  useEffect(() => {
    if (status === "SUCCEEDED" && outputKey) {
      onSuccess(outputKey);
    }
  }, [status, outputKey, onSuccess]);

  useEffect(() => {
    if (status === "FAILED" || (error && !isPolling)) {
      onError(error ?? "Job failed with no details.");
    }
  }, [status, error, isPolling, onError]);

  const message = status ? STATUS_MESSAGES[status] : "Connecting...";
  const isFailed = status === "FAILED";

  return (
    <div
      role="status"
      aria-live="polite"
      aria-label="Job status"
      className="flex flex-col items-center gap-4 py-8"
    >
      {isPolling && (
        <div
          className="h-10 w-10 animate-spin rounded-full border-4 border-neutral-200 border-t-blue-600"
          aria-hidden="true"
        />
      )}

      {status === "SUCCEEDED" && (
        <div
          className="flex h-10 w-10 items-center justify-center rounded-full bg-green-100 dark:bg-green-900"
          aria-hidden="true"
        >
          <svg className="h-6 w-6 text-green-600 dark:text-green-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
          </svg>
        </div>
      )}

      {isFailed && (
        <div
          className="flex h-10 w-10 items-center justify-center rounded-full bg-red-100 dark:bg-red-900"
          aria-hidden="true"
        >
          <svg className="h-6 w-6 text-red-600 dark:text-red-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2.5}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </div>
      )}

      <p
        className={[
          "text-sm font-medium",
          isFailed ? "text-red-600 dark:text-red-400" : "text-neutral-700 dark:text-neutral-300",
        ].join(" ")}
      >
        {message}
      </p>

      {error && !isFailed && (
        <p className="text-xs text-neutral-500" aria-live="polite">{error}</p>
      )}
    </div>
  );
}
