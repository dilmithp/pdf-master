"use server";

import { cookies } from "next/headers";
import { createJob } from "@/lib/api/gateway";
import type { JobCreateResponse } from "@/lib/api/gateway";

export async function startJob(op: string, body: unknown): Promise<JobCreateResponse> {
  const cookieStore = await cookies();
  const jwt = cookieStore.get(process.env["AUTH_COOKIE_NAME"] ?? "__session")?.value;
  return createJob(op, body, jwt);
}
