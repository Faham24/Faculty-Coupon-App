package com.example.facultycouponqr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ApiService apiService;
    private EditText emailEditText, passwordEditText;
    private Button userButton, facultyButton, coebutton,HodButton;  // Segmented control buttons
    private String selectedUserType = "faculty";  // Default user type is faculty

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Retrofit client
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Find views
        coebutton = findViewById(R.id.coeButton);
        userButton = findViewById(R.id.userButton);
        facultyButton = findViewById(R.id.facultyButton);
        emailEditText = findViewById(R.id.emailEditText);
        HodButton = findViewById(R.id.HodButton);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button loginButton = findViewById(R.id.signInButton);
        Button registerButton = findViewById(R.id.signupButton);

        // Handle segmented control button click for Faculty
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUserType = "faculty";  // Set selected user type to "faculty"
                userButton.setTextColor(Color.WHITE);  // Highlight the selected option
                facultyButton.setTextColor(Color.GRAY);
                coebutton.setTextColor(Color.GRAY);
                HodButton.setTextColor(Color.GRAY);
            }
        });

        // Handle segmented control button click for Canteen
        facultyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUserType = "canteen";  // Set selected user type to "canteen"
                facultyButton.setTextColor(Color.WHITE);  // Highlight the selected option
                userButton.setTextColor(Color.GRAY);
                coebutton.setTextColor(Color.GRAY);
                HodButton.setTextColor(Color.GRAY);
            }
        });
        coebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUserType = "COE";
                coebutton.setTextColor(Color.WHITE);
                userButton.setTextColor(Color.GRAY);
                facultyButton.setTextColor(Color.GRAY);
                HodButton.setTextColor(Color.GRAY);
            }
        });
        HodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUserType = "HOD";
                HodButton.setTextColor(Color.WHITE);
                coebutton.setTextColor(Color.GRAY);
                userButton.setTextColor(Color.GRAY);
                facultyButton.setTextColor(Color.GRAY);

            }
        });

        // Handle login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (!username.isEmpty() && !password.isEmpty()) {
                    performLogin(username, password, selectedUserType);
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    // Perform login with the selected user type
    private void performLogin(String username, String password, String userType) {
        LoginRequest loginRequest = new LoginRequest(username, password, userType);

        Call<LoginResponse> call = apiService.login(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d("LoginActivity", "Login API response: " + response.body().toString());
                    if (loginResponse.isSuccess()) {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        String facultyId = loginResponse.getFacultyId();
                        Log.d("LoginActivity", "Faculty ID received: " + facultyId);// Get faculty_id from response
                        String department = response.body().getDepartment(); // Assuming response has a getDepartment() method
                        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("department", department);
                        editor.apply();
                        // Redirect based on the user type
                        Intent intent;
                        if (userType.equals("faculty")) {
                            intent = new Intent(LoginActivity.this, MainScreen.class);
                            intent.putExtra("faculty_id", facultyId);
                            Log.d("LoginActivity", "Passing Faculty ID to MainScreen: " + facultyId);// Pass faculty_id
                            startActivity(intent);
                        } else if (userType.equals("canteen")) {
                            intent = new Intent(LoginActivity.this, CanteenActivity.class);
                            startActivity(intent);
                        } else if (userType.equals("COE")) {
                            intent = new Intent(LoginActivity.this, COEScreenActivity.class);
                            startActivity(intent);
                        }
                        else if(userType.equals("HOD")){
                            intent = new Intent(LoginActivity.this, HODManagementActivity.class);
                            startActivity(intent);
                        }
                        finish(); // Close LoginActivity
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed, please try again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

