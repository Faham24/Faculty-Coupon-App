package com.example.facultycouponqr;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HODExamActivity extends AppCompatActivity {

    private LinearLayout batchesContainer;
    private Button submitExamDetailsButton, addBatchesButton;
    private String facultyId;
    private ApiService apiService;

    private List<View> batchViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_exam);

        batchesContainer = findViewById(R.id.batchesContainer);
        submitExamDetailsButton = findViewById(R.id.submitExamDetailsButton);
        addBatchesButton = findViewById(R.id.addBatchesButton);

        facultyId = getIntent().getStringExtra("faculty_id");
        apiService = RetrofitClient.getClient().create(ApiService.class);

        addBatchesButton.setOnClickListener(v -> {
            String numBatchesStr = ((EditText) findViewById(R.id.numBatchesEditText)).getText().toString().trim();
            if (numBatchesStr.isEmpty()) {
                Toast.makeText(this, "Please enter number of batches", Toast.LENGTH_SHORT).show();
                return;
            }

            int totalBatches;
            try {
                totalBatches = Integer.parseInt(numBatchesStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number of batches", Toast.LENGTH_SHORT).show();
                return;
            }

            generateBatchInputFields(totalBatches);
        });

        submitExamDetailsButton.setOnClickListener(v -> submitExamDetails());
    }

    private void generateBatchInputFields(int totalBatches) {
        batchesContainer.removeAllViews();
        batchViews.clear();

        for (int i = 1; i <= totalBatches; i++) {
            View batchView = getLayoutInflater().inflate(R.layout.batch_details, batchesContainer, false);

            TextView batchLabel = batchView.findViewById(R.id.batchLabel);
            batchLabel.setText("Batch " + i);

            EditText dateEditText = batchView.findViewById(R.id.examDateEditText);
            dateEditText.setOnClickListener(v -> showDatePicker(dateEditText));

            batchesContainer.addView(batchView);
            batchViews.add(batchView);
        }
    }

    private void showDatePicker(EditText dateEditText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    dateEditText.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void submitExamDetails() {
        List<FacultyDuty> duties = new ArrayList<>();

        for (View batchView : batchViews) {
            String examName = ((EditText) batchView.findViewById(R.id.examNameEditText)).getText().toString().trim();
            String examDate = ((EditText) batchView.findViewById(R.id.examDateEditText)).getText().toString().trim();
            String examTime = ((EditText) batchView.findViewById(R.id.examTimeEditText)).getText().toString().trim();

            if (examName.isEmpty() || examDate.isEmpty() || examTime.isEmpty()) {
                Toast.makeText(this, "Please fill all fields for all batches", Toast.LENGTH_SHORT).show();
                return;
            }

            FacultyDuty duty = new FacultyDuty(facultyId, null, examName, batchViews.size(), examDate, examTime);
            duties.add(duty);
        }

        Log.d("HODExamActivity", "Submitting duties: " + new Gson().toJson(duties));

        // Submit duties to the backend
        Call<ResponseBody> call = apiService.submitExamDetails(duties); // Ensure API accepts List<FacultyDuty>
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HODExamActivity.this, "Exam details submitted!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(HODExamActivity.this, "Failed to submit exam details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HODExamActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
