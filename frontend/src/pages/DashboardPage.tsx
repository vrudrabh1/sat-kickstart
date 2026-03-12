import { useEffect, useState } from 'react';
import { DifficultyBadge } from '../components/DifficultyBadge';
import { useSessionId } from '../hooks/useSessionId';
import { progressApi } from '../services/api';
import type { ProgressEntry } from '../types';

export function DashboardPage() {
  const sessionId = useSessionId();
  const [progress, setProgress] = useState<ProgressEntry[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    progressApi
      .getProgress(sessionId)
      .then(setProgress)
      .finally(() => setLoading(false));
  }, [sessionId]);

  return (
    <div style={{ padding: '32px 24px', maxWidth: 760, margin: '0 auto' }}>
      <h1 style={{ fontSize: 24, fontWeight: 700, marginBottom: 24 }}>Your Progress</h1>

      {loading && <p style={{ color: '#64748b' }}>Loading…</p>}

      {!loading && progress.length === 0 && (
        <div
          style={{
            padding: '40px 24px',
            textAlign: 'center',
            background: '#f8fafc',
            borderRadius: 12,
            border: '1px dashed #cbd5e1',
          }}
        >
          <p style={{ color: '#64748b', marginBottom: 16 }}>
            No practice sessions yet. Start practising to see your stats here!
          </p>
          <a
            href="/practice"
            style={{
              background: '#1e40af',
              color: '#fff',
              padding: '10px 24px',
              borderRadius: 6,
              textDecoration: 'none',
              fontWeight: 600,
            }}
          >
            Start Practising
          </a>
        </div>
      )}

      {!loading && progress.length > 0 && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          {progress.map((entry) => (
            <div
              key={entry.section}
              style={{
                padding: '20px 24px',
                background: '#fff',
                borderRadius: 12,
                border: '1px solid #e2e8f0',
                boxShadow: '0 1px 3px rgba(0,0,0,0.05)',
              }}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
                <h2 style={{ fontSize: 18, fontWeight: 700, margin: 0 }}>
                  {entry.section === 'MATH' ? '📐 Math' : '📖 Reading & Writing'}
                </h2>
                <DifficultyBadge level={entry.difficultyLevel} />
              </div>

              <div style={{ display: 'flex', gap: 32 }}>
                <Stat label="Questions" value={entry.totalAttempts.toString()} />
                <Stat label="Correct" value={entry.correctAttempts.toString()} />
                <Stat label="Accuracy" value={`${entry.accuracy.toFixed(1)}%`} highlight />
              </div>

              {/* Simple progress bar */}
              <div
                style={{
                  marginTop: 16,
                  height: 8,
                  borderRadius: 4,
                  background: '#e2e8f0',
                  overflow: 'hidden',
                }}
              >
                <div
                  style={{
                    height: '100%',
                    width: `${Math.min(entry.accuracy, 100)}%`,
                    background: entry.accuracy >= 70 ? '#10b981' : entry.accuracy >= 40 ? '#f59e0b' : '#ef4444',
                    borderRadius: 4,
                    transition: 'width 0.6s ease',
                  }}
                />
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

function Stat({ label, value, highlight }: { label: string; value: string; highlight?: boolean }) {
  return (
    <div>
      <p style={{ fontSize: 12, color: '#94a3b8', margin: '0 0 4px' }}>{label}</p>
      <p style={{ fontSize: 22, fontWeight: 700, margin: 0, color: highlight ? '#1e40af' : '#1e293b' }}>
        {value}
      </p>
    </div>
  );
}
