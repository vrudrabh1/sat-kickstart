import { useState } from 'react';

/**
 * Returns a stable session ID for the current browser session.
 * Uses sessionStorage so it resets when the browser tab is closed.
 */
export function useSessionId(): string {
  const [sessionId] = useState<string>(() => {
    const existing = sessionStorage.getItem('sat_session_id');
    if (existing) return existing;
    const id = `session-${Date.now()}-${Math.random().toString(36).slice(2)}`;
    sessionStorage.setItem('sat_session_id', id);
    return id;
  });
  return sessionId;
}
