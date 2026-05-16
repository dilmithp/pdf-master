import Link from 'next/link';
import { BRAND } from '@/lib/brand';
import { ForgotPasswordForm } from '@/components/auth/ForgotPasswordForm';

export const metadata = {
  title: `Reset Password | ${BRAND.name}`,
  robots: { index: false },
};

export default function ForgotPasswordPage() {
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
      <div className="w-full max-w-md rounded-2xl border border-neutral-200 bg-white p-8 shadow-lg dark:border-neutral-800 dark:bg-neutral-900">
        <h1 className="mb-2 text-xl font-semibold text-neutral-900 dark:text-white">Reset your password</h1>
        <p className="mb-6 text-sm text-neutral-500">
          Enter your email and we will send you a reset link.
        </p>
        <ForgotPasswordForm />
        <p className="mt-4 text-center text-sm text-neutral-500">
          Remembered it?{' '}
          <Link href="/sign-in" className="underline hover:text-neutral-900">
            Sign in
          </Link>
        </p>
      </div>
      <p className="text-sm text-neutral-500">
        <Link href="/terms" className="underline hover:text-neutral-900">Terms</Link>
        {' · '}
        <Link href="/privacy" className="underline hover:text-neutral-900">Privacy</Link>
      </p>
    </div>
  );
}
