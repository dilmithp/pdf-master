import { describe, it, expect, vi, beforeEach } from "vitest";

// ---------------------------------------------------------------------------
// Module mocks — hoisted before imports
// ---------------------------------------------------------------------------
vi.mock("@clerk/nextjs/server", () => ({
  auth: vi.fn(),
  currentUser: vi.fn(),
}));

vi.mock("next/navigation", () => ({
  redirect: vi.fn(),
}));

vi.mock("@/lib/billing/stripe", () => ({
  getStripe: vi.fn(),
}));

vi.mock("@/lib/auth/jwt", () => ({
  getServerJwt: vi.fn(),
}));

import * as clerkServer from "@clerk/nextjs/server";
import * as navigation from "next/navigation";
import * as billingStripe from "@/lib/billing/stripe";
import * as authJwt from "@/lib/auth/jwt";
import { createCheckoutSession, deleteAccount } from "./actions";

// Stripe mock factory
function makeStripeMock(sessionUrl = "https://checkout.stripe.com/test") {
  return {
    checkout: {
      sessions: {
        create: vi.fn().mockResolvedValue({ url: sessionUrl }),
      },
    },
    customers: {
      search: vi.fn().mockResolvedValue({ data: [] }),
    },
    billingPortal: {
      sessions: {
        create: vi.fn().mockResolvedValue({ url: "https://portal.stripe.com/test" }),
      },
    },
  };
}

beforeEach(() => {
  vi.clearAllMocks();
  // Make redirect throw so async functions abort at redirect call
  vi.spyOn(navigation, "redirect").mockImplementation(() => {
    throw new Error("NEXT_REDIRECT");
  });
});

describe("createCheckoutSession", () => {
  it("redirects to /sign-in when userId is null", async () => {
    vi.spyOn(clerkServer, "auth").mockResolvedValue({ userId: null } as never);

    await expect(createCheckoutSession("price_test")).rejects.toThrow("NEXT_REDIRECT");
    expect(navigation.redirect).toHaveBeenCalledWith("/sign-in");
  });

  it("creates Stripe checkout session with correct params", async () => {
    vi.spyOn(clerkServer, "auth").mockResolvedValue({ userId: "user_123" } as never);
    vi.spyOn(clerkServer, "currentUser").mockResolvedValue({
      primaryEmailAddress: { emailAddress: "test@example.com" },
    } as never);

    const stripeMock = makeStripeMock();
    vi.spyOn(billingStripe, "getStripe").mockReturnValue(stripeMock as never);

    await expect(createCheckoutSession("price_pro_monthly")).rejects.toThrow("NEXT_REDIRECT");

    expect(stripeMock.checkout.sessions.create).toHaveBeenCalledWith(
      expect.objectContaining({
        mode: "subscription",
        line_items: [{ price: "price_pro_monthly", quantity: 1 }],
        client_reference_id: "user_123",
        customer_email: "test@example.com",
      })
    );
  });

  it("includes success_url and cancel_url with correct paths", async () => {
    process.env.NEXT_PUBLIC_SITE_URL = "https://pdfmaster.app";
    vi.spyOn(clerkServer, "auth").mockResolvedValue({ userId: "user_456" } as never);
    vi.spyOn(clerkServer, "currentUser").mockResolvedValue(null as never);

    const stripeMock = makeStripeMock();
    vi.spyOn(billingStripe, "getStripe").mockReturnValue(stripeMock as never);

    await expect(createCheckoutSession("price_team_monthly")).rejects.toThrow("NEXT_REDIRECT");

    const callArgs = stripeMock.checkout.sessions.create.mock.calls[0]?.[0] as {
      success_url: string;
      cancel_url: string;
    };
    expect(callArgs.success_url).toContain("/dashboard");
    expect(callArgs.cancel_url).toContain("/pricing");
  });
});

describe("deleteAccount", () => {
  it("redirects to /sign-in when userId is null", async () => {
    vi.spyOn(clerkServer, "auth").mockResolvedValue({ userId: null } as never);

    await expect(deleteAccount()).rejects.toThrow("NEXT_REDIRECT");
    expect(navigation.redirect).toHaveBeenCalledWith("/sign-in");
  });

  it("calls gateway DELETE /v1/account with Bearer token", async () => {
    vi.spyOn(clerkServer, "auth").mockResolvedValue({ userId: "user_789" } as never);
    vi.spyOn(authJwt, "getServerJwt").mockResolvedValue("test-jwt-token");

    global.fetch = vi.fn().mockResolvedValue({ ok: true, status: 200 } as Response);

    await expect(deleteAccount()).rejects.toThrow("NEXT_REDIRECT");

    expect(global.fetch).toHaveBeenCalledWith(
      expect.stringContaining("/v1/account"),
      expect.objectContaining({
        method: "DELETE",
        headers: expect.objectContaining({ Authorization: "Bearer test-jwt-token" }),
      })
    );
    expect(navigation.redirect).toHaveBeenCalledWith("/");
  });

  it("throws when gateway returns non-ok, non-404 status", async () => {
    vi.spyOn(clerkServer, "auth").mockResolvedValue({ userId: "user_789" } as never);
    vi.spyOn(authJwt, "getServerJwt").mockResolvedValue("test-jwt-token");

    global.fetch = vi.fn().mockResolvedValue({ ok: false, status: 500 } as Response);

    await expect(deleteAccount()).rejects.toThrow("Failed to delete account: 500");
  });
});
