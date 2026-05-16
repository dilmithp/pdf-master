import { auth } from "@clerk/nextjs/server";

/**
 * Returns the Clerk session JWT for use in server-side requests to the gateway.
 * Returns null when no active session exists.
 */
export async function getServerJwt(): Promise<string | null> {
  const session = await auth();
  if (!session.userId) return null;
  return session.getToken();
}
