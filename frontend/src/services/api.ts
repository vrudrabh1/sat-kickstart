import axios from 'axios';
import type { AnswerRequest, AnswerResponse, ProgressEntry, Question, Section } from '../types';

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? '/api/v1';

const client = axios.create({ baseURL: BASE_URL });

export const questionsApi = {
  getAdaptive: (sessionId: string, section: Section, limit = 1): Promise<Question[]> =>
    client
      .get<Question[]>('/questions/adaptive', { params: { sessionId, section, limit } })
      .then((r) => r.data),

  getQuestions: (section: Section, domain?: string, limit = 10): Promise<Question[]> =>
    client
      .get<Question[]>('/questions', { params: { section, domain, limit } })
      .then((r) => r.data),
};

export const practiceApi = {
  submitAnswer: (req: AnswerRequest): Promise<AnswerResponse> =>
    client.post<AnswerResponse>('/practice/answer', req).then((r) => r.data),
};

export const progressApi = {
  getProgress: (sessionId: string): Promise<ProgressEntry[]> =>
    client.get<ProgressEntry[]>('/progress', { params: { sessionId } }).then((r) => r.data),
};
