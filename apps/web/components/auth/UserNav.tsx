"use client";

import { SignedIn, SignedOut, UserButton } from "@clerk/nextjs";
import Link from "next/link";

export function UserNav() {
  return (
    <nav className="flex items-center gap-3">
      <SignedIn>
        <Link
          href="/dashboard"
          className="text-sm font-medium text-neutral-600 hover:text-neutral-900 dark:text-neutral-400 dark:hover:text-white"
        >
          Dashboard
        </Link>
        <UserButton afterSignOutUrl="/" />
      </SignedIn>
      <SignedOut>
        <Link
          href="/sign-in"
          className="text-sm font-medium text-neutral-600 hover:text-neutral-900 dark:text-neutral-400 dark:hover:text-white"
        >
          Sign in
        </Link>
        <Link
          href="/sign-up"
          className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700"
        >
          Sign up
        </Link>
      </SignedOut>
    </nav>
  );
}
