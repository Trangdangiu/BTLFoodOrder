package com.example.testbotom.admin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbotom.Adapter.Adapter_revanue;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.Database.OrderItem;
import com.example.testbotom.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbotom.Adapter.Adapter_revanue;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.Database.OrderItem;
import com.example.testbotom.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Revenue extends AppCompatActivity {
    private EditText editTextStartDate, editTextEndDate;
    private RecyclerView recyclerView;
    private Adapter_revanue adapter_revanue;
    private Create_database databaseHelper;
    private Button buttonReload;
    private LineChart lineChart;
    private TextView txt_alltotalamount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        // Khởi tạo các thành phần UI
        lineChart = findViewById(R.id.lineChart);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        buttonReload = findViewById(R.id.btn_reload);
        recyclerView = findViewById(R.id.recyclerViewRevenue);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        txt_alltotalamount=findViewById(R.id.view_totalamount);
        // Khởi tạo cơ sở dữ liệu
        databaseHelper = new Create_database(this);

        // Sự kiện khi nhấn vào ngày bắt đầu và kết thúc
        editTextStartDate.setOnClickListener(v -> showDatePickerDialog(editTextStartDate));
        editTextEndDate.setOnClickListener(v -> showDatePickerDialog(editTextEndDate));

        // Sự kiện khi bấm nút reload
        buttonReload.setOnClickListener(v -> loadRevenueData());
    }

    private void showDatePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Định dạng ngày sau khi chọn
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editText.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void loadRevenueData() {
        String startDate = editTextStartDate.getText().toString();
        String endDate = editTextEndDate.getText().toString();

        // Kiểm tra nếu ngày bắt đầu và ngày kết thúc không trống
        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn cả ngày bắt đầu và ngày kết thúc!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy danh sách đơn hàng từ cơ sở dữ liệu theo khoảng thời gian
        List<OrderItem> orderItems = databaseHelper.getOrderItemsByDate(startDate, endDate);

        if (orderItems.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu doanh thu!", Toast.LENGTH_SHORT).show();
            lineChart.clear(); // Xóa dữ liệu biểu đồ
            return;
        }

        // Tính doanh thu từ đơn hàng
        ArrayList<Entry> lineEntries = new ArrayList<>();
        float totalRevenue = 0;
        int index = 0;

        for (OrderItem item : orderItems) {
            totalRevenue += item.getTotalAmount();
            lineEntries.add(new Entry(index++, totalRevenue)); // Thêm doanh thu vào biểu đồ
        }

        for (OrderItem item : orderItems) {
            totalRevenue += item.getTotalAmount();
            // Thêm doanh thu vào biểu đồ
        }
        txt_alltotalamount.setText(totalRevenue+" VND");

        // Cập nhật biểu đồ
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Doanh thu theo thời gian");
        lineDataSet.setColor(getResources().getColor(R.color.colorPrimary)); // Đặt màu cho đường
        lineDataSet.setValueTextColor(getResources().getColor(R.color.black)); // Đặt màu cho giá trị
        lineDataSet.setValueTextSize(12f); // Kích thước chữ cho giá trị

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setText("Doanh thu trong khoảng thời gian"); // Mô tả biểu đồ
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt trục X ở phía dưới
        lineChart.invalidate(); // Cập nhật biểu đồ

        // Cập nhật RecyclerView
        if (adapter_revanue != null) {
            adapter_revanue.clearData(); // Xóa dữ liệu cũ từ RecyclerView (nếu đã có)
        }
        adapter_revanue = new Adapter_revanue(orderItems);
        recyclerView.setAdapter(adapter_revanue);
    }


}
