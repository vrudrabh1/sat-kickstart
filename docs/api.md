# SAT Kickstart API Documentation

Base URL: `/api/v1`

All responses are JSON. Errors follow the shape: `{ "error": "...", "message": "..." }`

---

## Questions

### GET `/api/v1/questions`

Fetch practice questions from the OpenSAT data source.

**Query Parameters**

| Parameter  | Type    | Required | Description                                      |
|------------|---------|----------|--------------------------------------------------|
| `section`  | string  | No       | `english` or `math`                              |
| `domain`   | string  | No       | Topic domain (e.g. `"Heart of Algebra"`)         |
| `limit`    | integer | No       | Max number of questions (default: 10, max: 50)   |
| `page`     | integer | No       | Page number, 0-indexed (default: 0)              |
| `size`     | integer | No       | Page size (default: 20)                          |

**Response**

```json
[
  {
    "id": "abc123",
    "domain": "Heart of Algebra",
    "question": {
      "paragraph": "...",
      "question": "Solve for x...",
      "choices": { "A": "...", "B": "...", "C": "...", "D": "..." },
      "correct_answer": "B",
      "explanation": "..."
    }
  }
]
```

---

## Users

### POST `/api/v1/users/register`

Register a new user.

**Request Body**

```json
{
  "username": "student123",
  "email": "student@example.com",
  "password": "securepassword"
}
```

**Response** `201 Created`

```json
{
  "id": 1,
  "username": "student123",
  "email": "student@example.com"
}
```

---

### POST `/api/v1/users/login`

Authenticate a user.

**Request Body**

```json
{
  "email": "student@example.com",
  "password": "securepassword"
}
```

**Response** `200 OK`

```json
{
  "token": "<jwt>",
  "userId": 1
}
```

---

## Progress

### GET `/api/v1/users/{userId}/progress`

Get a user's progress and performance stats.

**Response**

```json
{
  "userId": 1,
  "difficultyLevel": "MEDIUM",
  "domainStats": [
    {
      "domain": "Heart of Algebra",
      "totalAnswered": 20,
      "correctCount": 15,
      "accuracyRate": 0.75,
      "consecutiveStreak": 2
    }
  ]
}
```

---

### POST `/api/v1/users/{userId}/answers`

Submit an answer and update adaptive difficulty.

**Request Body**

```json
{
  "questionId": "abc123",
  "selectedAnswer": "B",
  "domain": "Heart of Algebra"
}
```

**Response** `200 OK`

```json
{
  "correct": true,
  "correctAnswer": "B",
  "explanation": "...",
  "newDifficultyLevel": "HARD"
}
```

---

## Tests (Timed Sessions)

### POST `/api/v1/tests`

Start a new timed test session.

**Request Body**

```json
{
  "userId": 1,
  "type": "FULL"
}
```

`type` options: `FULL`, `MATH_ONLY`, `ENGLISH_ONLY`

**Response** `201 Created`

```json
{
  "testId": 42,
  "questions": [...],
  "durationSeconds": 3600
}
```

---

### PUT `/api/v1/tests/{testId}/submit`

Submit a completed test and receive a score report.

**Request Body**

```json
{
  "answers": [
    { "questionId": "abc123", "selectedAnswer": "B" }
  ]
}
```

**Response** `200 OK`

```json
{
  "testId": 42,
  "score": 1280,
  "projectedRange": "1250–1310",
  "sectionScores": {
    "math": 680,
    "english": 600
  },
  "weakAreas": ["Passport to Advanced Math", "Rhetoric & Style"]
}
```

---

> **Note:** All practice content is **unofficial** and not affiliated with or endorsed by the College Board.
