package com.example.testbotom.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbotom.Database.Create_database;
import com.example.testbotom.Database.OrderItem;
import com.example.testbotom.R;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderViewHolder> {
    private List<OrderItem> orderItems;

    public OrderItemAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_order_detail, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        holder.bind(orderItem);

        // Cập nhật trạng thái của checkbox dựa trên `isDelivery`
        holder.checkBox.setChecked(orderItem.isDelivery());

        // Thay đổi màu nền item layout dựa vào trạng thái của checkbox
        if (orderItem.isDelivery()) {
            holder.itemView.setBackgroundColor(Color.LTGRAY); // Màu xám
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE); // Màu trắng
        }

        // Thiết lập listener cho checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            orderItem.setDelivery(isChecked); // Cập nhật trạng thái cho object

            // Cập nhật vào database
            Create_database db = new Create_database(holder.itemView.getContext());
            db.updateOrderDeliveryStatus(orderItem.getOrderItemId(), isChecked);  // Lưu vào cơ sở dữ liệu

            // Cập nhật màu nền item dựa trên trạng thái
            if (isChecked) {
                holder.itemView.setBackgroundColor(Color.LTGRAY); // Màu xám khi đã giao hàng
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE); // Màu trắng khi chưa giao hàng
            }
        });
    }


    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        // Khai báo các view trong layout item_order
        TextView txtOrderCode, txtFullName, txtTotalAmount, txt_sdt, txt_adress, txt_menu, txt_ngaydat, txt_payment;
        CheckBox checkBox;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderCode = itemView.findViewById(R.id.txt_order_code);
            txtFullName = itemView.findViewById(R.id.txt_full_name);
            txtTotalAmount = itemView.findViewById(R.id.txt_total_amount);
            txt_sdt = itemView.findViewById(R.id.txt_sdt);
            txt_adress = itemView.findViewById(R.id.txt_address);
            txt_menu = itemView.findViewById(R.id.txt_thucdon);
            txt_ngaydat = itemView.findViewById(R.id.txt_ngaydat);
            txt_payment = itemView.findViewById(R.id.txt_payment);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        public void bind(OrderItem orderItem) {
            txtOrderCode.setText(orderItem.getOrderCode());
            txtFullName.setText(orderItem.getFullName());
            txtTotalAmount.setText(String.valueOf(orderItem.getTotalAmount()));
            txt_sdt.setText(orderItem.getPhoneNumber());
            txt_adress.setText(orderItem.getAddress());
            txt_menu.setText(orderItem.getMenu());
            txt_ngaydat.setText(orderItem.getOrderDate());
            txt_payment.setText(orderItem.getPaymentMethod());
        }
    }
}
