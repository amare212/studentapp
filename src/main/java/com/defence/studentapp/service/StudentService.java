
package com.defence.studentapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.defence.studentapp.dto.StudentRequest;
import com.defence.studentapp.dto.StudentResponse;
import com.defence.studentapp.entity.Student;
import com.defence.studentapp.exception.DuplicateEmailException;
import com.defence.studentapp.exception.StudentNotFoundException;
import com.defence.studentapp.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public StudentResponse createStudent(StudentRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(
                    "Email already exists: " + request.getEmail());
        }

        Student student = new Student();

        student.setName(request.getName());
        student.setDepartment(request.getDepartment());
        student.setEmail(request.getEmail());

        Student savedStudent = repository.save(student);

        return convertToResponse(savedStudent);
    }

    // GET ALL - PAGINATION + SORTING + FILTERING
    public Page<StudentResponse> getAllStudents(
            int page,
            int size,
            String sortBy,
            String direction,
            String department) {

        Sort sort;

        if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Student> students;

        if (department != null && !department.isBlank()) {

            students = repository.findByDepartmentIgnoreCase(
                    department,
                    pageable);

        } else {

            students = repository.findAll(pageable);
        }

        return students.map(this::convertToResponse);
    }

    // GET BY ID
    public StudentResponse getStudentById(Long id) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"));

        return convertToResponse(student);
    }

    // UPDATE
    public StudentResponse updateStudent(
            Long id,
            StudentRequest request) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"));

        if (!student.getEmail().equals(request.getEmail())
                && repository.existsByEmail(request.getEmail())) {

            throw new DuplicateEmailException(
                    "Email already exists: " + request.getEmail());
        }

        student.setName(request.getName());
        student.setDepartment(request.getDepartment());
        student.setEmail(request.getEmail());

        Student updatedStudent = repository.save(student);

        return convertToResponse(updatedStudent);
    }

    // DELETE
    public void deleteStudent(Long id) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"));

        repository.delete(student);
    }

    // ENTITY → RESPONSE DTO
    private StudentResponse convertToResponse(Student student) {

        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getDepartment(),
                student.getEmail()
        );
    }
}

