package com.example.testbotom.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbotom.Database.OrderItem;
import com.example.testbotom.R;

import java.util.List;

public class Adapter_revanue extends RecyclerView.Adapter<Adapter_revanue.RevenueViewHolder> {
    private List<OrderItem> revenueItemList;

    public Adapter_revanue(List<OrderItem> revenueItemList) {
        this.revenueItemList = revenueItemList;
    }

    @NonNull
    @Override
    public RevenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_revenue_order, parent, false);
        return new RevenueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RevenueViewHolder holder, int position) {
        OrderItem item = revenueItemList.get(position);
        holder.txtOrderId.setText("Mã đơn hàng: " + item.getOrderCode());
        holder.txtOrderDate.setText("Ngày đặt: " + item.getOrderDate());
        holder.txtTotalAmount.setText("Tổng số tiền: " + item.getTotalAmount() + " VNĐ");
    }

    @Override
    public int getItemCount() {
        return revenueItemList.size();
    }

    static class RevenueViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtOrderDate, txtTotalAmount;

        public RevenueViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txt_order_id);
            txtOrderDate = itemView.findViewById(R.id.txt_order_date);
            txtTotalAmount = itemView.findViewById(R.id.txt_total_amount);
        }
    }

    public void clearData() {
        if (revenueItemList != null) {
            revenueItemList.clear(); // Xóa danh sách cũ
            notifyDataSetChanged(); // Thông báo RecyclerView để cập nhật giao diện
        }
    }
}

