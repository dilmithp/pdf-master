"use client";

import type { ReactNode } from "react";
import { useCallback, useState } from "react";
import { Dropzone } from "@/components/upload/Dropzone";
import { UploadProgress } from "@/components/upload/UploadProgress";
import type { FileUploadItem } from "@/components/upload/UploadProgress";
import { JobStatusIndicator } from "@/components/job/JobStatusIndicator";
import { ResultDownload } from "@/components/job/ResultDownload";
import { startJob } from "@/app/(marketing)/tools/[tool]/actions";
import { uploadToPresigned } from "@/lib/upload/presignedPut";

type FlowState =
  | { kind: "idle" }
  | { kind: "uploading"; files: FileUploadItem[] }
  | { kind: "processing"; jobId: string }
  | { kind: "done"; jobId: string; outputKey: string }
  | { kind: "error"; message: string };

export interface ToolFlowProps {
  op: string;
  acceptMimes: string[];
  minFiles: number;
  maxFiles: number;
  maxBytes: number;
  formExtras?: ReactNode;
  extraBody?: Record<string, unknown>;
}

function dispatchError(message: string) {
  window.dispatchEvent(new CustomEvent("pdf-master:error", { detail: { message } }));
}

export function ToolFlow({
  op,
  acceptMimes,
  minFiles,
  maxFiles,
  maxBytes,
  formExtras,
  extraBody,
}: ToolFlowProps) {
  const [flowState, setFlowState] = useState<FlowState>({ kind: "idle" });

  const handleFiles = useCallback(
    async (files: File[]) => {
      if (files.length < minFiles) {
        setFlowState({
          kind: "error",
          message: `Please select at least ${minFiles} file${minFiles > 1 ? "s" : ""}.`,
        });
        return;
      }

      const initialItems: FileUploadItem[] = files.map((f) => ({
        name: f.name,
        size: f.size,
        status: "queued",
        progress: 0,
      }));
      setFlowState({ kind: "uploading", files: initialItems });

      let response: Awaited<ReturnType<typeof startJob>>;
      try {
        response = await startJob(op, { fileCount: files.length, ...extraBody });
      } catch (err) {
        const message = err instanceof Error ? err.message : "Failed to create job.";
        setFlowState({ kind: "error", message });
        dispatchError(message);
        return;
      }

      const uploadItems = [...initialItems];

      const uploadPromises = files.map(async (file, idx) => {
        const urlEntry = response.uploadUrls[idx];
        if (!urlEntry) {
          const msg = `No upload URL for file ${file.name}`;
          uploadItems[idx] = { ...uploadItems[idx]!, status: "error", error: msg, progress: 0 };
          setFlowState({ kind: "uploading", files: [...uploadItems] });
          throw new Error(msg);
        }

        uploadItems[idx] = { ...uploadItems[idx]!, status: "uploading", progress: 0 };
        setFlowState({ kind: "uploading", files: [...uploadItems] });

        await uploadToPresigned(urlEntry.url, file, (pct) => {
          uploadItems[idx] = { ...uploadItems[idx]!, progress: pct };
          setFlowState({ kind: "uploading", files: [...uploadItems] });
        });

        uploadItems[idx] = { ...uploadItems[idx]!, status: "done", progress: 100 };
        setFlowState({ kind: "uploading", files: [...uploadItems] });
      });

      try {
        await Promise.all(uploadPromises);
      } catch (err) {
        const message = err instanceof Error ? err.message : "Upload failed.";
        setFlowState({ kind: "error", message });
        dispatchError(message);
        return;
      }

      setFlowState({ kind: "processing", jobId: response.jobId });
    },
    [op, minFiles, extraBody],
  );

  const onSuccess = useCallback((outputKey: string) => {
    setFlowState((prev) => {
      if (prev.kind !== "processing") return prev;
      return { kind: "done", jobId: prev.jobId, outputKey };
    });
  }, []);

  const onError = useCallback((message: string) => {
    setFlowState({ kind: "error", message });
    dispatchError(message);
  }, []);

  const retry = useCallback(() => {
    setFlowState({ kind: "idle" });
  }, []);

  if (flowState.kind === "idle") {
    return (
      <div className="space-y-4">
        {formExtras && <div>{formExtras}</div>}
        <Dropzone
          accept={acceptMimes}
          maxFiles={maxFiles}
          maxBytes={maxBytes}
          onFiles={(files) => void handleFiles(files)}
        />
      </div>
    );
  }

  if (flowState.kind === "uploading") {
    return (
      <section aria-label="Upload progress">
        <UploadProgress files={flowState.files} />
      </section>
    );
  }

  if (flowState.kind === "processing") {
    return (
      <section aria-label="Processing">
        <JobStatusIndicator
          jobId={flowState.jobId}
          onSuccess={onSuccess}
          onError={onError}
        />
      </section>
    );
  }

  if (flowState.kind === "done") {
    return (
      <section aria-label="Download result">
        <ResultDownload jobId={flowState.jobId} autoTrigger />
      </section>
    );
  }

  // error state
  return (
    <section aria-label="Error" aria-live="polite" className="rounded-xl border border-red-200 bg-red-50 p-6 text-center dark:border-red-800 dark:bg-red-950">
      <p className="font-medium text-red-700 dark:text-red-300">{flowState.message}</p>
      <button
        type="button"
        onClick={retry}
        className="mt-4 rounded-md border border-red-300 px-4 py-2 text-sm font-medium text-red-700 hover:bg-red-100 dark:border-red-700 dark:text-red-300 dark:hover:bg-red-900"
      >
        Try again
      </button>
    </section>
  );
}
