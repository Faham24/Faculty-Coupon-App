package com.example.facultycouponqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreen extends AppCompatActivity {

    private ApiService apiService;
    private TextView couponStatusTextView;
    private LinearLayout qrCodeContainer;
    private Button generateCouponButton, logoutButton;
    private String facultyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        couponStatusTextView = findViewById(R.id.couponStatus);
        qrCodeContainer = findViewById(R.id.qrCodeContainer);  // Container for multiple QR codes

        facultyId = getIntent().getStringExtra("faculty_id");
        Log.d("MainScreen", "Faculty ID received: " + facultyId);

        generateCouponButton = findViewById(R.id.generateCouponButton);
        generateCouponButton.setOnClickListener(v -> checkAssignedCoupons(facultyId));

        checkCouponStatus(facultyId);  // Call function to check coupon status

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Check assigned coupons for the faculty
    private void checkAssignedCoupons(String facultyId) {
        Call<AssignedCouponsResponse> call = apiService.getAssignedCoupons(facultyId);

        call.enqueue(new Callback<AssignedCouponsResponse>() {
            @Override
            public void onResponse(Call<AssignedCouponsResponse> call, Response<AssignedCouponsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AssignedCouponsResponse assignedCouponsResponse = response.body();

                    if (assignedCouponsResponse.isSuccess() && assignedCouponsResponse.getCouponCodes().size() > 0) {
                        displayAssignedCoupons(assignedCouponsResponse.getCouponCodes());
                    } else {
                        Toast.makeText(MainScreen.this, "No assigned coupons found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainScreen.this, "Failed to check assigned coupons!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AssignedCouponsResponse> call, Throwable t) {
                Toast.makeText(MainScreen.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display multiple assigned QR codes
    private void displayAssignedCoupons(List<String> coupons) {
        qrCodeContainer.removeAllViews(); // Clear previous QR codes
        couponStatusTextView.setText("Assigned Coupons:");

        for (String couponCode : coupons) {
            ImageView qrCodeImage = new ImageView(this);
            qrCodeImage.setLayoutParams(new LinearLayout.LayoutParams(400, 400));
            generateQRCode(couponCode, qrCodeImage);
            qrCodeContainer.addView(qrCodeImage); // Add QR code to container
        }
    }

    // Generate QR code
    private void generateQRCode(String couponCode, ImageView qrCodeImageView) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(couponCode, BarcodeFormat.QR_CODE, 400, 400);
            Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.RGB_565);

            for (int x = 0; x < 400; x++) {
                for (int y = 0; y < 400; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
        }
    }

    // Check coupon status using Retrofit
    private void checkCouponStatus(String facultyId) {
        Call<CouponStatusResponse> call = apiService.getCouponStatus(facultyId);

        call.enqueue(new Callback<CouponStatusResponse>() {
            @Override
            public void onResponse(Call<CouponStatusResponse> call, Response<CouponStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().getStatus();
                    if ("used".equals(status)) {
                        generateCouponButton.setEnabled(false);
                        generateCouponButton.setText("Coupon Already Used");
                    } else {
                        generateCouponButton.setEnabled(true);
                        generateCouponButton.setText("Generate Coupon");
                    }
                } else {
                    Toast.makeText(MainScreen.this, "Failed to fetch coupon status!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CouponStatusResponse> call, Throwable t) {
                Toast.makeText(MainScreen.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
