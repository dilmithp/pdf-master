import { SignIn } from '@clerk/nextjs';
import Link from 'next/link';
import { BRAND } from '@/lib/brand';

export const metadata = {
  title: `Sign In | ${BRAND.name}`,
  robots: { index: false },
};

export default function SignInPage() {
  if (!process.env.NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <p className="text-neutral-500">Auth not configured — set NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY.</p>
      </div>
    );
  }

  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-8 bg-neutral-50 px-4 dark:bg-neutral-950">
      <Link href="/" className="text-2xl font-bold tracking-tight text-neutral-900 dark:text-white">
        {BRAND.name}
      </Link>
      <SignIn
        appearance={{
          elements: {
            rootBox: 'w-full max-w-md',
            card: 'shadow-lg rounded-2xl border border-neutral-200 dark:border-neutral-800',
          },
        }}
      />
      <p className="text-sm text-neutral-500">
        By signing in you agree to our{' '}
        <Link href="/terms" className="underline hover:text-neutral-900">
          Terms
        </Link>{' '}
        and{' '}
        <Link href="/privacy" className="underline hover:text-neutral-900">
          Privacy Policy
        </Link>
        .
      </p>
    </div>
  );
}
