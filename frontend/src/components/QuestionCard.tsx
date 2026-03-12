import { useState } from 'react';
import type { AnswerResponse, Question } from '../types';

interface QuestionCardProps {
  question: Question;
  onAnswer: (selected: string) => void;
  result: AnswerResponse | null;
  onNext: () => void;
}

export function QuestionCard({ question, onAnswer, result, onNext }: QuestionCardProps) {
  const [selected, setSelected] = useState<string | null>(null);

  const handleSelect = (key: string) => {
    if (result) return; // already answered
    setSelected(key);
    onAnswer(key);
  };

  const choiceEntries = Object.entries(question.choices);

  return (
    <div style={{ maxWidth: 680, margin: '0 auto', fontFamily: 'system-ui, sans-serif' }}>
      {question.paragraph && (
        <div
          style={{
            background: '#f8fafc',
            border: '1px solid #e2e8f0',
            borderRadius: 8,
            padding: '16px 20px',
            marginBottom: 20,
            lineHeight: 1.7,
            fontSize: 15,
          }}
        >
          {question.paragraph}
        </div>
      )}

      <p style={{ fontWeight: 600, fontSize: 16, marginBottom: 20, lineHeight: 1.5 }}>
        {question.question}
      </p>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
        {choiceEntries.map(([key, text]) => {
          const isSelected = selected === key;
          const isCorrect = result && key === question.correctAnswer;
          const isWrong = result && isSelected && !result.correct;

          let bg = '#fff';
          let border = '1px solid #cbd5e1';
          if (isCorrect) { bg = '#d1fae5'; border = '2px solid #10b981'; }
          else if (isWrong) { bg = '#fee2e2'; border = '2px solid #ef4444'; }
          else if (isSelected) { bg = '#eff6ff'; border = '2px solid #3b82f6'; }

          return (
            <button
              key={key}
              onClick={() => handleSelect(key)}
              disabled={!!result}
              style={{
                background: bg,
                border,
                borderRadius: 8,
                padding: '12px 16px',
                textAlign: 'left',
                cursor: result ? 'default' : 'pointer',
                fontSize: 15,
                display: 'flex',
                gap: 12,
                alignItems: 'flex-start',
                transition: 'background 0.15s',
              }}
            >
              <span style={{ fontWeight: 700, minWidth: 20 }}>{key}.</span>
              <span>{text}</span>
            </button>
          );
        })}
      </div>

      {result && (
        <div
          style={{
            marginTop: 24,
            padding: '16px 20px',
            borderRadius: 8,
            background: result.correct ? '#d1fae5' : '#fee2e2',
            border: `1px solid ${result.correct ? '#10b981' : '#ef4444'}`,
          }}
        >
          <p style={{ fontWeight: 700, marginBottom: 8 }}>
            {result.correct ? '✓ Correct!' : '✗ Incorrect'}
          </p>
          {question.explanation && (
            <p style={{ fontSize: 14, lineHeight: 1.6, margin: 0 }}>{question.explanation}</p>
          )}
          <button
            onClick={() => { setSelected(null); onNext(); }}
            style={{
              marginTop: 16,
              background: '#1e40af',
              color: '#fff',
              border: 'none',
              borderRadius: 6,
              padding: '10px 24px',
              cursor: 'pointer',
              fontWeight: 600,
              fontSize: 14,
            }}
          >
            Next Question →
          </button>
        </div>
      )}

      <p
        style={{
          fontSize: 11,
          color: '#94a3b8',
          marginTop: 24,
          textAlign: 'center',
        }}
      >
        Unofficial practice content — not affiliated with College Board
      </p>
    </div>
  );
}
