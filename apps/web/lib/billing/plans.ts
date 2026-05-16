export interface PlanFeature {
  readonly label: string;
  readonly included: boolean;
}

export interface Plan {
  readonly id: "free" | "pro" | "team";
  readonly name: string;
  readonly description: string;
  readonly monthlyPrice: number;
  readonly yearlyPrice: number;
  readonly stripePriceIdMonthly: string | null;
  readonly stripePriceIdYearly: string | null;
  readonly features: readonly PlanFeature[];
  readonly limits: {
    readonly opsPerDay: number | "unlimited";
    readonly maxFileSizeMb: number;
    readonly seats: number | "unlimited";
  };
  readonly highlighted: boolean;
}

export const PLANS: readonly Plan[] = [
  {
    id: "free",
    name: "Free",
    description: "For occasional use — no credit card required.",
    monthlyPrice: 0,
    yearlyPrice: 0,
    stripePriceIdMonthly: null,
    stripePriceIdYearly: null,
    features: [
      { label: "10 operations per day", included: true },
      { label: "Up to 50 MB per file", included: true },
      { label: "Files deleted in 60 minutes", included: true },
      { label: "AI features", included: false },
      { label: "Priority queue", included: false },
      { label: "API access", included: false },
    ],
    limits: { opsPerDay: 10, maxFileSizeMb: 50, seats: 1 },
    highlighted: false,
  },
  {
    id: "pro",
    name: "Pro",
    description: "Unlimited operations with AI-powered tools.",
    monthlyPrice: 9,
    yearlyPrice: 84,
    stripePriceIdMonthly: process.env.STRIPE_PRICE_PRO_MONTHLY ?? null,
    stripePriceIdYearly: process.env.STRIPE_PRICE_PRO_YEARLY ?? null,
    features: [
      { label: "Unlimited operations", included: true },
      { label: "Up to 500 MB per file", included: true },
      { label: "Files deleted in 60 minutes", included: true },
      { label: "AI features (chat with PDF, OCR+AI)", included: true },
      { label: "Priority queue", included: false },
      { label: "API access", included: false },
    ],
    limits: { opsPerDay: "unlimited", maxFileSizeMb: 500, seats: 1 },
    highlighted: true,
  },
  {
    id: "team",
    name: "Team",
    description: "Collaborative workspace with priority processing.",
    monthlyPrice: 29,
    yearlyPrice: 276,
    stripePriceIdMonthly: process.env.STRIPE_PRICE_TEAM_MONTHLY ?? null,
    stripePriceIdYearly: process.env.STRIPE_PRICE_TEAM_YEARLY ?? null,
    features: [
      { label: "Unlimited operations", included: true },
      { label: "Up to 2 GB per file", included: true },
      { label: "Files deleted in 60 minutes", included: true },
      { label: "AI features (chat with PDF, OCR+AI)", included: true },
      { label: "Priority queue", included: true },
      { label: "API access", included: true },
    ],
    limits: { opsPerDay: "unlimited", maxFileSizeMb: 2048, seats: "unlimited" },
    highlighted: false,
  },
] as const;

export function getPlanById(id: string): Plan | undefined {
  return PLANS.find((p) => p.id === id);
}
