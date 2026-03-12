import { useCallback, useEffect, useState } from 'react';
import { practiceApi, questionsApi } from '../services/api';
import type { AnswerResponse, DifficultyLevel, Question, Section } from '../types';

interface PracticeState {
  question: Question | null;
  loading: boolean;
  error: string | null;
  lastResult: AnswerResponse | null;
  difficultyLevel: DifficultyLevel;
  totalAttempts: number;
  accuracy: number;
}

export function usePractice(sessionId: string, section: Section) {
  const [state, setState] = useState<PracticeState>({
    question: null,
    loading: true,
    error: null,
    lastResult: null,
    difficultyLevel: 'MEDIUM',
    totalAttempts: 0,
    accuracy: 0,
  });

  const loadNextQuestion = useCallback(async () => {
    setState((s) => ({ ...s, loading: true, error: null, lastResult: null }));
    try {
      const questions = await questionsApi.getAdaptive(sessionId, section, 1);
      setState((s) => ({
        ...s,
        question: questions[0] ?? null,
        loading: false,
        error: questions.length === 0 ? 'No questions available.' : null,
      }));
    } catch {
      setState((s) => ({ ...s, loading: false, error: 'Failed to load question.' }));
    }
  }, [sessionId, section]);

  useEffect(() => {
    loadNextQuestion();
  }, [loadNextQuestion]);

  const submitAnswer = useCallback(
    async (selectedAnswer: string) => {
      if (!state.question) return;
      try {
        const result = await practiceApi.submitAnswer({
          sessionId,
          section,
          questionId: state.question.id,
          selectedAnswer,
          correctAnswer: state.question.correctAnswer,
        });
        setState((s) => ({
          ...s,
          lastResult: result,
          difficultyLevel: result.newDifficultyLevel,
          totalAttempts: result.totalAttempts,
          accuracy: result.accuracy,
        }));
      } catch {
        setState((s) => ({ ...s, error: 'Failed to submit answer.' }));
      }
    },
    [sessionId, section, state.question]
  );

  return { ...state, loadNextQuestion, submitAnswer };
}
