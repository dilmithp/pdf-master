"use client";

export type FileUploadStatus = "queued" | "uploading" | "done" | "error";

export interface FileUploadItem {
  name: string;
  size: number;
  status: FileUploadStatus;
  progress: number;
  error?: string;
}

interface UploadProgressProps {
  files: FileUploadItem[];
}

function formatBytes(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${Math.round(bytes / 1024)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

const STATUS_LABEL: Record<FileUploadStatus, string> = {
  queued: "Waiting",
  uploading: "Uploading",
  done: "Done",
  error: "Failed",
};

export function UploadProgress({ files }: UploadProgressProps) {
  return (
    <ul className="space-y-3" aria-label="Upload progress">
      {files.map((file, idx) => (
        <li key={idx} className="rounded-lg border border-neutral-200 p-3 dark:border-neutral-700">
          <div className="flex items-center justify-between gap-2">
            <span className="min-w-0 truncate text-sm font-medium">{file.name}</span>
            <span className="shrink-0 text-xs text-neutral-500">{formatBytes(file.size)}</span>
          </div>

          <div
            className="mt-2 h-1.5 w-full overflow-hidden rounded-full bg-neutral-200 dark:bg-neutral-700"
            role="progressbar"
            aria-valuenow={file.progress}
            aria-valuemin={0}
            aria-valuemax={100}
            aria-label={`${file.name} upload progress`}
          >
            <div
              className={[
                "h-full rounded-full transition-all duration-300",
                file.status === "error"
                  ? "bg-red-500"
                  : file.status === "done"
                    ? "bg-green-500"
                    : "bg-blue-500",
              ].join(" ")}
              style={{ width: `${file.progress}%` }}
            />
          </div>

          <div className="mt-1 flex items-center justify-between gap-2">
            <span
              className={[
                "text-xs",
                file.status === "error"
                  ? "text-red-600 dark:text-red-400"
                  : file.status === "done"
                    ? "text-green-600 dark:text-green-400"
                    : "text-neutral-500",
              ].join(" ")}
            >
              {file.status === "error" && file.error ? file.error : STATUS_LABEL[file.status]}
            </span>
            <span className="text-xs text-neutral-500">{file.progress}%</span>
          </div>
        </li>
      ))}
    </ul>
  );
}
