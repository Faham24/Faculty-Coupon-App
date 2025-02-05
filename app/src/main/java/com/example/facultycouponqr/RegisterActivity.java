package com.example.facultycouponqr;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, facultyIdEditText, emailEditText;
    private Spinner departmentSpinner;
    private Button registerButton;

    private RadioGroup roleRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameEditText);
        facultyIdEditText = findViewById(R.id.facultyIdEditText);
        emailEditText = findViewById(R.id.emailEditText);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        registerButton = findViewById(R.id.registerButton);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);
        // Set up the Spinner with departments
        String[] departments = {
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
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(adapter);

        registerButton.setOnClickListener(v -> registerFaculty());
    }

    private void registerFaculty() {
        String name = nameEditText.getText().toString().trim();
        String facultyId = facultyIdEditText.getText().toString().trim();
        String department = departmentSpinner.getSelectedItem().toString(); // Get selected department
        String email = emailEditText.getText().toString().trim();

        if (name.isEmpty() || facultyId.isEmpty() || department.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        String role;
        int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
        if(selectedRoleId == R.id.radioHOD)
        {
            role="HOD";
        }
        else{
            role="faculty";
        }
        Log.d("RegisterActivity", "facultyId: " + facultyId);

        // Prepare your registration request object
        Faculty faculty = new Faculty(facultyId, name, email, department,role);

        Log.d("RegisterActivity", "Registering Faculty: " + faculty.toString());

        // Call the API to register
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.registerFaculty(faculty);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity or redirect to login
                } else {
                    try {
                        Log.e("RegisterActivity", "Registration failed: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("RegisterActivity", "Error parsing error body: " + e.getMessage());
                    }
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
