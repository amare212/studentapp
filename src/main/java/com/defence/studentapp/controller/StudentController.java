
package com.defence.studentapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.defence.studentapp.dto.StudentRequest;
import com.defence.studentapp.dto.StudentResponse;
import com.defence.studentapp.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Page<StudentResponse> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String department) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative");
        }

        if (size <= 0) {
            throw new IllegalArgumentException(
                    "Page size must be greater than 0");
        }

        if (size > 100) {
            throw new IllegalArgumentException(
                    "Page size cannot be greater than 100");
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Direction must be 'asc' or 'desc'");
        }

        if (!sortBy.equals("id")
                && !sortBy.equals("name")
                && !sortBy.equals("department")
                && !sortBy.equals("email")) {

            throw new IllegalArgumentException(
                    "Invalid sortBy field. Use: id, name, department, or email");
        }

        return studentService.getAllStudents(
                page,
                size,
                sortBy,
                direction,
                department);
    }

    @GetMapping("/{id}")
    public StudentResponse getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse createStudent(
            @Valid @RequestBody StudentRequest request) {

        return studentService.createStudent(request);
    }

    @PutMapping("/{id}")
    public StudentResponse updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {

        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);

        return "Student deleted successfully";
    }
}

