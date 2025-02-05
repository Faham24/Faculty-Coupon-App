package com.example.facultycouponqr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FacultyDutiesAdapter extends RecyclerView.Adapter<FacultyDutiesAdapter.ViewHolder> {
    private final List<FacultyDuty> facultyDutyList;
    private final OnAssignCouponClickListener onAssignCouponClickListener;

    // Constructor for the adapter
    public FacultyDutiesAdapter(List<FacultyDuty> facultyDutyList, OnAssignCouponClickListener listener) {
        this.facultyDutyList = facultyDutyList;
        this.onAssignCouponClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faculty_duty, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FacultyDuty facultyDuty = facultyDutyList.get(position);
        holder.bind(facultyDuty, onAssignCouponClickListener); // Pass the listener
    }

    @Override
    public int getItemCount() {
        return facultyDutyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView facultyNameText;
        private final TextView examDetailsText;
        private final Button assignCouponButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            facultyNameText = itemView.findViewById(R.id.facultyNameText);
            examDetailsText = itemView.findViewById(R.id.examDetailsText);
            assignCouponButton = itemView.findViewById(R.id.assignCouponButton);
        }

        void bind(FacultyDuty facultyDuty, OnAssignCouponClickListener listener) {
            facultyNameText.setText(facultyDuty.getFacultyName());
            String examDetails = String.format("Exam: %s\nDate: %s\nTime: %s\nBatches: %d",
                    facultyDuty.getExamName(), facultyDuty.getExamDate(), facultyDuty.getExamTime(), facultyDuty.getNumBatches());
            examDetailsText.setText(examDetails);

            // Set OnClickListener for the button
            assignCouponButton.setOnClickListener(v -> listener.onAssignCouponClick(facultyDuty, assignCouponButton)); // Pass the button
        }
    }
}
