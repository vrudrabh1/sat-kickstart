import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';
import { QuestionCard } from './QuestionCard';
import type { Question } from '../types';

const mockQuestion: Question = {
  id: 'q1',
  domain: 'Heart of Algebra',
  paragraph: null,
  question: 'What is 2 + 2?',
  choices: { A: '3', B: '4', C: '5', D: '6' },
  correctAnswer: 'B',
  explanation: '2 + 2 equals 4.',
};

describe('QuestionCard', () => {
  it('renders the question text', () => {
    render(
      <QuestionCard
        question={mockQuestion}
        onAnswer={vi.fn()}
        result={null}
        onNext={vi.fn()}
      />
    );
    expect(screen.getByText('What is 2 + 2?')).toBeInTheDocument();
  });

  it('renders all answer choices', () => {
    render(
      <QuestionCard
        question={mockQuestion}
        onAnswer={vi.fn()}
        result={null}
        onNext={vi.fn()}
      />
    );
    expect(screen.getByText('3')).toBeInTheDocument();
    expect(screen.getByText('4')).toBeInTheDocument();
  });

  it('calls onAnswer when a choice is selected', async () => {
    const onAnswer = vi.fn();
    render(
      <QuestionCard
        question={mockQuestion}
        onAnswer={onAnswer}
        result={null}
        onNext={vi.fn()}
      />
    );
    await userEvent.click(screen.getByText('4'));
    expect(onAnswer).toHaveBeenCalledWith('B');
  });

  it('shows explanation and next button after answering', () => {
    render(
      <QuestionCard
        question={mockQuestion}
        onAnswer={vi.fn()}
        result={{ correct: true, explanation: null, newDifficultyLevel: 'MEDIUM', accuracy: 100, totalAttempts: 1 }}
        onNext={vi.fn()}
      />
    );
    expect(screen.getByText('✓ Correct!')).toBeInTheDocument();
    expect(screen.getByText('Next Question →')).toBeInTheDocument();
  });
});
