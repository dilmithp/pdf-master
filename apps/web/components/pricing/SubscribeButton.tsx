"use client";

import { createCheckoutSession } from "@/app/(dashboard)/actions";
import { useTransition } from "react";

interface SubscribeButtonProps {
  readonly priceId: string | null;
  readonly planName: string;
  readonly highlighted: boolean;
}

export function SubscribeButton({ priceId, planName, highlighted }: SubscribeButtonProps) {
  const [isPending, startTransition] = useTransition();

  function handleClick() {
    if (!priceId) return;
    startTransition(async () => {
      await createCheckoutSession(priceId);
    });
  }

  if (!priceId) {
    return (
      <a
        href="/sign-up"
        className={`block w-full rounded-lg px-4 py-2 text-center text-sm font-semibold transition-colors ${
          highlighted
            ? "bg-blue-600 text-white hover:bg-blue-700"
            : "border border-neutral-300 text-neutral-700 hover:bg-neutral-50 dark:border-neutral-700 dark:text-neutral-300 dark:hover:bg-neutral-800"
        }`}
      >
        Get started free
      </a>
    );
  }

  return (
    <button
      type="button"
      onClick={handleClick}
      disabled={isPending}
      className={`w-full rounded-lg px-4 py-2 text-sm font-semibold transition-colors disabled:cursor-not-allowed disabled:opacity-60 ${
        highlighted
          ? "bg-blue-600 text-white hover:bg-blue-700"
          : "border border-neutral-300 text-neutral-700 hover:bg-neutral-50 dark:border-neutral-700 dark:text-neutral-300 dark:hover:bg-neutral-800"
      }`}
    >
      {isPending ? "Redirecting…" : `Subscribe to ${planName}`}
    </button>
  );
}
