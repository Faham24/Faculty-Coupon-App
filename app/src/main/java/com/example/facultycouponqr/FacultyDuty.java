package com.example.facultycouponqr;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

public class FacultyDuty {
    @SerializedName("faculty_id") // Ensure this matches what your Flask API expects
    private final String facultyId;

    @SerializedName("faculty_name") // Ensure this matches what your Flask API expects
    private final String facultyName;

    @SerializedName("exam_name") // Ensure this matches what your Flask API expects
    private final String examName;

    @SerializedName("exam_date") // Ensure this matches what your Flask API expects
    private final String examDate;

    @SerializedName("exam_time") // Ensure this matches what your Flask API expects
    private final String examTime;

    private final int num_batches;

    public FacultyDuty(String facultyId, String facultyName, String examName, int num_batches, String examDate, String examTime) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.examName = examName;
        this.num_batches = num_batches;
        this.examDate = examDate;
        this.examTime = examTime;
    }

    // Getters
    public String getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getExamName() {
        return examName;
    }

    public String getExamDate() {
        return examDate;
    }

    public String getExamTime() {
        return examTime;
    }

    public int getNumBatches() {
        return num_batches;
    }

    @NonNull
    @Override
    public String toString() {
        return "FacultyDuty{" +
                "facultyId='" + facultyId + '\'' +
                ", facultyName='" + facultyName + '\'' +
                ", examName='" + examName + '\'' +
                ", num_batches=" + num_batches +
                ", examDate='" + examDate + '\'' +
                ", examTime='" + examTime + '\'' +
                '}';
    }
}
