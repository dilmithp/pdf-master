export class UploadError extends Error {
  constructor(
    message: string,
    public readonly status: number,
  ) {
    super(message);
    this.name = "UploadError";
  }
}

export async function uploadToPresigned(
  url: string,
  file: File,
  onProgress?: (pct: number) => void,
): Promise<void> {
  return new Promise<void>((resolve, reject) => {
    const xhr = new XMLHttpRequest();

    xhr.upload.addEventListener("progress", (evt) => {
      if (evt.lengthComputable && onProgress) {
        onProgress(Math.round((evt.loaded / evt.total) * 100));
      }
    });

    xhr.addEventListener("load", () => {
      if (xhr.status >= 200 && xhr.status < 300) {
        onProgress?.(100);
        resolve();
      } else {
        reject(new UploadError(`S3 PUT failed: ${xhr.status} ${xhr.statusText}`, xhr.status));
      }
    });

    xhr.addEventListener("error", () => {
      reject(new UploadError("Network error during upload", 0));
    });

    xhr.addEventListener("abort", () => {
      reject(new UploadError("Upload aborted", 0));
    });

    xhr.open("PUT", url);
    xhr.setRequestHeader("Content-Type", file.type || "application/octet-stream");
    xhr.send(file);
  });
}
