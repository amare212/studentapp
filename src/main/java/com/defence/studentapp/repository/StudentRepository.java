
package com.defence.studentapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.defence.studentapp.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    Page<Student> findByDepartmentIgnoreCase(
            String department,
            Pageable pageable);
}

