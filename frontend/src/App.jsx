
import { useState } from 'react';
import Students from './pages/Students';
import Login from './pages/Login';
import AddStudent from './pages/AddStudent';

function App() {
  const [currentSection, setCurrentSection] = useState(
    window.location.hash.replace('#', '') || 'dashboard'
  );

  const [isLoggedIn, setIsLoggedIn] = useState(
    !!localStorage.getItem('token')
  );

  const goTo = (section) => {
    window.location.hash = section;
    setCurrentSection(section);
  };

  const handleLoginSuccess = () => {
    setIsLoggedIn(true);
    goTo('students');
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');

    setIsLoggedIn(false);
    goTo('dashboard');
  };

  return (
    <div className="app">

      <header className="navbar">

        <div className="logo">
          StudentApp
        </div>

        <nav>

          <button onClick={() => goTo('dashboard')}>
            Dashboard
          </button>

          <button onClick={() => goTo('students')}>
            Students
          </button>

          <button onClick={() => goTo('add-student')}>
            Add Student
          </button>

          {!isLoggedIn ? (
            <button onClick={() => goTo('login')}>
              Login
            </button>
          ) : (
            <button onClick={handleLogout}>
              Logout
            </button>
          )}

        </nav>

      </header>

      {currentSection === 'dashboard' && (
        <main className="dashboard">

          <h1>
            Student Management System
          </h1>

          <p className="welcome">
            Welcome to StudentApp
          </p>

          <div className="cards">

            <div className="card">

              <h2>Students</h2>

              <p>
                Manage student information
              </p>

              <button onClick={() => goTo('students')}>
                View Students
              </button>

            </div>

            <div className="card">

              <h2>Add Student</h2>

              <p>
                Register a new student
              </p>

              <button onClick={() => goTo('add-student')}>
                Add Student
              </button>

            </div>

            <div className="card">

              <h2>Authentication</h2>

              <p>
                Login and manage access
              </p>

              <button onClick={() => goTo('login')}>
                {isLoggedIn ? 'Account' : 'Login'}
              </button>

            </div>

          </div>

        </main>
      )}

      {currentSection === 'students' && (
        <main className="dashboard">

          {isLoggedIn ? (
            <Students />
          ) : (
            <div>

              <h2>
                Students
              </h2>

              <p>
                Please login first.
              </p>

              <button onClick={() => goTo('login')}>
                Go to Login
              </button>

            </div>
          )}

        </main>
      )}

      {currentSection === 'add-student' && (
        <main className="dashboard">

          {isLoggedIn ? (
            <AddStudent />
          ) : (
            <div>

              <h2>
                Add Student
              </h2>

              <p>
                Please login first.
              </p>

              <button onClick={() => goTo('login')}>
                Go to Login
              </button>

            </div>
          )}

        </main>
      )}

      {currentSection === 'login' && (
        <main className="dashboard">

          <section className="students-section">

            <Login
              onLoginSuccess={handleLoginSuccess}
            />

            <button onClick={() => goTo('dashboard')}>
              Back to Dashboard
            </button>

          </section>

        </main>
      )}

    </div>
  );
}

export default App;

