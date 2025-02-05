package com.example.facultycouponqr;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.ViewHolder> {

    private final List<Faculty> facultyList;
    private final OnFacultyClickListener onFacultyClickListener;

    public interface OnFacultyClickListener {
        void onFacultyClick(Faculty faculty);
    }

    public FacultyAdapter(List<Faculty> facultyList, OnFacultyClickListener listener) {
        this.facultyList = facultyList;
        this.onFacultyClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faculty, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Faculty faculty = facultyList.get(position);
        Log.d("FacultyAdapter", "Binding Faculty: " + faculty.getFacultyId() + ", Name: " + faculty.getName());
        holder.bind(faculty, onFacultyClickListener);
    }

    @Override
    public int getItemCount() {
        return facultyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView facultyNameText;
        private final Button manageExamButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            facultyNameText = itemView.findViewById(R.id.facultyNameText);
            manageExamButton = itemView.findViewById(R.id.manageExamButton);
        }

        void bind(Faculty faculty, OnFacultyClickListener listener) {
            facultyNameText.setText(faculty.getName());
            manageExamButton.setOnClickListener(v -> listener.onFacultyClick(faculty));
        }
    }
}
