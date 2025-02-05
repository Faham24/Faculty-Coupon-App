package com.example.facultycouponqr;

import com.google.gson.annotations.SerializedName;

public class CouponResponse {

    // Fields expected in the JSON response
    @SerializedName("success")
    private boolean success;  // Indicates if the login or request was successful

    @SerializedName("message")
    private String message;  // Optional message from the server, like "Login successful" or "Login failed"

    @SerializedName("coupon")
    private String coupon;  // Assuming the server sends back a coupon code or some information after login

    @SerializedName("coupon_code")
    private String couponCode;
    // Constructor
    public CouponResponse(boolean success, String message, String coupon) {
        this.success = success;
        this.message = message;
        this.coupon = coupon;
    }
    public String getCouponCode() {
        return couponCode;
    }

    // Getters and setters for accessing fields
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }
}
