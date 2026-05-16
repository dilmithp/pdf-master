"use client";

import { useState, useRef } from "react";
import { deleteAccount } from "@/app/(dashboard)/actions";

const CONFIRM_PHRASE = "delete my account";

export function DeleteAccountDialog() {
  const [open, setOpen] = useState(false);
  const [input, setInput] = useState("");
  const [pending, setPending] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const dialogRef = useRef<HTMLDialogElement>(null);

  function handleOpen() {
    setInput("");
    setError(null);
    setOpen(true);
    dialogRef.current?.showModal();
  }

  function handleClose() {
    dialogRef.current?.close();
    setOpen(false);
  }

  async function handleDelete() {
    if (input !== CONFIRM_PHRASE) return;
    setPending(true);
    setError(null);
    try {
      await deleteAccount();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Failed to delete account.");
      setPending(false);
    }
  }

  const confirmed = input === CONFIRM_PHRASE;

  return (
    <>
      <button
        type="button"
        onClick={handleOpen}
        className="rounded-lg border border-red-300 bg-white px-4 py-2 text-sm font-medium text-red-600 hover:bg-red-50 dark:border-red-800 dark:bg-transparent dark:hover:bg-red-900/30"
      >
        Delete account
      </button>

      {open && (
        <dialog
          ref={dialogRef}
          onClose={handleClose}
          className="rounded-2xl border border-neutral-200 bg-white p-8 shadow-xl dark:border-neutral-800 dark:bg-neutral-900 backdrop:bg-black/50"
        >
          <h2 className="mb-2 text-lg font-semibold text-neutral-900 dark:text-white">
            Are you absolutely sure?
          </h2>
          <p className="mb-4 text-sm text-neutral-500">
            This will permanently delete your account and all associated data. This action cannot be
            undone.
          </p>
          <p className="mb-2 text-sm font-medium text-neutral-700 dark:text-neutral-300">
            Type <span className="font-mono text-red-600">{CONFIRM_PHRASE}</span> to confirm:
          </p>
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder={CONFIRM_PHRASE}
            className="mb-4 w-full rounded-lg border border-neutral-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-red-500 dark:border-neutral-700 dark:bg-neutral-800"
          />
          {error && <p className="mb-3 text-sm text-red-600">{error}</p>}
          <div className="flex justify-end gap-3">
            <button
              type="button"
              onClick={handleClose}
              className="rounded-lg border border-neutral-300 px-4 py-2 text-sm font-medium text-neutral-700 hover:bg-neutral-50 dark:border-neutral-700 dark:text-neutral-300"
            >
              Cancel
            </button>
            <button
              type="button"
              onClick={handleDelete}
              disabled={!confirmed || pending}
              className="rounded-lg bg-red-600 px-4 py-2 text-sm font-semibold text-white hover:bg-red-700 disabled:cursor-not-allowed disabled:opacity-50"
            >
              {pending ? "Deleting…" : "Delete my account"}
            </button>
          </div>
        </dialog>
      )}
    </>
  );
}
