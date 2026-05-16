"use client";

import { useCallback, useEffect, useRef, useState } from "react";
import { getJobResult } from "@/lib/api/gateway";

interface ResultDownloadProps {
  jobId: string;
  jwt?: string;
  autoTrigger?: boolean;
  filename?: string;
}

type DownloadState =
  | { kind: "loading" }
  | { kind: "ready"; downloadUrl: string }
  | { kind: "error"; message: string };

export function ResultDownload({
  jobId,
  jwt,
  autoTrigger = true,
  filename = "result",
}: ResultDownloadProps) {
  const [downloadState, setDownloadState] = useState<DownloadState>({ kind: "loading" });
  const hasTriggered = useRef(false);

  const triggerDownload = useCallback((url: string) => {
    if (hasTriggered.current) return;
    hasTriggered.current = true;
    const a = document.createElement("a");
    a.href = url;
    a.download = filename;
    a.rel = "noopener noreferrer";
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  }, [filename]);

  useEffect(() => {
    let cancelled = false;

    getJobResult(jobId, jwt)
      .then((result) => {
        if (cancelled) return;
        setDownloadState({ kind: "ready", downloadUrl: result.downloadUrl });
        if (autoTrigger) triggerDownload(result.downloadUrl);
      })
      .catch((err: unknown) => {
        if (cancelled) return;
        const message = err instanceof Error ? err.message : "Failed to fetch download URL";
        setDownloadState({ kind: "error", message });
      });

    return () => {
      cancelled = true;
    };
  }, [jobId, jwt, autoTrigger, triggerDownload]);

  if (downloadState.kind === "loading") {
    return (
      <p className="text-sm text-neutral-500" aria-live="polite">
        Preparing download...
      </p>
    );
  }

  if (downloadState.kind === "error") {
    return (
      <p className="text-sm text-red-600 dark:text-red-400" role="alert" aria-live="polite">
        {downloadState.message}
      </p>
    );
  }

  return (
    <div className="flex flex-col items-center gap-4">
      <div className="flex h-12 w-12 items-center justify-center rounded-full bg-green-100 dark:bg-green-900">
        <svg
          className="h-7 w-7 text-green-600 dark:text-green-400"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth={2}
          aria-hidden="true"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"
          />
        </svg>
      </div>
      <p className="text-base font-semibold text-neutral-900 dark:text-neutral-100">
        Your file is ready!
      </p>
      <a
        href={downloadState.downloadUrl}
        download={filename}
        rel="noopener noreferrer"
        className="rounded-md bg-neutral-900 px-6 py-2.5 font-medium text-white dark:bg-white dark:text-neutral-900"
      >
        Download
      </a>
    </div>
  );
}
