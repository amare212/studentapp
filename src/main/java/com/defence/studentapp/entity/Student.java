package com.defence.studentapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String department;

    private String email;

    // Empty constructor
    public Student() {
    }

    // Constructor
    public Student(Long id, String name, String department, String email) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
    }

    // GET ID
    public Long getId() {
        return id;
    }

    // SET ID
    public void setId(Long id) {
        this.id = id;
    }

    // GET NAME
    public String getName() {
        return name;
    }

    // SET NAME
    public void setName(String name) {
        this.name = name;
    }

    // GET DEPARTMENT
    public String getDepartment() {
        return department;
    }

    // SET DEPARTMENT
    public void setDepartment(String department) {
        this.department = department;
    }

    // GET EMAIL
    public String getEmail() {
        return email;
    }

    // SET EMAIL
    public void setEmail(String email) {
        this.email = email;
    }
}