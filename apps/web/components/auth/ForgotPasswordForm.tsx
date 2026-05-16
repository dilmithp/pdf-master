"use client";

import { useSignIn } from "@clerk/nextjs";
import { useState } from "react";

type Step = "email" | "code" | "done";

export function ForgotPasswordForm() {
  const { signIn, isLoaded } = useSignIn();
  const [step, setStep] = useState<Step>("email");
  const [email, setEmail] = useState("");
  const [code, setCode] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  async function handleRequestReset(e: React.FormEvent) {
    e.preventDefault();
    if (!isLoaded) return;
    setError(null);
    setLoading(true);
    try {
      await signIn.create({ strategy: "reset_password_email_code", identifier: email });
      setStep("code");
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Failed to send reset email.");
    } finally {
      setLoading(false);
    }
  }

  async function handleResetPassword(e: React.FormEvent) {
    e.preventDefault();
    if (!isLoaded) return;
    setError(null);
    setLoading(true);
    try {
      const result = await signIn.attemptFirstFactor({
        strategy: "reset_password_email_code",
        code,
        password: newPassword,
      });
      if (result.status === "complete") setStep("done");
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Reset failed. Check code and try again.");
    } finally {
      setLoading(false);
    }
  }

  if (step === "done") {
    return (
      <p className="text-center text-sm text-green-600">
        Password reset! You can now{" "}
        <a href="/sign-in" className="underline">
          sign in
        </a>
        .
      </p>
    );
  }

  if (step === "code") {
    return (
      <form onSubmit={handleResetPassword} className="flex flex-col gap-4">
        {error && <p className="text-sm text-red-600">{error}</p>}
        <label className="flex flex-col gap-1 text-sm font-medium text-neutral-700 dark:text-neutral-300">
          Reset code
          <input
            type="text"
            value={code}
            onChange={(e) => setCode(e.target.value)}
            required
            className="rounded-lg border border-neutral-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:border-neutral-700 dark:bg-neutral-800"
          />
        </label>
        <label className="flex flex-col gap-1 text-sm font-medium text-neutral-700 dark:text-neutral-300">
          New password
          <input
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
            minLength={8}
            className="rounded-lg border border-neutral-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:border-neutral-700 dark:bg-neutral-800"
          />
        </label>
        <button
          type="submit"
          disabled={loading}
          className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? "Resetting…" : "Set new password"}
        </button>
      </form>
    );
  }

  return (
    <form onSubmit={handleRequestReset} className="flex flex-col gap-4">
      {error && <p className="text-sm text-red-600">{error}</p>}
      <label className="flex flex-col gap-1 text-sm font-medium text-neutral-700 dark:text-neutral-300">
        Email address
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          autoComplete="email"
          className="rounded-lg border border-neutral-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:border-neutral-700 dark:bg-neutral-800"
        />
      </label>
      <button
        type="submit"
        disabled={loading}
        className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700 disabled:opacity-50"
      >
        {loading ? "Sending…" : "Send reset link"}
      </button>
    </form>
  );
}
