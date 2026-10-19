
import { useState } from 'react';
import { login } from '../services/api';

function Login({ onLoginSuccess }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  async function handleLogin(event) {
    event.preventDefault();

    setError('');
    setSuccess('');
    setLoading(true);

    try {
      const data = await login(username, password);

      console.log('Login response:', data);

      if (!data.token) {
        throw new Error('Login succeeded but no token was returned.');
      }

      // Save login information
      localStorage.setItem('token', data.token);
      localStorage.setItem('username', data.username || username);
      localStorage.setItem('role', data.role || '');
      localStorage.setItem('userId', data.userId || '');

      console.log(
        'Token saved:',
        localStorage.getItem('token') ? 'YES' : 'NO'
      );

      setSuccess(
        `Login successful. Welcome ${data.username || username}!`
      );

      setPassword('');

      // Tell App.jsx that login was successful
      if (onLoginSuccess) {
        onLoginSuccess();
      }

    } catch (err) {
      console.error('Login error:', err);
      setError(err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="login-container">

      <h2>Login</h2>

      <form onSubmit={handleLogin}>

        <div>
          <label htmlFor="username">
            Username
          </label>

          <input
            id="username"
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Enter username"
            required
          />
        </div>

        <div>
          <label htmlFor="password">
            Password
          </label>

          <input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Enter password"
            required
          />
        </div>

        <button
          type="submit"
          disabled={loading}
        >
          {loading ? 'Logging in...' : 'Login'}
        </button>

      </form>

      {error && (
        <p style={{ color: 'red' }}>
          {error}
        </p>
      )}

      {success && (
        <p style={{ color: 'green' }}>
          {success}
        </p>
      )}

    </div>
  );
}

export default Login;

