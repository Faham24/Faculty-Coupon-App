package com.example.facultycouponqr;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    // Fields expected in the JSON response
    @SerializedName("success")
    private boolean success;  // Indicates if the login or request was successful

    @SerializedName("message")
    private String message;  // Optional message from the server, like "Login successful" or "Login failed"

    @SerializedName("facultyId")
    private String facultyId;  // The faculty ID returned from the server on successful login

    @SerializedName("department")
    private String department;
    // Constructor
    public LoginResponse(boolean success, String message, String facultyId, String department) {
        this.success = success;
        this.message = message;
        this.facultyId = facultyId;
        this.department=department;

    }

    // Getter for 'success' field
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Getter for 'message' field
    public String getMessage() {
        return message;
    }
    public String getDepartment() {
        return department; // This method retrieves the department value
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for 'facultyId' field
    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }
}
