"use client";

import { useCallback, useEffect, useRef, useState } from "react";
import { getJobStatus } from "@/lib/api/gateway";
import type { JobStatus } from "@/lib/api/gateway";

const TERMINAL_STATUSES: ReadonlySet<JobStatus> = new Set(["SUCCEEDED", "FAILED"]);
const BASE_INTERVAL_MS = 1000;
const MAX_BACKOFF_MS = 16000;
const MAX_CONSECUTIVE_ERRORS = 5;

export interface PollState {
  status: JobStatus | null;
  outputKey: string | undefined;
  error: string | undefined;
  isPolling: boolean;
}

export function usePollJobStatus(jobId: string | null, jwt?: string): PollState {
  const [state, setState] = useState<PollState>({
    status: null,
    outputKey: undefined,
    error: undefined,
    isPolling: false,
  });

  const consecutiveErrors = useRef(0);
  const timeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const activeRef = useRef(false);

  const clearScheduled = useCallback(() => {
    if (timeoutRef.current !== null) {
      clearTimeout(timeoutRef.current);
      timeoutRef.current = null;
    }
  }, []);

  const backoffMs = useCallback((errors: number): number => {
    return Math.min(BASE_INTERVAL_MS * Math.pow(2, errors), MAX_BACKOFF_MS);
  }, []);

  useEffect(() => {
    if (!jobId) return;

    activeRef.current = true;
    consecutiveErrors.current = 0;
    setState({ status: "QUEUED", outputKey: undefined, error: undefined, isPolling: true });

    const poll = async (): Promise<void> => {
      if (!activeRef.current) return;

      try {
        const response = await getJobStatus(jobId, jwt);
        consecutiveErrors.current = 0;

        setState({
          status: response.status,
          outputKey: response.outputKey,
          error: response.error,
          isPolling: !TERMINAL_STATUSES.has(response.status),
        });

        if (!TERMINAL_STATUSES.has(response.status) && activeRef.current) {
          timeoutRef.current = setTimeout(() => void poll(), BASE_INTERVAL_MS);
        }
      } catch (err) {
        consecutiveErrors.current += 1;

        if (consecutiveErrors.current >= MAX_CONSECUTIVE_ERRORS) {
          const message = err instanceof Error ? err.message : "Polling failed";
          setState((prev) => ({ ...prev, error: message, isPolling: false }));
          return;
        }

        if (activeRef.current) {
          timeoutRef.current = setTimeout(
            () => void poll(),
            backoffMs(consecutiveErrors.current),
          );
        }
      }
    };

    void poll();

    return () => {
      activeRef.current = false;
      clearScheduled();
    };
  }, [jobId, jwt, backoffMs, clearScheduled]);

  return state;
}
