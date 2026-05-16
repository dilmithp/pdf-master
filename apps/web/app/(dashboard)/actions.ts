"use server";

import { auth, currentUser } from "@clerk/nextjs/server";
import { redirect } from "next/navigation";
import { getStripe } from "@/lib/billing/stripe";
import { getServerJwt } from "@/lib/auth/jwt";

export async function createCheckoutSession(planPriceId: string): Promise<void> {
  const session = await auth();
  if (!session.userId) redirect("/sign-in");

  const user = await currentUser();
  const stripe = getStripe();

  const email = user?.primaryEmailAddress?.emailAddress;
  const checkoutSession = await stripe.checkout.sessions.create({
    mode: "subscription",
    line_items: [{ price: planPriceId, quantity: 1 }],
    success_url: `${process.env.NEXT_PUBLIC_SITE_URL}/dashboard?session_id={CHECKOUT_SESSION_ID}`,
    cancel_url: `${process.env.NEXT_PUBLIC_SITE_URL}/pricing?cancelled=true`,
    client_reference_id: session.userId,
    ...(email ? { customer_email: email } : {}),
  });

  if (!checkoutSession.url) throw new Error("Stripe did not return a checkout URL.");
  redirect(checkoutSession.url);
}

export async function manageSubscription(): Promise<void> {
  const clerkSession = await auth();
  if (!clerkSession.userId) redirect("/sign-in");

  const stripe = getStripe();
  const siteUrl = process.env.NEXT_PUBLIC_SITE_URL ?? "";

  // Look up the Stripe customer by metadata userId
  const customers = await stripe.customers.search({
    query: `metadata['clerk_user_id']:'${clerkSession.userId}'`,
    limit: 1,
  });

  const customerId = customers.data[0]?.id;
  if (!customerId) redirect("/pricing");

  const portalSession = await stripe.billingPortal.sessions.create({
    customer: customerId,
    return_url: `${siteUrl}/dashboard/billing`,
  });

  redirect(portalSession.url);
}

export async function deleteAccount(): Promise<void> {
  const clerkSession = await auth();
  if (!clerkSession.userId) redirect("/sign-in");

  const jwt = await getServerJwt();
  const gatewayUrl = process.env.NEXT_PUBLIC_GATEWAY_URL ?? "http://localhost:8080";

  const res = await fetch(`${gatewayUrl}/v1/account`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${jwt}`,
    },
  });

  if (!res.ok && res.status !== 404) {
    throw new Error(`Failed to delete account: ${res.status}`);
  }

  redirect("/");
}
