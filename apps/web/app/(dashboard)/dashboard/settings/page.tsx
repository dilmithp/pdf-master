import { auth } from "@clerk/nextjs/server";
import { redirect } from "next/navigation";
import { UserProfile } from "@clerk/nextjs";
import { DeleteAccountDialog } from "@/components/dashboard/DeleteAccountDialog";

export const metadata = { title: "Settings", robots: { index: false } };

export default async function SettingsPage() {
  const clerkSession = await auth();
  if (!clerkSession.userId) redirect("/sign-in");

  return (
    <div className="mx-auto max-w-3xl space-y-10 px-4 py-10">
      <h1 className="text-2xl font-bold text-neutral-900 dark:text-white">Account Settings</h1>

      <section>
        <UserProfile
          appearance={{
            elements: {
              rootBox: "w-full",
              card: "shadow-sm rounded-xl border border-neutral-200 dark:border-neutral-800",
            },
          }}
        />
      </section>

      <section className="rounded-xl border border-red-200 bg-red-50 p-6 dark:border-red-900 dark:bg-red-950">
        <h2 className="mb-2 text-lg font-semibold text-red-700 dark:text-red-400">Danger zone</h2>
        <p className="mb-4 text-sm text-red-600 dark:text-red-400">
          Permanently delete your account and all associated data. This action cannot be undone.
        </p>
        <DeleteAccountDialog />
      </section>
    </div>
  );
}
