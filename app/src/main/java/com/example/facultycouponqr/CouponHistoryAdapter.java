package com.example.facultycouponqr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CouponHistoryAdapter extends RecyclerView.Adapter<CouponHistoryAdapter.ViewHolder> {
    private List<CouponHistory> historyList;

    public CouponHistoryAdapter(List<CouponHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CouponHistory history = historyList.get(position);
        holder.txtFacultyName.setText(history.getFacultyName());
        holder.txtDepartment.setText(history.getDepartment());
        holder.txtCouponCode.setText(history.getCouponCode());
        holder.txtCouponType.setText(history.getCouponType());
        holder.txtAssignedDate.setText(history.getAssignedDate());
        holder.txtExamName.setText(history.getExamName());
        holder.txtExamTime.setText(history.getExamTime());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFacultyName, txtDepartment, txtCouponCode, txtCouponType, txtAssignedDate, txtExamName, txtExamTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFacultyName = itemView.findViewById(R.id.txtFacultyName);
            txtDepartment = itemView.findViewById(R.id.txtDepartment);
            txtCouponCode = itemView.findViewById(R.id.txtCouponCode);
            txtCouponType = itemView.findViewById(R.id.txtCouponType);
            txtAssignedDate = itemView.findViewById(R.id.txtAssignedDate);
            txtExamName = itemView.findViewById(R.id.txtExamName);
            txtExamTime = itemView.findViewById(R.id.txtExamTime);
        }
    }
}
