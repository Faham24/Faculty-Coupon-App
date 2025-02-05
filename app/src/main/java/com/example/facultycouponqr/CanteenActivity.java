package com.example.facultycouponqr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CanteenActivity extends AppCompatActivity {

    private TextView scannedQrCodeTextView;
    private EditText couponNumberEditText;
    private Button scanQrCodeButton, validateCouponButton, generateCouponsButton;
    private String scannedQrCode = null;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_screen);

        scannedQrCodeTextView = findViewById(R.id.scannedQrCodeTextView);
        scanQrCodeButton = findViewById(R.id.scanQrCodeButton);
        validateCouponButton = findViewById(R.id.validateCouponButton);
        generateCouponsButton = findViewById(R.id.generateCouponsButton);
        couponNumberEditText = findViewById(R.id.couponNumberEditText);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Start QR Code scan when the button is clicked
        scanQrCodeButton.setOnClickListener(v -> startQrCodeScanner());

        // Validate the scanned QR code when clicked
        validateCouponButton.setOnClickListener(v -> {
            if (scannedQrCode != null) {
                validateCoupon(scannedQrCode);
            } else {
                Toast.makeText(CanteenActivity.this, "Please scan a QR code first.", Toast.LENGTH_SHORT).show();
            }
        });

        // Generate Coupons when the button is clicked
        generateCouponsButton.setOnClickListener(v -> generateCoupons());
    }

    // Method to generate coupons
    private void generateCoupons() {
        String couponCountStr = couponNumberEditText.getText().toString().trim();

        if (couponCountStr.isEmpty()) {
            Toast.makeText(this, "Please enter the number of coupons to generate", Toast.LENGTH_SHORT).show();
            return;
        }

        int numCoupons;
        try {
            numCoupons = Integer.parseInt(couponCountStr);
            if (numCoupons <= 0) {
                Toast.makeText(this, "Enter a valid number of coupons", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input. Please enter a number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send JSON object instead of raw int
        CouponGenerationRequest request = new CouponGenerationRequest(numCoupons);
        Call<ResponseBody> call = apiService.generateCoupons(request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CanteenActivity.this, "Coupons generated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CanteenActivity.this, "Failed to generate coupons!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CanteenActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Start ZXing QR code scanner
    private void startQrCodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(CanteenActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan Faculty QR Code");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    // Get the result of the QR code scan
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                scannedQrCode = result.getContents();
                scannedQrCodeTextView.setText("Scanned QR Code: " + scannedQrCode);
                Log.d("CanteenActivity", "Scanned QR Code: " + scannedQrCode);
            } else {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Method to validate the scanned coupon
    private void validateCoupon(String scannedQrCode) {
        CouponRequest request = new CouponRequest(scannedQrCode);

        Call<CouponResponse> call = apiService.validateCoupon(request);

        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CouponResponse couponResponse = response.body();
                    if (couponResponse.isSuccess()) {
                        Toast.makeText(CanteenActivity.this, "Coupon validated successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CanteenActivity.this, "Invalid coupon: " + couponResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CanteenActivity.this, "Failed to validate coupon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                Toast.makeText(CanteenActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
