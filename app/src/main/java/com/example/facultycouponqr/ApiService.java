package com.example.facultycouponqr;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // Login endpoint
    @POST("/api/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // Generate coupon endpoint
    @POST("/api/generate_coupon")
    Call<CouponResponse> generateCoupon(@Body CouponRequest couponRequest);

    // Validate coupon endpoint


    // Get invigilation schedule (for COE)
    @GET("/api/invigilation-schedule")
    Call<List<FacultyDuty>> getInvigilationSchedule(@Query("department") String department);

    // Register faculty
    @POST("/api/register")
    Call<ResponseBody> registerFaculty(@Body Faculty faculty);

    // Submit exam details (for faculty)
    @POST("/api/submit_exam_details")
    Call<ResponseBody> submitExamDetails(@Body List<FacultyDuty> duties);

    @GET("/api/get_faculty_name")
    Call<Faculty> getFacultyName(@Query("faculty_id") String facultyId);

    @POST("/api/assign_coupon")
    Call<ResponseBody> assignCoupon(@Body CouponRequest request);


    @GET("/api/get_assigned_coupons")
    Call<AssignedCouponsResponse> getAssignedCoupons(@Query("faculty_id") String facultyId);

    @POST("/api/validate_coupon")
    Call<CouponResponse> validateCoupon(@Body CouponRequest couponRequest);

    @GET("/api/get_faculty_list")
    Call<List<Faculty>> getFacultyList(@Query("department") String department);

    @POST("/api/generate_coupons")
    Call<ResponseBody> generateCoupons(@Body CouponGenerationRequest request);

    @GET("api/get_coupon_history")
    Call<List<CouponHistory>> getCouponHistory();

    @GET("/api/faculty_coupon_status")
    Call<CouponStatusResponse> getCouponStatus(@Query("faculty_id") String facultyId);

}
