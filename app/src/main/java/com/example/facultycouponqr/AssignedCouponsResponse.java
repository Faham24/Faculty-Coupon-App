package com.example.facultycouponqr;

import java.util.List;

public class AssignedCouponsResponse {
    private boolean success;
    private List<String> coupons; // Must match JSON key in API response

    public boolean isSuccess() {
        return success;
    }

    public List<String> getCouponCodes() {
        return coupons;
    }
}
