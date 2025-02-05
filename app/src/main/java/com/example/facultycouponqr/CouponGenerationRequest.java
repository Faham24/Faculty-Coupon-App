package com.example.facultycouponqr;

public class CouponGenerationRequest {
    private int num_coupons;

    public CouponGenerationRequest(int num_coupons) {
        this.num_coupons = num_coupons;
    }

    public int getNumCoupons() {
        return num_coupons;
    }
}
