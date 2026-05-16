import { describe, it, expect, vi, beforeEach } from 'vitest';
import { uploadToPresigned, UploadError } from './presignedPut';

interface MockXhr {
  open: ReturnType<typeof vi.fn>;
  setRequestHeader: ReturnType<typeof vi.fn>;
  send: ReturnType<typeof vi.fn>;
  upload: { addEventListener: ReturnType<typeof vi.fn> };
  addEventListener: ReturnType<typeof vi.fn>;
  status: number;
  statusText: string;
  _listeners: Record<string, ((e?: unknown) => void)[]>;
  _uploadListeners: Record<string, ((e?: unknown) => void)[]>;
}

function makeMockXhr(): MockXhr {
  const xhr: MockXhr = {
    open: vi.fn(),
    setRequestHeader: vi.fn(),
    send: vi.fn(),
    status: 200,
    statusText: 'OK',
    _listeners: {},
    _uploadListeners: {},
    upload: {
      addEventListener: vi.fn((event: string, cb: (e?: unknown) => void) => {
        xhr._uploadListeners[event] = xhr._uploadListeners[event] ?? [];
        xhr._uploadListeners[event]!.push(cb);
      }),
    },
    addEventListener: vi.fn((event: string, cb: (e?: unknown) => void) => {
      xhr._listeners[event] = xhr._listeners[event] ?? [];
      xhr._listeners[event]!.push(cb);
    }),
  };
  return xhr;
}

function triggerXhrEvent(xhr: MockXhr, event: string, detail?: unknown) {
  const listeners = xhr._listeners[event] ?? [];
  for (const cb of listeners) cb(detail);
}

function triggerUploadEvent(xhr: MockXhr, event: string, detail?: unknown) {
  const listeners = xhr._uploadListeners[event] ?? [];
  for (const cb of listeners) cb(detail);
}

describe('uploadToPresigned', () => {
  let mockXhr: MockXhr;

  beforeEach(() => {
    mockXhr = makeMockXhr();
    vi.stubGlobal('XMLHttpRequest', vi.fn(() => mockXhr));
  });

  it('opens a PUT request with the given URL', async () => {
    const file = new File(['hello'], 'test.pdf', { type: 'application/pdf' });
    const promise = uploadToPresigned('https://s3.example.com/key', file);
    triggerXhrEvent(mockXhr, 'load');
    await promise;
    expect(mockXhr.open).toHaveBeenCalledWith('PUT', 'https://s3.example.com/key');
  });

  it('sets Content-Type header from file.type', async () => {
    const file = new File(['data'], 'doc.pdf', { type: 'application/pdf' });
    const promise = uploadToPresigned('https://s3.example.com/key', file);
    triggerXhrEvent(mockXhr, 'load');
    await promise;
    expect(mockXhr.setRequestHeader).toHaveBeenCalledWith('Content-Type', 'application/pdf');
  });

  it('calls onProgress with 100 when done', async () => {
    const file = new File(['x'], 'f.pdf', { type: 'application/pdf' });
    const onProgress = vi.fn();
    const promise = uploadToPresigned('https://s3.example.com/key', file, onProgress);
    triggerXhrEvent(mockXhr, 'load');
    await promise;
    expect(onProgress).toHaveBeenCalledWith(100);
  });

  it('fires progress callback with correct percentage', async () => {
    const file = new File(['data'], 'f.pdf', { type: 'application/pdf' });
    const onProgress = vi.fn();
    const promise = uploadToPresigned('https://s3.example.com/key', file, onProgress);

    triggerUploadEvent(mockXhr, 'progress', { lengthComputable: true, loaded: 50, total: 100 });
    triggerXhrEvent(mockXhr, 'load');
    await promise;

    expect(onProgress).toHaveBeenCalledWith(50);
  });

  it('throws UploadError on non-2xx status', async () => {
    mockXhr.status = 403;
    mockXhr.statusText = 'Forbidden';
    const file = new File(['x'], 'f.pdf', { type: 'application/pdf' });
    const promise = uploadToPresigned('https://s3.example.com/key', file);
    triggerXhrEvent(mockXhr, 'load');
    await expect(promise).rejects.toBeInstanceOf(UploadError);
  });

  it('throws UploadError with status code on non-2xx', async () => {
    mockXhr.status = 403;
    mockXhr.statusText = 'Forbidden';
    const file = new File(['x'], 'f.pdf', { type: 'application/pdf' });
    const promise = uploadToPresigned('https://s3.example.com/key', file);
    triggerXhrEvent(mockXhr, 'load');
    await expect(promise).rejects.toMatchObject({ status: 403 });
  });

  it('throws UploadError on network error', async () => {
    const file = new File(['x'], 'f.pdf', { type: 'application/pdf' });
    const promise = uploadToPresigned('https://s3.example.com/key', file);
    triggerXhrEvent(mockXhr, 'error');
    await expect(promise).rejects.toBeInstanceOf(UploadError);
  });

  it('throws UploadError on abort', async () => {
    const file = new File(['x'], 'f.pdf', { type: 'application/pdf' });
    const promise = uploadToPresigned('https://s3.example.com/key', file);
    triggerXhrEvent(mockXhr, 'abort');
    await expect(promise).rejects.toBeInstanceOf(UploadError);
  });

  it('uses application/octet-stream when file.type is empty', async () => {
    const file = new File(['x'], 'f.bin', { type: '' });
    const promise = uploadToPresigned('https://s3.example.com/key', file);
    triggerXhrEvent(mockXhr, 'load');
    await promise;
    expect(mockXhr.setRequestHeader).toHaveBeenCalledWith('Content-Type', 'application/octet-stream');
  });
});
