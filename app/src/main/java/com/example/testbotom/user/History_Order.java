package com.example.testbotom.user;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbotom.Adapter.Adapter_History_User;
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

//    private void loadOrderItems() {
//        orderItemList.clear(); // Xóa danh sách cũ
//        // Lấy user_id từ SharedPreferences
//        String userIdStr = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).getString("user_id", "-1");
//        int user_id= Integer.parseInt(userIdStr);
//        // Lấy danh sách đơn hàng của user_id hiện tại
//        orderItemList.addAll(db.getAllOrderItems(user_id)); // Cập nhật để lấy dữ liệu dựa theo user_id
//
//        orderAdapter = new Adapter_History_User(orderItemList); // Tạo adapter với dữ liệu mới
//        recyclerView.setAdapter(orderAdapter); // Đặt adapter cho RecyclerView
//    }

    private void loadOrderItems() {
        orderItemList.clear(); // Xóa danh sách cũ

        // Lấy user_id từ SharedPreferences
        String userIdStr = this.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).getString("user_id", "-1");
        int user_id;

        // Kiểm tra và chuyển đổi userIdStr thành int
        try {
            user_id = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            user_id = -1; // Giá trị mặc định nếu có lỗi trong việc chuyển đổi
        }

        // Lấy danh sách đơn hàng của user_id hiện tại
        orderItemList.addAll(db.getAllOrderItems(user_id)); // Cập nhật để lấy dữ liệu dựa theo user_id

        // Tạo adapter với dữ liệu mới
        orderAdapter = new Adapter_History_User(orderItemList);
        recyclerView.setAdapter(orderAdapter); // Đặt adapter cho RecyclerView
    }


}