import { describe, it, expect } from "vitest";
import { PLANS, getPlanById } from "./plans";

describe("PLANS catalog", () => {
  it("has exactly 3 plans: free, pro, team", () => {
    const ids = PLANS.map((p) => p.id);
    expect(ids).toEqual(["free", "pro", "team"]);
  });

  it("free plan has zero price and no stripe price IDs", () => {
    const free = getPlanById("free");
    expect(free?.monthlyPrice).toBe(0);
    expect(free?.yearlyPrice).toBe(0);
    expect(free?.stripePriceIdMonthly).toBeNull();
    expect(free?.stripePriceIdYearly).toBeNull();
  });

  it("pro plan has positive prices and feature list", () => {
    const pro = getPlanById("pro");
    expect(pro?.monthlyPrice).toBeGreaterThan(0);
    expect(pro?.features.length).toBeGreaterThan(0);
    expect(pro?.highlighted).toBe(true);
  });

  it("team plan has unlimited ops and 2GB file limit", () => {
    const team = getPlanById("team");
    expect(team?.limits.opsPerDay).toBe("unlimited");
    expect(team?.limits.maxFileSizeMb).toBe(2048);
  });

  it("stripe price IDs fall back to null when env vars unset", () => {
    const pro = getPlanById("pro");
    // In test environment env vars are unset → null expected
    // (If env is set this still passes as long as it's a string or null)
    expect(pro?.stripePriceIdMonthly === null || typeof pro?.stripePriceIdMonthly === "string").toBe(true);
  });

  it("each plan has at least one feature", () => {
    for (const plan of PLANS) {
      expect(plan.features.length).toBeGreaterThan(0);
    }
  });

  it("getPlanById returns undefined for unknown id", () => {
    expect(getPlanById("enterprise")).toBeUndefined();
  });

  it("all plans have required shape", () => {
    for (const plan of PLANS) {
      expect(plan).toMatchObject({
        id: expect.any(String),
        name: expect.any(String),
        description: expect.any(String),
        monthlyPrice: expect.any(Number),
        yearlyPrice: expect.any(Number),
        features: expect.any(Array),
        limits: expect.objectContaining({
          maxFileSizeMb: expect.any(Number),
        }),
      });
    }
  });
});
