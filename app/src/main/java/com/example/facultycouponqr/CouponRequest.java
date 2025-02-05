package com.example.facultycouponqr;


import com.google.gson.annotations.SerializedName;

public class CouponRequest {
    @SerializedName("faculty_id")  // Must match backend key
    private String facultyId;

    @SerializedName("coupon_type")  // ✅ Change "couponCode" → "coupon_type" to match Flask API
    private String couponType;

    // Constructor for assigning coupons (COE screen)
    public CouponRequest(String facultyId, String couponType) {
        this.facultyId = facultyId;
        this.couponType = couponType;
    }

    // Constructor for validating coupons (Canteen screen)
    public CouponRequest(String couponType) {
        this.couponType = couponType;
    }

    // Getters
    public String getFacultyId() {
        return facultyId;
    }

    public String getCouponType() {
        return couponType;
    }

    // Setters
    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }
}

