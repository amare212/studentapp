
import { useEffect, useState } from 'react';
import {
  getStudents,
  updateStudent,
  deleteStudent,
} from '../services/api';

function Students() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [editingStudent, setEditingStudent] = useState(null);

  const [editName, setEditName] = useState('');
  const [editDepartment, setEditDepartment] = useState('');
  const [editEmail, setEditEmail] = useState('');

  const [editError, setEditError] = useState('');
  const [editSuccess, setEditSuccess] = useState('');
  const [editLoading, setEditLoading] = useState(false);

  const [deleteLoading, setDeleteLoading] = useState(false);

  const role = localStorage.getItem('role');
  const isAdmin = role === 'ADMIN';

  useEffect(() => {
    loadStudents();
  }, []);

  async function loadStudents() {
    try {
      setLoading(true);
      setError('');

      const data = await getStudents();

      if (Array.isArray(data)) {
        setStudents(data);
      } else if (data && Array.isArray(data.content)) {
        setStudents(data.content);
      } else {
        setStudents([]);
      }
    } catch (err) {
      setError(err.message || 'Unable to load students.');
    } finally {
      setLoading(false);
    }
  }

  function startEdit(student) {
    setEditingStudent(student);
    setEditName(student.name || '');
    setEditDepartment(student.department || '');
    setEditEmail(student.email || '');
    setEditError('');
    setEditSuccess('');
  }

  function cancelEdit() {
    setEditingStudent(null);
    setEditName('');
    setEditDepartment('');
    setEditEmail('');
    setEditError('');
    setEditSuccess('');
  }

  async function handleUpdate(event) {
    event.preventDefault();

    if (!editingStudent) {
      return;
    }

    try {
      setEditLoading(true);
      setEditError('');
      setEditSuccess('');

      await updateStudent(editingStudent.id, {
        name: editName,
        department: editDepartment,
        email: editEmail,
      });

      setEditSuccess('Student updated successfully.');

      await loadStudents();

      setTimeout(() => {
        cancelEdit();
      }, 1000);
    } catch (err) {
      setEditError(
        err.message || 'Failed to update student.'
      );
    } finally {
      setEditLoading(false);
    }
  }

  async function handleDelete(id) {
    const confirmed = window.confirm(
      'Are you sure you want to delete this student?'
    );

    if (!confirmed) {
      return;
    }

    try {
      setDeleteLoading(true);
      setError('');

      await deleteStudent(id);

      await loadStudents();
    } catch (err) {
      setError(
        err.message || 'Failed to delete student.'
      );
    } finally {
      setDeleteLoading(false);
    }
  }

  if (loading) {
    return (
      <div>
        <h1>Students</h1>
        <p>Loading students...</p>
      </div>
    );
  }

  return (
    <div>
      <h1>Students</h1>

      {error && (
        <div>
          <p style={{ color: 'red' }}>{error}</p>

          <button
            type="button"
            onClick={loadStudents}
          >
            Try Again
          </button>
        </div>
      )}

      {!error && students.length === 0 && (
        <div>
          <p>No students found.</p>

          <button
            type="button"
            onClick={loadStudents}
          >
            Refresh
          </button>
        </div>
      )}

      {!error && students.length > 0 && (
        <div>
          <table
            border="1"
            cellPadding="10"
            style={{
              borderCollapse: 'collapse',
              width: '100%',
            }}
          >
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Department</th>
                <th>Email</th>

                {isAdmin && <th>Actions</th>}
              </tr>
            </thead>

            <tbody>
              {students.map((student) => (
                <tr key={student.id}>
                  <td>{student.id}</td>
                  <td>{student.name}</td>
                  <td>{student.department}</td>
                  <td>{student.email}</td>

                  {isAdmin && (
                    <td>
                      <button
                        type="button"
                        onClick={() => startEdit(student)}
                        disabled={deleteLoading}
                      >
                        Edit
                      </button>

                      {' '}

                      <button
                        type="button"
                        onClick={() => handleDelete(student.id)}
                        disabled={deleteLoading}
                      >
                        {deleteLoading
                          ? 'Deleting...'
                          : 'Delete'}
                      </button>
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {editingStudent && (
        <div>
          <h2>Edit Student</h2>

          <form onSubmit={handleUpdate}>
            <div>
              <label htmlFor="edit-name">
                Name
              </label>

              <br />

              <input
                id="edit-name"
                type="text"
                value={editName}
                onChange={(event) =>
                  setEditName(event.target.value)
                }
                required
              />
            </div>

            <br />

            <div>
              <label htmlFor="edit-department">
                Department
              </label>

              <br />

              <input
                id="edit-department"
                type="text"
                value={editDepartment}
                onChange={(event) =>
                  setEditDepartment(event.target.value)
                }
                required
              />
            </div>

            <br />

            <div>
              <label htmlFor="edit-email">
                Email
              </label>

              <br />

              <input
                id="edit-email"
                type="email"
                value={editEmail}
                onChange={(event) =>
                  setEditEmail(event.target.value)
                }
                required
              />
            </div>

            <br />

            <button
              type="submit"
              disabled={editLoading}
            >
              {editLoading
                ? 'Updating...'
                : 'Update Student'}
            </button>

            {' '}

            <button
              type="button"
              onClick={cancelEdit}
              disabled={editLoading}
            >
              Cancel
            </button>
          </form>

          {editError && (
            <p style={{ color: 'red' }}>
              {editError}
            </p>
          )}

          {editSuccess && (
            <p style={{ color: 'green' }}>
              {editSuccess}
            </p>
          )}
        </div>
      )}
    </div>
  );
}

export default Students;

