const API_BASE_URL = 'http://localhost:8080';


/*
 * LOGIN
 *
 * Spring Boot creates the JWT and stores it
 * in an HttpOnly cookie.
 */
export async function login(username, password) {

  const response = await fetch(`${API_BASE_URL}/auth/login`, {
    method: 'POST',

    headers: {
      'Content-Type': 'application/json',
    },

    // Allow the browser to receive/store the cookie
    credentials: 'include',

    body: JSON.stringify({
      username,
      password,
    }),
  });

  const data = await response.json();

  if (!response.ok) {

    if (response.status === 400) {
      throw new Error(
        data.message || 'Username and password are required.'
      );
    }

    if (response.status === 401) {
      throw new Error(
        data.message || 'Invalid username or password.'
      );
    }

    throw new Error(
      data.message || 'Login failed.'
    );
  }

  return data;
}


/*
 * LOGOUT
 */
export async function logout() {

  const response = await fetch(
    `${API_BASE_URL}/auth/logout`,
    {
      method: 'POST',
      credentials: 'include',
    }
  );

  if (!response.ok) {
    throw new Error('Logout failed.');
  }

  return true;
}


/*
 * GET STUDENTS
 */
export async function getStudents() {

  const response = await fetch(
    `${API_BASE_URL}/students`,
    {
      method: 'GET',

      headers: {
        'Content-Type': 'application/json',
      },

      credentials: 'include',
    }
  );

  if (!response.ok) {

    if (response.status === 401) {
      throw new Error(
        'Session expired. Please login again.'
      );
    }

    if (response.status === 403) {
      throw new Error(
        'You do not have permission to view students.'
      );
    }

    throw new Error(
      'Failed to fetch students.'
    );
  }

  return response.json();
}


/*
 * ADD STUDENT
 */
export async function addStudent(student) {

  const response = await fetch(
    `${API_BASE_URL}/students`,
    {
      method: 'POST',

      headers: {
        'Content-Type': 'application/json',
      },

      credentials: 'include',

      body: JSON.stringify(student),
    }
  );

  if (!response.ok) {

    if (response.status === 401) {
      throw new Error(
        'Session expired. Please login again.'
      );
    }

    if (response.status === 403) {
      throw new Error(
        'You do not have permission to add students. Admin access is required.'
      );
    }

    let data = {};

    try {
      data = await response.json();
    } catch {
      data = {};
    }

    if (response.status === 400) {
      throw new Error(
        data.message ||
        'Please check the student information.'
      );
    }

    if (response.status === 409) {
      throw new Error(
        data.message ||
        'A student with this email already exists.'
      );
    }

    throw new Error(
      data.message ||
      'Failed to add student.'
    );
  }

  const text = await response.text();

  if (!text) {
    return null;
  }

  return JSON.parse(text);
}


/*
 * UPDATE STUDENT
 */
export async function updateStudent(id, student) {

  const response = await fetch(
    `${API_BASE_URL}/students/${id}`,
    {
      method: 'PUT',

      headers: {
        'Content-Type': 'application/json',
      },

      credentials: 'include',

      body: JSON.stringify(student),
    }
  );

  if (!response.ok) {

    if (response.status === 401) {
      throw new Error(
        'Session expired. Please login again.'
      );
    }

    if (response.status === 403) {
      throw new Error(
        'You do not have permission to edit students. Admin access is required.'
      );
    }

    if (response.status === 404) {
      throw new Error(
        'Student not found.'
      );
    }

    let data = {};

    try {
      data = await response.json();
    } catch {
      data = {};
    }

    if (response.status === 400) {
      throw new Error(
        data.message ||
        'Please check the student information.'
      );
    }

    if (response.status === 409) {
      throw new Error(
        data.message ||
        'A student with this email already exists.'
      );
    }

    throw new Error(
      data.message ||
      'Failed to update student.'
    );
  }

  const text = await response.text();

  if (!text) {
    return null;
  }

  return JSON.parse(text);
}


/*
 * DELETE STUDENT
 */
export async function deleteStudent(id) {

  const response = await fetch(
    `${API_BASE_URL}/students/${id}`,
    {
      method: 'DELETE',

      headers: {
        'Content-Type': 'application/json',
      },

      credentials: 'include',
    }
  );

  if (!response.ok) {

    if (response.status === 401) {
      throw new Error(
        'Session expired. Please login again.'
      );
    }

    if (response.status === 403) {
      throw new Error(
        'You do not have permission to delete students. Admin access is required.'
      );
    }

    if (response.status === 404) {
      throw new Error(
        'Student not found.'
      );
    }

    throw new Error(
      'Failed to delete student.'
    );
  }

  return true;
}