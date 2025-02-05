package com.example.facultycouponqr;

import com.google.gson.annotations.SerializedName;

public class Faculty {
    @SerializedName("faculty_id")
    private String facultyId;

    @SerializedName("name")
    private String name;

    @SerializedName("department")
    private String department;

    @SerializedName("email")
    private String email;

    @SerializedName("role")
    private String role; // Add back the role field for registration purposes

    // Constructor for registration
    public Faculty(String facultyId, String name, String email, String department, String role) {
        this.facultyId = facultyId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
    }

    // Constructor for API response without role
    public Faculty(String facultyId, String name, String email, String department) {
        this(facultyId, name, email, department, null); // Default role to null for API response
    }

    // Getters
    public String getFacultyId() {
        return facultyId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}


