package com.example.facultycouponqr;

import com.google.gson.annotations.SerializedName;

public class CouponRequest {

    @SerializedName("faculty_id")
    private String facultyId;  // For COE assigning coupons

    @SerializedName("coupon_type")
    private String couponType; // Lunch/Morning

    @SerializedName("exam_name")
    private String examName;   // Added for COE

    @SerializedName("num_batches")
    private int numBatches;    // Added for COE

    @SerializedName("exam_time")
    private String examTime;
    @SerializedName("coupon_code")
    private String couponCode; // For Canteen validation

    // Constructor for COE (Assigning coupons)
    public CouponRequest(String facultyId, String couponType, String examName, String examTime) {
        this.facultyId = facultyId;
        this.couponType = couponType;
        this.examName = examName;
        this.examTime= examTime;
    }

    // Constructor for Canteen (Validating coupons)
    public CouponRequest(String couponCode) {
        this.couponCode = couponCode;
    }

    // Getters
    public String getFacultyId() {
        return facultyId;
    }

    public String getCouponType() {
        return couponType;
    }

    public String getExamName() {
        return examName;
    }

    public int getNumBatches() {
        return numBatches;
    }

    public String getCouponCode() {
        return couponCode;
    }

    // Setters
    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public void setNumBatches(int numBatches) {
        this.numBatches = numBatches;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
