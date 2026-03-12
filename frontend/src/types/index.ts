export type Section = 'MATH' | 'ENGLISH';

export type DifficultyLevel = 'EASY' | 'MEDIUM' | 'HARD';

export interface Question {
  id: string;
  domain: string;
  paragraph: string | null;
  question: string;
  choices: Record<string, string>; // { A: '...', B: '...', C: '...', D: '...' }
  correctAnswer: string;
  explanation: string;
}

export interface AnswerRequest {
  sessionId: string;
  section: Section;
  questionId: string;
  selectedAnswer: string;
  correctAnswer: string;
}

export interface AnswerResponse {
  correct: boolean;
  explanation: string | null;
  newDifficultyLevel: DifficultyLevel;
  accuracy: number;
  totalAttempts: number;
}

export interface ProgressEntry {
  section: Section;
  difficultyLevel: DifficultyLevel;
  totalAttempts: number;
  correctAttempts: number;
  accuracy: number;
}
