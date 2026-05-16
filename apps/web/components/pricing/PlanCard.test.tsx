import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import { PlanCard } from "./PlanCard";
import type { Plan } from "@/lib/billing/plans";

// SubscribeButton uses useTransition and Server Action — mock it for isolation
vi.mock("./SubscribeButton", () => ({
  SubscribeButton: ({ planName, priceId }: { planName: string; priceId: string | null }) => (
    <button type="button">
      {priceId ? `Subscribe to ${planName}` : "Get started free"}
    </button>
  ),
}));

const FREE_PLAN: Plan = {
  id: "free",
  name: "Free",
  description: "For occasional use",
  monthlyPrice: 0,
  yearlyPrice: 0,
  stripePriceIdMonthly: null,
  stripePriceIdYearly: null,
  features: [
    { label: "10 operations per day", included: true },
    { label: "AI features", included: false },
  ],
  limits: { opsPerDay: 10, maxFileSizeMb: 50, seats: 1 },
  highlighted: false,
};

const PRO_PLAN: Plan = {
  id: "pro",
  name: "Pro",
  description: "Unlimited operations",
  monthlyPrice: 9,
  yearlyPrice: 84,
  stripePriceIdMonthly: "price_pro_monthly",
  stripePriceIdYearly: "price_pro_yearly",
  features: [
    { label: "Unlimited operations", included: true },
    { label: "AI features", included: true },
    { label: "Priority queue", included: false },
  ],
  limits: { opsPerDay: "unlimited", maxFileSizeMb: 500, seats: 1 },
  highlighted: true,
};

describe("PlanCard", () => {
  it("renders plan name and description", () => {
    render(<PlanCard plan={FREE_PLAN} />);
    expect(screen.getByText("Free")).toBeDefined();
    expect(screen.getByText("For occasional use")).toBeDefined();
  });

  it("renders 'Free' price label for zero-cost plan", () => {
    render(<PlanCard plan={FREE_PLAN} />);
    expect(screen.getByText("Free")).toBeDefined();
  });

  it("renders monthly price for paid plan", () => {
    render(<PlanCard plan={PRO_PLAN} />);
    expect(screen.getByText("$9")).toBeDefined();
    expect(screen.getByText("/ month", { exact: false })).toBeDefined();
  });

  it("renders all feature labels", () => {
    render(<PlanCard plan={PRO_PLAN} />);
    expect(screen.getByText("Unlimited operations")).toBeDefined();
    expect(screen.getByText("AI features")).toBeDefined();
    expect(screen.getByText("Priority queue")).toBeDefined();
  });

  it("shows 'Most popular' badge only on highlighted plan", () => {
    const { rerender } = render(<PlanCard plan={PRO_PLAN} />);
    expect(screen.getByText("Most popular")).toBeDefined();

    rerender(<PlanCard plan={FREE_PLAN} />);
    expect(screen.queryByText("Most popular")).toBeNull();
  });

  it("renders CTA with correct label for free plan", () => {
    render(<PlanCard plan={FREE_PLAN} />);
    expect(screen.getByText("Get started free")).toBeDefined();
  });

  it("renders CTA with correct label for pro plan", () => {
    render(<PlanCard plan={PRO_PLAN} />);
    expect(screen.getByText("Subscribe to Pro")).toBeDefined();
  });
});
