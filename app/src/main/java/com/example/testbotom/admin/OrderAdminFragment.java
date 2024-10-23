package com.example.testbotom.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testbotom.Adapter.OrderItemAdapter;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.Database.OrderItem;
import com.example.testbotom.R;

import java.util.ArrayList;
import java.util.List;


public class OrderAdminFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrderItemAdapter orderAdapter;
    private List<OrderItem> orderItemList;
    private Create_database db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_admin, container, false);

        recyclerView = view.findViewById(R.id.recycle_order_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = new Create_database(getContext());
        orderItemList = new ArrayList<>();

        loadOrderItems();

        return view;
    }

    private void loadOrderItems() {
        orderItemList.clear(); // Xóa danh sách cũ
        orderItemList.addAll(db.getAllOrderItemsAdmin()); // Lấy dữ liệu từ DB và thêm vào danh sách
        orderAdapter = new OrderItemAdapter(orderItemList); // Tạo adapter với dữ liệu mới
        recyclerView.setAdapter(orderAdapter); // Đặt adapter cho RecyclerView
    }
}
