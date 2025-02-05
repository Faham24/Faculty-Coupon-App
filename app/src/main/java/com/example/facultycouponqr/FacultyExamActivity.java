package com.example.facultycouponqr;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultyExamActivity extends AppCompatActivity {

    private EditText examNameEditText, numBatchesEditText, examDateEditText, examTimeEditText;
    private Button submitExamDetailsButton;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_exam);

        // Initialize views
        examNameEditText = findViewById(R.id.examNameEditText);
        numBatchesEditText = findViewById(R.id.numBatchesEditText);
        examDateEditText = findViewById(R.id.examDateEditText);
        examTimeEditText = findViewById(R.id.examTimeEditText);
        submitExamDetailsButton = findViewById(R.id.submitExamDetailsButton);

        // Get faculty ID from intent
        String facultyId = getIntent().getStringExtra("faculty_id");
        Log.d("FacultyExamActivity", "Faculty ID received: " + facultyId);

        // Initialize Retrofit service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Handle Submit Button Click
        submitExamDetailsButton.setOnClickListener(v -> submitExamDetails(facultyId));
    }

    private void submitExamDetails(String facultyId) {
        String examName = examNameEditText.getText().toString().trim();
        String numBatchesStr = numBatchesEditText.getText().toString().trim();
        String examDate = examDateEditText.getText().toString().trim();
        String examTime = examTimeEditText.getText().toString().trim();

        Log.d("FacultyExamActivity", "Submitting data: " +
                "facultyId: " + facultyId +
                ", examName: " + examName +
                ", numBatches: " + numBatchesStr +
                ", examDate: " + examDate +
                ", examTime: " + examTime);

        if (examName.isEmpty() || numBatchesStr.isEmpty() || examDate.isEmpty() || examTime.isEmpty()) {
            Toast.makeText(FacultyExamActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate exam date format (YYYY-MM-DD)
        if (!examDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Toast.makeText(FacultyExamActivity.this, "Please enter a valid date (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert numBatchesStr to int
        int num_Batches;
        try {
            num_Batches = Integer.parseInt(numBatchesStr);
        } catch (NumberFormatException e) {
            Toast.makeText(FacultyExamActivity.this, "Number of batches must be a valid integer", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading indicator for fetching faculty name
        final ProgressDialog fetchProgressDialog = new ProgressDialog(FacultyExamActivity.this);
        fetchProgressDialog.setMessage("Fetching faculty name...");
        fetchProgressDialog.setCancelable(false);
        fetchProgressDialog.show();

        // Fetch faculty name using the faculty ID
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Faculty> call = apiService.getFacultyName(facultyId); // Assuming you have this endpoint
        call.enqueue(new Callback<Faculty>() {
            @Override
            public void onResponse(Call<Faculty> call, Response<Faculty> response) {
                fetchProgressDialog.dismiss(); // Dismiss loading indicator for fetching faculty name

                if (response.isSuccessful() && response.body() != null) {
                    String facultyName = response.body().getName(); // Assuming getName() method exists

                    // Now create FacultyDuty object with faculty name
                    FacultyDuty duty = new FacultyDuty(facultyId, facultyName, examName, num_Batches, examDate, examTime);
                    Log.d("FacultyExamActivity", "Payload: " + new Gson().toJson(duty));
                    // Log the FacultyDuty object before submitting
                    Log.d("FacultyExamActivity", "Submitting data: " + duty.toString());  // Logging the FacultyDuty object

                    // Show loading indicator for submitting exam details
                    final ProgressDialog submitProgressDialog = new ProgressDialog(FacultyExamActivity.this);
                    submitProgressDialog.setMessage("Submitting...");
                    submitProgressDialog.setCancelable(false);
                    submitProgressDialog.show();

                    // Make API call to submit exam details
                    Call<ResponseBody> submitCall = apiService.submitExamDetails((List<FacultyDuty>) duty);
                    submitCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            submitProgressDialog.dismiss(); // Dismiss loading indicator
                            if (response.isSuccessful()) {
                                Toast.makeText(FacultyExamActivity.this, "Exam details submitted!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                try {
                                    Toast.makeText(FacultyExamActivity.this, "Submission failed: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Log.d("FacultyExamActivity", "Submitting data: " +
                                        "facultyId: " + facultyId +
                                        ", examName: " + examName +
                                        ", numBatches: " + numBatchesStr +
                                        ", examDate: " + examDate +
                                        ", examTime: " + examTime);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            submitProgressDialog.dismiss(); // Dismiss loading indicator
                            Toast.makeText(FacultyExamActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FacultyExamActivity", "API error: " + t.getMessage());
                        }
                    });
                } else {
                    fetchProgressDialog.dismiss(); // Dismiss loading indicator
                    Toast.makeText(FacultyExamActivity.this, "Failed to fetch faculty name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Faculty> call, Throwable t) {
                fetchProgressDialog.dismiss(); // Dismiss loading indicator
                Toast.makeText(FacultyExamActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FacultyExamActivity", "API error: " + t.getMessage());
            }
        });
    }
}
