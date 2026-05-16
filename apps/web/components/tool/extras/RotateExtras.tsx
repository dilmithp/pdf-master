"use client";

import { useState } from "react";

export type RotateAngle = 90 | 180 | 270;

export interface RotateExtrasValue {
  angle: RotateAngle;
  applyToAll: boolean;
}

interface RotateExtrasProps {
  onChange: (value: RotateExtrasValue) => void;
}

const ANGLE_OPTIONS: { value: RotateAngle; label: string }[] = [
  { value: 90, label: "90° clockwise" },
  { value: 180, label: "180°" },
  { value: 270, label: "90° counter-clockwise" },
];

export function RotateExtras({ onChange }: RotateExtrasProps) {
  const [angle, setAngle] = useState<RotateAngle>(90);
  const [applyToAll, setApplyToAll] = useState(true);

  function emit(updates: Partial<{ angle: RotateAngle; applyToAll: boolean }>) {
    const next = { angle, applyToAll, ...updates };
    onChange(next);
  }

  return (
    <fieldset className="space-y-3 rounded-lg border border-neutral-200 p-4 dark:border-neutral-700">
      <legend className="px-1 text-sm font-medium">Rotation options</legend>

      <div className="flex flex-wrap gap-3">
        {ANGLE_OPTIONS.map(({ value, label }) => (
          <label key={value} className="flex items-center gap-2 cursor-pointer text-sm">
            <input
              type="radio"
              name="rotate-angle"
              value={value}
              checked={angle === value}
              onChange={() => {
                setAngle(value);
                emit({ angle: value });
              }}
            />
            {label}
          </label>
        ))}
      </div>

      <label className="flex items-center gap-2 cursor-pointer text-sm">
        <input
          type="checkbox"
          checked={applyToAll}
          onChange={(e) => {
            setApplyToAll(e.target.checked);
            emit({ applyToAll: e.target.checked });
          }}
        />
        Apply to all pages
      </label>
    </fieldset>
  );
}
