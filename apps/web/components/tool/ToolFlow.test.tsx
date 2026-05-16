import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { ToolFlow } from './ToolFlow';

// Mock server action
vi.mock('@/app/(marketing)/tools/[tool]/actions', () => ({
  startJob: vi.fn(),
}));

// Mock upload helper
vi.mock('@/lib/upload/presignedPut', () => ({
  uploadToPresigned: vi.fn(),
  UploadError: class UploadError extends Error {
    status: number;
    constructor(message: string, status: number) {
      super(message);
      this.status = status;
    }
  },
}));

// Mock polling hook
vi.mock('@/lib/jobs/usePollJobStatus', () => ({
  usePollJobStatus: vi.fn(),
}));

// Mock gateway for ResultDownload
vi.mock('@/lib/api/gateway', () => ({
  getJobResult: vi.fn(),
  getJobStatus: vi.fn(),
  createJob: vi.fn(),
}));

import { startJob } from '@/app/(marketing)/tools/[tool]/actions';
import { uploadToPresigned } from '@/lib/upload/presignedPut';
import { usePollJobStatus } from '@/lib/jobs/usePollJobStatus';
import { getJobResult } from '@/lib/api/gateway';

const mockStartJob = vi.mocked(startJob);
const mockUpload = vi.mocked(uploadToPresigned);
const mockUsePoll = vi.mocked(usePollJobStatus);
const mockGetJobResult = vi.mocked(getJobResult);

const defaultProps = {
  op: 'merge',
  acceptMimes: ['application/pdf'],
  minFiles: 1,
  maxFiles: 10,
  maxBytes: 50 * 1024 * 1024,
};

function makeFile(name = 'test.pdf', type = 'application/pdf'): File {
  return new File(['pdf content'], name, { type });
}

describe('ToolFlow', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockUsePoll.mockReturnValue({
      status: null,
      outputKey: undefined,
      error: undefined,
      isPolling: false,
    });
    mockGetJobResult.mockResolvedValue({
      downloadUrl: 'https://s3.example.com/output',
      expiresAt: '2026-01-01T01:00:00Z',
    });
  });

  it('renders the dropzone in idle state', () => {
    render(<ToolFlow {...defaultProps} />);
    expect(screen.getByRole('button', { name: /select files/i })).toBeInTheDocument();
  });

  it('shows upload progress after files are dropped', async () => {
    mockStartJob.mockResolvedValue({
      jobId: 'job-123',
      uploadUrls: [{ url: 'https://s3.example.com/upload', key: 'key1', expiresAt: '' }],
    });
    mockUpload.mockResolvedValue(undefined);

    render(<ToolFlow {...defaultProps} />);

    const dropzone = screen.getByRole('button', { name: /drop files/i });
    const file = makeFile();

    fireEvent.drop(dropzone, {
      dataTransfer: { files: [file] },
    });

    await waitFor(() => {
      expect(screen.getByRole('region', { name: /upload progress/i })).toBeInTheDocument();
    });
  });

  it('calls startJob with the op and transitions to processing', async () => {
    mockStartJob.mockResolvedValue({
      jobId: 'job-456',
      uploadUrls: [{ url: 'https://s3.example.com/upload', key: 'k', expiresAt: '' }],
    });
    mockUpload.mockResolvedValue(undefined);
    mockUsePoll.mockReturnValue({
      status: 'RUNNING',
      outputKey: undefined,
      error: undefined,
      isPolling: true,
    });

    render(<ToolFlow {...defaultProps} />);

    const dropzone = screen.getByRole('button', { name: /drop files/i });
    fireEvent.drop(dropzone, { dataTransfer: { files: [makeFile()] } });

    await waitFor(() => {
      expect(mockStartJob).toHaveBeenCalledWith('merge', expect.objectContaining({ fileCount: 1 }));
    });
  });

  it('shows error state when startJob fails', async () => {
    mockStartJob.mockRejectedValue(new Error('Gateway unavailable'));

    render(<ToolFlow {...defaultProps} />);
    const dropzone = screen.getByRole('button', { name: /drop files/i });
    fireEvent.drop(dropzone, { dataTransfer: { files: [makeFile()] } });

    await waitFor(() => {
      expect(screen.getByRole('button', { name: /try again/i })).toBeInTheDocument();
    });
    expect(screen.getByText(/gateway unavailable/i)).toBeInTheDocument();
  });

  it('resets to idle when retry is clicked', async () => {
    mockStartJob.mockRejectedValue(new Error('oops'));

    render(<ToolFlow {...defaultProps} />);
    const dropzone = screen.getByRole('button', { name: /drop files/i });
    fireEvent.drop(dropzone, { dataTransfer: { files: [makeFile()] } });

    await waitFor(() => screen.getByRole('button', { name: /try again/i }));
    fireEvent.click(screen.getByRole('button', { name: /try again/i }));

    expect(screen.getByRole('button', { name: /select files/i })).toBeInTheDocument();
  });

  it('shows download section when job SUCCEEDED', async () => {
    mockStartJob.mockResolvedValue({
      jobId: 'job-789',
      uploadUrls: [{ url: 'https://s3.example.com/upload', key: 'k', expiresAt: '' }],
    });
    mockUpload.mockResolvedValue(undefined);
    mockUsePoll.mockReturnValue({
      status: 'SUCCEEDED',
      outputKey: 'output-key',
      error: undefined,
      isPolling: false,
    });

    render(<ToolFlow {...defaultProps} />);
    const dropzone = screen.getByRole('button', { name: /drop files/i });
    fireEvent.drop(dropzone, { dataTransfer: { files: [makeFile()] } });

    await waitFor(() => {
      expect(screen.getByRole('region', { name: /download result/i })).toBeInTheDocument();
    });
  });

  it('validates minFiles — shows error if too few files provided', async () => {
    render(<ToolFlow {...defaultProps} minFiles={2} />);
    const dropzone = screen.getByRole('button', { name: /drop files/i });
    fireEvent.drop(dropzone, { dataTransfer: { files: [makeFile()] } });

    await waitFor(() => {
      expect(screen.getByText(/at least 2 file/i)).toBeInTheDocument();
    });
  });
});
