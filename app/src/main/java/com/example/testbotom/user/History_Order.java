package com.example.testbotom.user;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbotom.Adapter.Adapter_History_User;
import com.example.testbotom.Adapter.OrderItemAdapter;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.Database.OrderItem;
import com.example.testbotom.R;

import java.util.ArrayList;
import java.util.List;

public class History_Order extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Adapter_History_User orderAdapter;
    private List<OrderItem> orderItemList;
    private Create_database db;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history_order);
        recyclerView = findViewById(R.id.recycle_history_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> {
            finish();
        });

        db = new Create_database(this);
        orderItemList = new ArrayList<>();

        // Lấy dữ liệu từ cơ sở dữ liệu và hiển thị lên RecyclerView
        loadOrderItems();
    }

    private void loadOrderItems() {
        orderItemList.clear(); // Xóa danh sách cũ
        orderItemList.addAll(db.getAllOrderItems()); // Lấy dữ liệu từ DB và thêm vào danh sách
        orderAdapter = new Adapter_History_User(orderItemList); // Tạo adapter với dữ liệu mới
        recyclerView.setAdapter(orderAdapter); // Đặt adapter cho RecyclerView
    }
}