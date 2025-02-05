package com.example.facultycouponqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreen extends AppCompatActivity {

    private ApiService apiService;
    private TextView couponStatusTextView;
    private ImageView qrCodeImageView;

    private Button manageExamButton, logoutButton;
    private String facultyId;  // Variable to hold the faculty ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        couponStatusTextView = findViewById(R.id.couponStatus);
        qrCodeImageView = findViewById(R.id.qrCodeImage);

        // Retrieve faculty ID passed from LoginActivity
        facultyId = getIntent().getStringExtra("faculty_id");
        Log.d("MainScreen", "Faculty ID received from LoginActivity: " + facultyId);

        Button generateCouponButton = findViewById(R.id.generateCouponButton);
        generateCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAssignedCoupon(facultyId);  // Check if a coupon is already assigned
            }
        });

        manageExamButton = findViewById(R.id.manageExamButton);
        manageExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, FacultyExamActivity.class);
                intent.putExtra("faculty_id", facultyId);  // Pass faculty ID to FacultyExamActivity
                startActivity(intent);
            }
        });
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Check if the faculty already has an assigned coupon
    private void checkAssignedCoupon(String facultyId) {
        Call<ResponseBody> call = apiService.getAssignedCoupon(facultyId); // API call to check coupon

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String couponCode = null;  // Parse coupon code from response
                    try {
                        couponCode = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (couponCode != null && !couponCode.isEmpty()) {
                        // Display the existing coupon code
                        couponStatusTextView.setText("Coupon Code: " + couponCode);
                        generateQRCode(couponCode);  // Generate QR code for the existing coupon
                    } else {
                        // No coupon assigned, generate a new one
                        CouponRequest couponRequest = new CouponRequest(facultyId);
                        generateCoupon(couponRequest);
                    }
                } else {
                    Toast.makeText(MainScreen.this, "Failed to check coupon assignment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainScreen.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to send coupon generation request if no coupon is assigned
    private void generateCoupon(CouponRequest couponRequest) {
        Call<CouponResponse> call = apiService.generateCoupon(couponRequest);

        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CouponResponse couponResponse = response.body();
                    if (couponResponse.isSuccess()) {
                        // Display the generated coupon code
                        String couponCode = couponResponse.getCouponCode();
                        couponStatusTextView.setText("Coupon Generated: " + couponCode);
                        generateQRCode(couponCode);  // Generate and display QR code
                        Toast.makeText(MainScreen.this, "Coupon generated successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainScreen.this, "Failed to generate coupon: " + couponResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainScreen.this, "Failed to generate coupon!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                Toast.makeText(MainScreen.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to generate QR code using ZXing
    private void generateQRCode(String couponCode) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(couponCode, BarcodeFormat.QR_CODE, 400, 400);
            Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.RGB_565);

            for (int x = 0; x < 400; x++) {
                for (int y = 0; y < 400; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF); // Black for QR code, white for background
                }
            }

            qrCodeImageView.setImageBitmap(bitmap);  // Display QR code in ImageView
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
        }
    }
}
