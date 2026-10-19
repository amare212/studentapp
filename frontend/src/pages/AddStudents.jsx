import React, { useEffect, useState } from "react";
import { getStudents } from "../service/api";

function Students() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadStudents();
  }, []);

  const loadStudents = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await getStudents();

      setStudents(response.data);
    } catch (err) {
      console.error("Error loading students:", err);
      setError("Unable to load students");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <p>Loading students...</p>;
  }

  return (
    <div>
      <h1>Students</h1>

      {error && <p>{error}</p>}

      {!error && students.length === 0 && (
        <p>No students found.</p>
      )}

      {!error && students.length > 0 && (
        <table border="1" cellPadding="10">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Department</th>
              <th>Email</th>
            </tr>
          </thead>

          <tbody>
            {students.map((student) => (
              <tr key={student.id}>
                <td>{student.id}</td>
                <td>{student.name}</td>
                <td>{student.department}</td>
                <td>{student.email}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default Students;