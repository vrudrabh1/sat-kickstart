import { useState } from 'react';
import { DifficultyBadge } from '../components/DifficultyBadge';
import { QuestionCard } from '../components/QuestionCard';
import { usePractice } from '../hooks/usePractice';
import { useSessionId } from '../hooks/useSessionId';
import type { Section } from '../types';

export function PracticePage() {
  const sessionId = useSessionId();
  const [section, setSection] = useState<Section>('MATH');
  const { question, loading, error, lastResult, difficultyLevel, totalAttempts, accuracy, loadNextQuestion, submitAnswer } =
    usePractice(sessionId, section);

  return (
    <div style={{ padding: '32px 24px', maxWidth: 760, margin: '0 auto' }}>
      <h1 style={{ fontSize: 24, fontWeight: 700, marginBottom: 8 }}>Practice Mode</h1>

      {/* Section selector */}
      <div style={{ display: 'flex', gap: 8, marginBottom: 24 }}>
        {(['MATH', 'ENGLISH'] as Section[]).map((s) => (
          <button
            key={s}
            onClick={() => setSection(s)}
            style={{
              padding: '8px 20px',
              borderRadius: 6,
              border: '1px solid #cbd5e1',
              background: section === s ? '#1e40af' : '#fff',
              color: section === s ? '#fff' : '#334155',
              cursor: 'pointer',
              fontWeight: 600,
              fontSize: 14,
            }}
          >
            {s === 'MATH' ? 'Math' : 'Reading & Writing'}
          </button>
        ))}
      </div>

      {/* Stats bar */}
      <div
        style={{
          display: 'flex',
          gap: 24,
          marginBottom: 32,
          padding: '12px 16px',
          background: '#f8fafc',
          borderRadius: 8,
          border: '1px solid #e2e8f0',
          alignItems: 'center',
          flexWrap: 'wrap',
        }}
      >
        <span style={{ fontSize: 13, color: '#64748b' }}>
          Difficulty: <DifficultyBadge level={difficultyLevel} />
        </span>
        <span style={{ fontSize: 13, color: '#64748b' }}>
          Answered: <strong>{totalAttempts}</strong>
        </span>
        <span style={{ fontSize: 13, color: '#64748b' }}>
          Accuracy: <strong>{accuracy.toFixed(1)}%</strong>
        </span>
      </div>

      {loading && (
        <div style={{ textAlign: 'center', padding: 48, color: '#64748b' }}>
          Loading question…
        </div>
      )}

      {error && (
        <div style={{ padding: 20, background: '#fee2e2', borderRadius: 8, color: '#991b1b' }}>
          {error}
        </div>
      )}

      {!loading && !error && question && (
        <QuestionCard
          question={question}
          onAnswer={submitAnswer}
          result={lastResult}
          onNext={loadNextQuestion}
        />
      )}
    </div>
  );
}
