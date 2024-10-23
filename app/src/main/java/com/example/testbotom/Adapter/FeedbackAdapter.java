package com.example.testbotom.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbotom.Database.Feedback;
import com.example.testbotom.R;


import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    private List<Feedback> feedbackList;

    public FeedbackAdapter(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_back, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        holder.tvHoTen.setText(feedback.getHoTen());
        holder.tvSdt.setText(feedback.getSdt());
        holder.tvEmail.setText(feedback.getEmail());
        holder.tvBinhLuan.setText(feedback.getBinhLuan());
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTen, tvSdt, tvEmail, tvBinhLuan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            tvSdt = itemView.findViewById(R.id.tvSdt);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvBinhLuan = itemView.findViewById(R.id.tvBinhLuan);
        }
    }
}
