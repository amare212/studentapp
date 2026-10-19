package com.defence.studentapp.dto;

public class StudentResponse {

    private Long id;
    private String name;
    private String department;
    private String email;

    // Empty constructor
    public StudentResponse() {
    }

    // Constructor
    public StudentResponse(
            Long id,
            String name,
            String department,
            String email) {

        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
    }

    // Getter for id
    public Long getId() {
        return id;
    }

    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for department
    public String getDepartment() {
        return department;
    }

    // Setter for department
    public void setDepartment(String department) {
        this.department = department;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }
}