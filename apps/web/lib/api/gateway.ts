const GATEWAY_URL = process.env.NEXT_PUBLIC_GATEWAY_URL ?? "http://localhost:8080";

export type JobStatus = "QUEUED" | "RUNNING" | "SUCCEEDED" | "FAILED";

export interface UploadUrl {
  url: string;
  key: string;
  expiresAt: string;
}

export interface JobCreateResponse {
  jobId: string;
  uploadUrls: UploadUrl[];
}

export interface JobStatusResponse {
  jobId: string;
  status: JobStatus;
  outputKey?: string;
  error?: string;
  createdAt: string;
  updatedAt: string;
}

export interface JobResultResponse {
  downloadUrl: string;
  expiresAt: string;
}

async function request<T>(path: string, init: RequestInit): Promise<T> {
  const res = await fetch(`${GATEWAY_URL}${path}`, init);
  if (!res.ok) {
    const text = await res.text().catch(() => res.statusText);
    throw new Error(`Gateway ${init.method ?? "GET"} ${path} failed: ${res.status} ${text}`);
  }
  return res.json() as Promise<T>;
}

function authHeaders(jwt?: string): Record<string, string> {
  if (!jwt) return {};
  return { Authorization: `Bearer ${jwt}` };
}

export async function createJob(
  op: string,
  body: unknown,
  jwt?: string,
): Promise<JobCreateResponse> {
  return request<JobCreateResponse>(`/v1/jobs/${op}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...authHeaders(jwt),
    },
    body: JSON.stringify(body),
  });
}

export async function getJobStatus(id: string, jwt?: string): Promise<JobStatusResponse> {
  return request<JobStatusResponse>(`/v1/jobs/${id}`, {
    method: "GET",
    headers: authHeaders(jwt),
  });
}

export async function getJobResult(id: string, jwt?: string): Promise<JobResultResponse> {
  return request<JobResultResponse>(`/v1/jobs/${id}/result`, {
    method: "GET",
    headers: authHeaders(jwt),
  });
}
