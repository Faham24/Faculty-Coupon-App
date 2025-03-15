package com.example.facultycouponqr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HODManagementActivity extends AppCompatActivity {

    private RecyclerView facultyRecyclerView;
    private FacultyAdapter facultyAdapter;
    private List<Faculty> facultyList;

    private String facultyId;

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_screen);

        facultyRecyclerView = findViewById(R.id.facultyRecyclerView);
        facultyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HODManagementActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        // Fetch faculty list from the backend
        fetchFacultyList();
    }


    private void fetchFacultyList() {
        // Retrieve department from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String department = sharedPreferences.getString("department", null);

        if (department == null) {
            Toast.makeText(this, "Department not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Faculty>> call = apiService.getFacultyList(department); // Ensure the API endpoint is properly set in ApiService

        call.enqueue(new Callback<List<Faculty>>() {
            @Override
            public void onResponse(Call<List<Faculty>> call, Response<List<Faculty>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    facultyList = response.body();
                    Log.d("HODManagementActivity", "Raw JSON Response: " + new Gson().toJson(response.body()));
                    for (Faculty faculty : facultyList) {
                        Log.d("HODManagementActivity", "Fetched Faculty: " + faculty.getFacultyId() + ", Name: " + faculty.getName());
                    }
                    facultyAdapter = new FacultyAdapter(facultyList, faculty -> {
                        Log.d("HODManagementActivity", "Selected Faculty ID: " + faculty.getFacultyId());
                        Intent intent = new Intent(HODManagementActivity.this, HODExamActivity.class);
                        intent.putExtra("faculty_id", faculty.getFacultyId());
                        startActivity(intent);
                    });
                    facultyRecyclerView.setAdapter(facultyAdapter);
                } else {
                    Toast.makeText(HODManagementActivity.this, "Failed to fetch faculty list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Faculty>> call, Throwable t) {
                Log.e("HODManagementActivity", "Error fetching faculty list: " + t.getMessage());
                Toast.makeText(HODManagementActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
