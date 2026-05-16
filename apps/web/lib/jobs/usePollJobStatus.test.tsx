import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { renderHook, act, waitFor } from '@testing-library/react';
import { usePollJobStatus } from './usePollJobStatus';
import type { JobStatusResponse } from '@/lib/api/gateway';

vi.mock('@/lib/api/gateway', () => ({
  getJobStatus: vi.fn(),
}));

import { getJobStatus } from '@/lib/api/gateway';

const mockGetJobStatus = vi.mocked(getJobStatus);

function makeStatus(status: JobStatusResponse['status'], outputKey?: string): JobStatusResponse {
  const base: JobStatusResponse = {
    jobId: 'job-1',
    status,
    createdAt: '2026-01-01T00:00:00Z',
    updatedAt: '2026-01-01T00:00:00Z',
  };
  if (outputKey !== undefined) {
    return { ...base, outputKey };
  }
  return base;
}

describe('usePollJobStatus', () => {
  beforeEach(() => {
    vi.useFakeTimers();
    mockGetJobStatus.mockReset();
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('returns null status when jobId is null', () => {
    const { result } = renderHook(() => usePollJobStatus(null));
    expect(result.current.status).toBeNull();
    expect(result.current.isPolling).toBe(false);
  });

  it('starts polling when jobId is provided', async () => {
    mockGetJobStatus.mockResolvedValue(makeStatus('QUEUED'));
    const { result } = renderHook(() => usePollJobStatus('job-1'));

    await waitFor(() => {
      expect(result.current.status).toBe('QUEUED');
    });

    expect(mockGetJobStatus).toHaveBeenCalledWith('job-1', undefined);
  });

  it('continues polling while status is not terminal', async () => {
    mockGetJobStatus
      .mockResolvedValueOnce(makeStatus('QUEUED'))
      .mockResolvedValueOnce(makeStatus('RUNNING'))
      .mockResolvedValue(makeStatus('SUCCEEDED', 'output-key-123'));

    const { result } = renderHook(() => usePollJobStatus('job-1'));

    await waitFor(() => expect(result.current.status).toBe('QUEUED'));

    act(() => { vi.advanceTimersByTime(1000); });
    await waitFor(() => expect(result.current.status).toBe('RUNNING'));

    act(() => { vi.advanceTimersByTime(1000); });
    await waitFor(() => expect(result.current.status).toBe('SUCCEEDED'));
  });

  it('stops polling when status is SUCCEEDED', async () => {
    mockGetJobStatus.mockResolvedValue(makeStatus('SUCCEEDED', 'key'));

    const { result } = renderHook(() => usePollJobStatus('job-1'));
    await waitFor(() => expect(result.current.status).toBe('SUCCEEDED'));
    expect(result.current.isPolling).toBe(false);
    expect(result.current.outputKey).toBe('key');
  });

  it('stops polling when status is FAILED', async () => {
    mockGetJobStatus.mockResolvedValue(makeStatus('FAILED'));
    const { result } = renderHook(() => usePollJobStatus('job-1'));
    await waitFor(() => expect(result.current.status).toBe('FAILED'));
    expect(result.current.isPolling).toBe(false);
  });

  it('uses exponential backoff on consecutive errors', async () => {
    mockGetJobStatus
      .mockRejectedValueOnce(new Error('network error 1'))
      .mockRejectedValueOnce(new Error('network error 2'))
      .mockResolvedValue(makeStatus('SUCCEEDED', 'k'));

    const { result } = renderHook(() => usePollJobStatus('job-1'));

    // first call fails immediately
    await vi.runAllTimersAsync();
    // backoff: 2^1 * 1000 = 2000ms
    act(() => { vi.advanceTimersByTime(2000); });
    await vi.runAllTimersAsync();
    // backoff: 2^2 * 1000 = 4000ms
    act(() => { vi.advanceTimersByTime(4000); });
    await vi.runAllTimersAsync();

    await waitFor(() => expect(result.current.status).toBe('SUCCEEDED'));
  });

  it('stops after MAX_CONSECUTIVE_ERRORS and sets error', async () => {
    mockGetJobStatus.mockRejectedValue(new Error('persistent failure'));

    const { result } = renderHook(() => usePollJobStatus('job-1'));

    for (let i = 0; i < 10; i++) {
      act(() => { vi.advanceTimersByTime(32000); });
      await vi.runAllTimersAsync();
    }

    await waitFor(() => expect(result.current.isPolling).toBe(false));
    expect(result.current.error).toBeTruthy();
  });

  it('stops polling on unmount', async () => {
    mockGetJobStatus.mockResolvedValue(makeStatus('RUNNING'));
    const { unmount } = renderHook(() => usePollJobStatus('job-1'));

    await waitFor(() => expect(mockGetJobStatus).toHaveBeenCalledTimes(1));
    unmount();

    act(() => { vi.advanceTimersByTime(5000); });
    // Should not have been called again after unmount
    expect(mockGetJobStatus).toHaveBeenCalledTimes(1);
  });

  it('passes jwt to getJobStatus', async () => {
    mockGetJobStatus.mockResolvedValue(makeStatus('SUCCEEDED', 'k'));
    renderHook(() => usePollJobStatus('job-1', 'my-jwt'));
    await waitFor(() => expect(mockGetJobStatus).toHaveBeenCalledWith('job-1', 'my-jwt'));
  });
});
