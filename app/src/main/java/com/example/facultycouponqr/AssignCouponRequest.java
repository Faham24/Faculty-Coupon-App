package com.example.facultycouponqr;

public class AssignCouponRequest {
    private String facultyId;
    private String examTime;

    public AssignCouponRequest(String facultyId, String examTime) {
        this.facultyId = facultyId;
        this.examTime = examTime;
    }

    // Getters
    public String getFacultyId() {
        return facultyId;
    }

    public String getExamTime() {
        return examTime;
    }

    @Override
    public String toString() {
        return "AssignCouponRequest{" +
                "facultyId='" + facultyId + '\'' +
                ", examTime='" + examTime + '\'' +
                '}';
    }
}
