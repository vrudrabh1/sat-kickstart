# CLAUDE.md — sat-kickstart

This file provides guidance for AI assistants (and human contributors) working on the **sat-kickstart** codebase. Keep this document up to date as the project evolves.

---

## Project Overview

**sat-kickstart** is a browser-based web application that helps high school students prepare for the SAT standardized test. It provides adaptive practice questions, timed mock tests, progress tracking, and performance analytics.

- **Target users:** High school students (ages 14–18) preparing for the SAT
- **Application type:** Full-stack web app (React frontend + Java/Spring Boot backend)
- **Goal:** Make SAT prep accessible, engaging, and personalized through adaptive difficulty

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React 18+ with TypeScript |
| Build tool | Vite |
| Backend | Java 17+, Spring Boot 3.x |
| Build tool | Maven (`pom.xml`) |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Testing (BE) | JUnit 5 + Mockito |
| Testing (FE) | Vitest + React Testing Library |
| API style | REST (`/api/v1/...`) |

---

## Project Structure

```
sat-kickstart/
├── backend/                        # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/satkickstart/
│   │   │   │   ├── controller/     # REST controllers
│   │   │   │   ├── service/        # Business logic
│   │   │   │   ├── repository/     # Spring Data JPA repos
│   │   │   │   ├── model/          # JPA entity classes
│   │   │   │   ├── dto/            # Request/response DTOs
│   │   │   │   └── config/         # Spring configuration
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/java/com/satkickstart/
│   └── pom.xml
├── frontend/                       # React application
│   ├── src/
│   │   ├── components/             # Reusable UI components (PascalCase)
│   │   ├── pages/                  # Route-level page components
│   │   ├── services/               # API client (Axios calls to backend)
│   │   ├── hooks/                  # Custom React hooks
│   │   ├── types/                  # TypeScript type definitions
│   │   └── utils/                  # Shared utility functions
│   ├── package.json
│   └── vite.config.ts
├── README.md
└── CLAUDE.md
```

---

## Development Workflow

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL running locally
- Maven (or use the `./mvnw` wrapper)

### Running the backend
```bash
cd backend
./mvnw spring-boot:run
# Starts on http://localhost:8080
```

### Running the frontend
```bash
cd frontend
npm install
npm run dev
# Starts on http://localhost:5173
```

### Running tests
```bash
# Backend
cd backend && ./mvnw test

# Frontend
cd frontend && npm test
```

### Building for production
```bash
cd backend && ./mvnw package          # Creates target/*.jar
cd frontend && npm run build          # Creates dist/
```

### Environment variables
Copy `.env.example` to `.env` in both `backend/` and `frontend/` and fill in values:

**backend** (`application.properties` or env):
- `SPRING_DATASOURCE_URL` — PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `OPENSAT_API_BASE_URL` — defaults to `https://pinesat.com/api`

**frontend** (`.env`):
- `VITE_API_BASE_URL` — backend URL, e.g. `http://localhost:8080/api/v1`

---

## SAT Question Data Source

### Primary: OpenSAT / PineSAT API
- **Endpoint:** `https://pinesat.com/api/questions`
- **Source:** https://github.com/Anas099X/OpenSAT
- **Volume:** 1,000+ community-generated SAT practice questions
- **No API key required**

#### API Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| `section` | string | `english` or `math` (case-insensitive) |
| `domain` | string | Topic domain (e.g. `"Heart of Algebra"`) |
| `limit` | integer | Max number of questions to return |

#### Example request
```
GET https://pinesat.com/api/questions?section=math&limit=10
```

#### Question object shape
```json
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
```

### Alternate: SAT Question Bank (offline seed data)
- https://github.com/mdn522/sat-question-bank
- JSON dataset — import into PostgreSQL as seed data for offline/testing use

### Copyright & Licensing
- OpenSAT **question database**: freely usable for personal and commercial projects (no restrictions)
- OpenSAT **source code**: do not copy for commercial use (we only call their API — that's fine)
- **Never** copy official College Board or Khan Academy questions — they are copyrighted
- Label all questions in the UI as **"unofficial practice content"** to avoid confusion with actual SAT materials

---

## Adaptive Difficulty System

OpenSAT questions have no native difficulty field. We implement adaptive difficulty in the backend:

### Domain Difficulty Tiers
| Tier | Math Domains | English Domains |
|------|-------------|-----------------|
| Easy | Heart of Algebra, Problem Solving | Grammar Basics, Main Idea |
| Medium | Data Analysis, Geometry | Vocabulary in Context, Inference |
| Hard | Passport to Advanced Math | Rhetoric & Style, Textual Evidence |

### Adaptive Algorithm
1. New users start at **MEDIUM** difficulty
2. After each answer, update the user's consecutive streak per domain
3. **2+ consecutive correct** → escalate to HARD
4. **2+ consecutive incorrect** → de-escalate to EASY
5. Store `difficulty_level` (`EASY | MEDIUM | HARD`) per user in the database
6. When fetching the next question, pass the appropriate domain(s) to the OpenSAT API

---

## Key Features

- **Practice mode** — unlimited questions by section/domain, with instant feedback
- **Timed tests** — full-length or section-level SAT simulations with countdown timer
- **Adaptive difficulty** — auto-adjusts question complexity based on performance
- **Progress tracking** — per-domain accuracy rates, score history, streak counts
- **Score reports** — projected SAT score range, weak area highlights
- **Vocabulary drills** — word lists tied to SAT Reading passages

---

## Code Conventions

### Java (Backend)
- Follow **Google Java Style Guide**
- Package root: `com.satkickstart`
- Class naming: `PascalCase`; method/variable naming: `camelCase`
- DTOs are separate from entity models — never expose entities directly in responses
- Service layer handles all business logic; controllers are thin
- Use `@Valid` on request bodies; return structured error responses

### React/TypeScript (Frontend)
- Use **functional components** with hooks only — no class components
- **TypeScript strict mode** enabled
- Component files: `PascalCase.tsx`; utilities/hooks: `camelCase.ts`
- Keep components small; lift state only as needed
- Use `services/` for all API calls (Axios); never fetch directly in components
- Prefer named exports over default exports for utilities

### REST API Design
- Base path: `/api/v1/`
- Use standard HTTP verbs (GET, POST, PUT, DELETE)
- Return consistent JSON error shapes: `{ "error": "...", "message": "..." }`
- Paginate list endpoints with `?page=0&size=20`

### Commits
Use **Conventional Commits** format:
```
feat: add adaptive difficulty selection
fix: correct answer comparison for math questions
docs: update CLAUDE.md with API details
test: add unit tests for DifficultyService
```

---

## AI Assistant Guidelines

When working on this codebase:

1. **Run tests before committing.** `./mvnw test` and `npm test` must pass.
2. **Keep frontend/backend concerns separate.** The backend exposes REST endpoints; the frontend consumes them. No direct DB access from the frontend.
3. **Use the OpenSAT API for question data** — do not hardcode questions or make up SAT content.
4. **Label all content as unofficial** — add UI disclaimers where questions are shown.
5. **Don't reproduce College Board content.** If writing seed data or mocks, generate novel questions or use OpenSAT's dataset.
6. **Document new API endpoints** in a `docs/api.md` file or inline with Javadoc/OpenAPI annotations.
7. **Keep the adaptive difficulty tiers updated** in this file if domain mappings change.
8. **This is an educational tool for minors** — keep content appropriate and avoid collecting unnecessary personal data.
