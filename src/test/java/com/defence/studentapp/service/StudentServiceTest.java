package com.defence.studentapp.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.defence.studentapp.dto.StudentRequest;
import com.defence.studentapp.dto.StudentResponse;
import com.defence.studentapp.entity.Student;
import com.defence.studentapp.exception.DuplicateEmailException;
import com.defence.studentapp.exception.StudentNotFoundException;
import com.defence.studentapp.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {


@Mock
private StudentRepository repository;

@InjectMocks
private StudentService service;

// TEST 1: CREATE SUCCESS
@Test
void createStudent_ShouldCreateStudentSuccessfully() {

    StudentRequest request = new StudentRequest();
    request.setName("Abebe");
    request.setDepartment("Computer Science");
    request.setEmail("abebe@test.com");

    Student savedStudent = new Student();
    savedStudent.setId(1L);
    savedStudent.setName("Abebe");
    savedStudent.setDepartment("Computer Science");
    savedStudent.setEmail("abebe@test.com");

    when(repository.existsByEmail("abebe@test.com"))
            .thenReturn(false);

    when(repository.save(org.mockito.ArgumentMatchers.any(Student.class)))
            .thenReturn(savedStudent);

    StudentResponse response = service.createStudent(request);

    assertEquals(1L, response.getId());
    assertEquals("Abebe", response.getName());
    assertEquals("Computer Science", response.getDepartment());
    assertEquals("abebe@test.com", response.getEmail());
}

// TEST 2: CREATE DUPLICATE EMAIL
@Test
void createStudent_ShouldThrowException_WhenEmailAlreadyExists() {

    StudentRequest request = new StudentRequest();
    request.setName("Abebe");
    request.setDepartment("Computer Science");
    request.setEmail("abebe@test.com");

    when(repository.existsByEmail("abebe@test.com"))
            .thenReturn(true);

    DuplicateEmailException exception = assertThrows(
            DuplicateEmailException.class,
            () -> service.createStudent(request)
    );

    assertEquals(
            "Email already exists: abebe@test.com",
            exception.getMessage()
    );
}

// TEST 3: GET BY ID - NOT FOUND
@Test
void getStudentById_ShouldThrowException_WhenStudentDoesNotExist() {

    when(repository.findById(99L))
            .thenReturn(Optional.empty());

    StudentNotFoundException exception = assertThrows(
            StudentNotFoundException.class,
            () -> service.getStudentById(99L)
    );

    assertEquals(
            "Student with ID 99 not found",
            exception.getMessage()
    );
}

// TEST 4: GET BY ID - SUCCESS
@Test
void getStudentById_ShouldReturnStudent_WhenStudentExists() {

    Student student = new Student();
    student.setId(1L);
    student.setName("Abebe");
    student.setDepartment("Computer Science");
    student.setEmail("abebe@test.com");

    when(repository.findById(1L))
            .thenReturn(Optional.of(student));

    StudentResponse response = service.getStudentById(1L);

    assertEquals(1L, response.getId());
    assertEquals("Abebe", response.getName());
    assertEquals("Computer Science", response.getDepartment());
    assertEquals("abebe@test.com", response.getEmail());
}

// TEST 5: UPDATE SUCCESS
@Test
void updateStudent_ShouldUpdateStudentSuccessfully() {

    StudentRequest request = new StudentRequest();
    request.setName("Abebe Updated");
    request.setDepartment("Software Engineering");
    request.setEmail("abebe.updated@test.com");

    Student existingStudent = new Student();
    existingStudent.setId(1L);
    existingStudent.setName("Abebe");
    existingStudent.setDepartment("Computer Science");
    existingStudent.setEmail("abebe@test.com");

    when(repository.findById(1L))
            .thenReturn(Optional.of(existingStudent));

    when(repository.existsByEmail("abebe.updated@test.com"))
            .thenReturn(false);

    when(repository.save(existingStudent))
            .thenReturn(existingStudent);

    StudentResponse response =
            service.updateStudent(1L, request);

    assertEquals(1L, response.getId());
    assertEquals("Abebe Updated", response.getName());
    assertEquals("Software Engineering", response.getDepartment());
    assertEquals("abebe.updated@test.com", response.getEmail());
}

// TEST 6: UPDATE - DUPLICATE EMAIL
@Test
void updateStudent_ShouldThrowException_WhenNewEmailAlreadyExists() {

    StudentRequest request = new StudentRequest();
    request.setName("Abebe Updated");
    request.setDepartment("Software Engineering");
    request.setEmail("existing@test.com");

    Student existingStudent = new Student();
    existingStudent.setId(1L);
    existingStudent.setName("Abebe");
    existingStudent.setDepartment("Computer Science");
    existingStudent.setEmail("abebe@test.com");

    when(repository.findById(1L))
            .thenReturn(Optional.of(existingStudent));

    when(repository.existsByEmail("existing@test.com"))
            .thenReturn(true);

    DuplicateEmailException exception = assertThrows(
            DuplicateEmailException.class,
            () -> service.updateStudent(1L, request)
    );

    assertEquals(
            "Email already exists: existing@test.com",
            exception.getMessage()
    );
}

// TEST 7: UPDATE - STUDENT NOT FOUND
@Test
void updateStudent_ShouldThrowException_WhenStudentDoesNotExist() {

    StudentRequest request = new StudentRequest();
    request.setName("Abebe Updated");
    request.setDepartment("Software Engineering");
    request.setEmail("abebe.updated@test.com");

    when(repository.findById(99L))
            .thenReturn(Optional.empty());

    StudentNotFoundException exception = assertThrows(
            StudentNotFoundException.class,
            () -> service.updateStudent(99L, request)
    );

    assertEquals(
            "Student with ID 99 not found",
            exception.getMessage()
    );
}

// TEST 8: DELETE SUCCESS
@Test
void deleteStudent_ShouldDeleteStudentSuccessfully() {

    Student student = new Student();
    student.setId(1L);
    student.setName("Abebe");
    student.setDepartment("Computer Science");
    student.setEmail("abebe@test.com");

    when(repository.findById(1L))
            .thenReturn(Optional.of(student));

    service.deleteStudent(1L);

    verify(repository).delete(student);
}

// TEST 9: DELETE - STUDENT NOT FOUND
@Test
void deleteStudent_ShouldThrowException_WhenStudentDoesNotExist() {

    when(repository.findById(99L))
            .thenReturn(Optional.empty());

    StudentNotFoundException exception = assertThrows(
            StudentNotFoundException.class,
            () -> service.deleteStudent(99L)
    );

    assertEquals(
            "Student with ID 99 not found",
            exception.getMessage()
    );
}


}
