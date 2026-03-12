import type { DifficultyLevel } from '../types';

interface DifficultyBadgeProps {
  level: DifficultyLevel;
}

const styles: Record<DifficultyLevel, string> = {
  EASY: 'background:#d1fae5;color:#065f46;',
  MEDIUM: 'background:#fef3c7;color:#92400e;',
  HARD: 'background:#fee2e2;color:#991b1b;',
};

export function DifficultyBadge({ level }: DifficultyBadgeProps) {
  return (
    <span
      style={{
        ...parseStyle(styles[level]),
        padding: '2px 10px',
        borderRadius: 12,
        fontSize: 12,
        fontWeight: 600,
        textTransform: 'uppercase',
        letterSpacing: '0.05em',
      }}
    >
      {level}
    </span>
  );
}

function parseStyle(s: string): Record<string, string> {
  return Object.fromEntries(
    s.split(';').filter(Boolean).map((kv) => kv.split(':').map((x) => x.trim()) as [string, string])
  );
}
