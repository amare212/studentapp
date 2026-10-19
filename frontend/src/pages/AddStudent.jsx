
import { useState } from 'react';
import { addStudent } from '../services/api';

function AddStudent() {
  const [name, setName] = useState('');
  const [department, setDepartment] = useState('');
  const [email, setEmail] = useState('');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  async function handleSubmit(event) {
    event.preventDefault();

    setError('');
    setSuccess('');
    setLoading(true);

    try {
      const student = {
        name,
        department,
        email,
      };

      const data = await addStudent(student);

      console.log('Student added:', data);

      setSuccess('Student added successfully.');

      setName('');
      setDepartment('');
      setEmail('');

    } catch (err) {
      console.error('Add student error:', err);
      setError(err.message || 'Failed to add student.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <h1>Add Student</h1>

      <form onSubmit={handleSubmit}>

        <div>
          <label htmlFor="name">
            Name
          </label>

          <input
            id="name"
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Enter student name"
            required
          />
        </div>

        <div>
          <label htmlFor="department">
            Department
          </label>

          <input
            id="department"
            type="text"
            value={department}
            onChange={(e) => setDepartment(e.target.value)}
            placeholder="Enter department"
            required
          />
        </div>

        <div>
          <label htmlFor="email">
            Email
          </label>

          <input
            id="email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Enter email"
            required
          />
        </div>

        <button
          type="submit"
          disabled={loading}
        >
          {loading ? 'Adding...' : 'Add Student'}
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

export default AddStudent;

