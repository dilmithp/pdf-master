"use client";

import { useCallback, useId, useRef, useState } from "react";

export interface DropzoneProps {
  accept: string[];
  maxFiles: number;
  maxBytes: number;
  onFiles: (files: File[]) => void;
}

interface ValidationError {
  message: string;
}

function formatBytes(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${Math.round(bytes / 1024)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

function validateFiles(
  files: File[],
  accept: string[],
  maxFiles: number,
  maxBytes: number,
): ValidationError[] {
  const errors: ValidationError[] = [];

  if (files.length > maxFiles) {
    errors.push({ message: `Too many files. Maximum is ${maxFiles}.` });
    return errors;
  }

  for (const file of files) {
    if (!accept.includes(file.type)) {
      errors.push({ message: `"${file.name}" is not an accepted file type.` });
    } else if (file.size > maxBytes) {
      errors.push({
        message: `"${file.name}" exceeds the ${formatBytes(maxBytes)} size limit.`,
      });
    }
  }

  return errors;
}

export function Dropzone({ accept, maxFiles, maxBytes, onFiles }: DropzoneProps) {
  const inputRef = useRef<HTMLInputElement>(null);
  const [isDragging, setIsDragging] = useState(false);
  const [errors, setErrors] = useState<ValidationError[]>([]);
  const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
  const errorId = useId();

  const handleFiles = useCallback(
    (files: File[]) => {
      const errs = validateFiles(files, accept, maxFiles, maxBytes);
      setErrors(errs);
      if (errs.length === 0) {
        setSelectedFiles(files);
        onFiles(files);
      }
    },
    [accept, maxFiles, maxBytes, onFiles],
  );

  const onDragOver = useCallback((e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    setIsDragging(true);
  }, []);

  const onDragLeave = useCallback(() => {
    setIsDragging(false);
  }, []);

  const onDrop = useCallback(
    (e: React.DragEvent<HTMLDivElement>) => {
      e.preventDefault();
      setIsDragging(false);
      const files = Array.from(e.dataTransfer.files);
      handleFiles(files);
    },
    [handleFiles],
  );

  const onInputChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const files = Array.from(e.target.files ?? []);
      handleFiles(files);
    },
    [handleFiles],
  );

  const onKeyDown = useCallback((e: React.KeyboardEvent<HTMLDivElement>) => {
    if (e.key === "Enter" || e.key === " ") {
      e.preventDefault();
      inputRef.current?.click();
    }
  }, []);

  const acceptAttr = accept.join(",");
  const totalSize = selectedFiles.reduce((acc, f) => acc + f.size, 0);

  return (
    <div>
      <div
        role="button"
        tabIndex={0}
        aria-label="Drop files here or press Enter to browse"
        aria-describedby={errors.length > 0 ? errorId : undefined}
        className={[
          "rounded-2xl border-2 border-dashed p-10 text-center cursor-pointer transition-colors",
          isDragging
            ? "border-blue-500 bg-blue-50 dark:border-blue-400 dark:bg-blue-950"
            : "border-neutral-300 bg-neutral-50 dark:border-neutral-700 dark:bg-neutral-900",
        ].join(" ")}
        onDragOver={onDragOver}
        onDragLeave={onDragLeave}
        onDrop={onDrop}
        onClick={() => inputRef.current?.click()}
        onKeyDown={onKeyDown}
      >
        <input
          ref={inputRef}
          type="file"
          className="sr-only"
          accept={acceptAttr}
          multiple={maxFiles > 1}
          onChange={onInputChange}
          aria-hidden="true"
          tabIndex={-1}
        />

        <p className="text-lg font-medium">
          {selectedFiles.length > 0
            ? `${selectedFiles.length} file${selectedFiles.length > 1 ? "s" : ""} selected (${formatBytes(totalSize)})`
            : "Drop files here or click to browse"}
        </p>
        <p className="mt-1 text-sm text-neutral-600 dark:text-neutral-400">
          Accepts {accept.map((m) => m.split("/")[1]?.toUpperCase() ?? m).join(", ")} — up to{" "}
          {maxFiles} file{maxFiles > 1 ? "s" : ""}, max {formatBytes(maxBytes)} each.
        </p>

        <button
          type="button"
          className="mt-6 rounded-md bg-neutral-900 px-5 py-2.5 font-medium text-white dark:bg-white dark:text-neutral-900"
          onClick={(e) => {
            e.stopPropagation();
            inputRef.current?.click();
          }}
        >
          Select files
        </button>

        <p className="mt-4 text-xs text-neutral-500">
          Files are encrypted in transit and deleted within 60 minutes.
        </p>
      </div>

      {errors.length > 0 && (
        <ul
          id={errorId}
          role="alert"
          aria-live="polite"
          className="mt-3 space-y-1"
        >
          {errors.map((err, i) => (
            <li key={i} className="text-sm text-red-600 dark:text-red-400">
              {err.message}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
