package com.example.facultycouponqr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class COEScreenActivity extends AppCompatActivity {

    private RecyclerView facultyDutiesRecyclerView;
    private FacultyDutiesAdapter adapter;
    private List<FacultyDuty> facultyDutyList;
    private Spinner departmentSpinner;
    private ApiService apiService;

    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coe_screen);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        // Set up RecyclerView
        facultyDutiesRecyclerView = findViewById(R.id.facultyDutiesRecyclerView);
        facultyDutiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up department dropdown (Spinner)
        departmentSpinner = findViewById(R.id.departmentSpinner);
        List<String> departments = Arrays.asList(
                "Electrical and Electronics Engineering",
                "Mechanical Engineering",
                "Robotics and Artificial Intelligence",
                "Computer Science",
                "Civil Engineering",
                "Information Science",
                "Computer and Communication",
                "Electronics and Communication",
                "VLSI",
                "Cyber Security",
                "Artificial Intelligence and Data Science",
                "Artificial Intelligence and Machine Learning"
        );
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(COEScreenActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        // Spinner adapter
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(spinnerAdapter);

        // Handle department selection
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedDepartment = departments.get(position);
                fetchFacultyDuties(selectedDepartment);  // Pass the selected department
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally handles the case where no department is selected
            }
        });

        // Fetch initial duties for the first department
        fetchFacultyDuties(departments.get(0));
    }


    private void fetchFacultyDuties(String department) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<FacultyDuty>> call = apiService.getInvigilationSchedule(department); // Pass department

        call.enqueue(new Callback<List<FacultyDuty>>() {
            @Override
            public void onResponse(Call<List<FacultyDuty>> call, Response<List<FacultyDuty>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    facultyDutyList = response.body();
                    adapter = new FacultyDutiesAdapter(facultyDutyList, (facultyDuty, assignCouponButton) -> {
                        // Call assignCoupon and pass the button
                        assignCoupon(facultyDuty, assignCouponButton);
                    });
                    facultyDutiesRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(COEScreenActivity.this, "Failed to fetch schedule", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FacultyDuty>> call, Throwable t) {
                Log.e("COE_SCREEN", "API Error: " + t.getMessage());
                Toast.makeText(COEScreenActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to handle coupon assignment
    private void assignCoupon(FacultyDuty facultyDuty, Button assignCouponButton) {
        String facultyId = facultyDuty.getFacultyId();
        String couponType = "Lunch";

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.assignCoupon(new CouponRequest(facultyId, couponType));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String couponCode = jsonResponse.optString("coupon_code");

                        Toast.makeText(COEScreenActivity.this, "Coupon assigned successfully!\nCode: " + couponCode, Toast.LENGTH_LONG).show();
                        assignCouponButton.setText("Coupon Assigned: " + couponCode);
                        fetchFacultyDuties(departmentSpinner.getSelectedItem().toString());

                    } catch (IOException | JSONException e) {
                        Log.e("COEActivity", "Error parsing response: " + e.getMessage());
                    }
                } else {
                    try {
                        String error = response.errorBody().string();
                        Log.e("COEActivity", "Failed to assign coupon: " + error);
                        Toast.makeText(COEScreenActivity.this, "Failed to assign coupon!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("COEActivity", "Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(COEScreenActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
