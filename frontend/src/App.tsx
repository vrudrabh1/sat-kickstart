import { BrowserRouter, Link, NavLink, Route, Routes } from 'react-router-dom';
import { DashboardPage } from './pages/DashboardPage';
import { PracticePage } from './pages/PracticePage';

export default function App() {
  return (
    <BrowserRouter>
      <div style={{ minHeight: '100vh', background: '#f1f5f9' }}>
        <nav
          style={{
            background: '#1e40af',
            padding: '0 24px',
            display: 'flex',
            alignItems: 'center',
            gap: 32,
            height: 56,
          }}
        >
          <Link
            to="/"
            style={{ color: '#fff', fontWeight: 700, fontSize: 18, textDecoration: 'none' }}
          >
            SAT Kickstart
          </Link>
          <NavLink
            to="/practice"
            style={({ isActive }) => ({
              color: isActive ? '#93c5fd' : '#bfdbfe',
              textDecoration: 'none',
              fontWeight: 600,
              fontSize: 14,
            })}
          >
            Practice
          </NavLink>
          <NavLink
            to="/dashboard"
            style={({ isActive }) => ({
              color: isActive ? '#93c5fd' : '#bfdbfe',
              textDecoration: 'none',
              fontWeight: 600,
              fontSize: 14,
            })}
          >
            Dashboard
          </NavLink>
        </nav>

        <main>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/practice" element={<PracticePage />} />
            <Route path="/dashboard" element={<DashboardPage />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

function HomePage() {
  return (
    <div
      style={{
        padding: '80px 24px',
        textAlign: 'center',
        maxWidth: 600,
        margin: '0 auto',
        fontFamily: 'system-ui, sans-serif',
      }}
    >
      <h1 style={{ fontSize: 40, fontWeight: 800, color: '#1e293b', marginBottom: 16 }}>
        SAT Kickstart
      </h1>
      <p style={{ fontSize: 18, color: '#64748b', marginBottom: 48, lineHeight: 1.6 }}>
        Adaptive SAT practice for high school students. Questions get harder as you improve —
        or easier when you need more support.
      </p>
      <div style={{ display: 'flex', gap: 16, justifyContent: 'center', flexWrap: 'wrap' }}>
        <Link
          to="/practice"
          style={{
            background: '#1e40af',
            color: '#fff',
            padding: '14px 32px',
            borderRadius: 8,
            textDecoration: 'none',
            fontWeight: 700,
            fontSize: 16,
          }}
        >
          Start Practising →
        </Link>
        <Link
          to="/dashboard"
          style={{
            background: '#fff',
            color: '#1e40af',
            padding: '14px 32px',
            borderRadius: 8,
            textDecoration: 'none',
            fontWeight: 700,
            fontSize: 16,
            border: '2px solid #1e40af',
          }}
        >
          View Progress
        </Link>
      </div>
    </div>
  );
}
