import Stripe from "stripe";

let stripeInstance: Stripe | null = null;

/**
 * Returns a singleton Stripe server SDK instance.
 * Throws at runtime (not at import time) when the secret key is absent,
 * so builds without Stripe keys still succeed.
 */
export function getStripe(): Stripe {
  if (!stripeInstance) {
    const key = process.env.STRIPE_SECRET_KEY;
    if (!key) {
      throw new Error("STRIPE_SECRET_KEY is not set.");
    }
    stripeInstance = new Stripe(key);
  }
  return stripeInstance;
}
