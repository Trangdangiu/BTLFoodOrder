package com.example.testbotom.admin;

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
    EditText editTextStartDate, editTextEndDate;
    RecyclerView recyclerView;
    Adapter_revanue adapter_revanue;
    Create_database databaseHelper;
    Button buttonReload;
    private LineChart lineChart;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);
        lineChart = findViewById(R.id.lineChart);

        ArrayList<Entry> lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(1, 10));
        lineEntries.add(new Entry(2, 20));
        lineEntries.add(new Entry(3, 15));
        lineEntries.add(new Entry(4, 30));
        lineEntries.add(new Entry(5, 25));

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Dữ liệu biểu đồ");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        // Tùy chỉnh đồ thị (nếu cần)
        lineChart.getDescription().setText("Biểu đồ đơn giản");
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false); // Vô hiệu hóa trục Y bên phải

        lineChart.invalidate(); // Cập nhật đồ thị
//        databaseHelper = new Create_database(this);
//
//        // Khởi tạo RecyclerView
//        recyclerView = findViewById(R.id.recyclerViewRevenue);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        editTextStartDate = findViewById(R.id.editTextStartDate);
//        editTextEndDate = findViewById(R.id.editTextEndDate);
//        buttonReload = findViewById(R.id.btn_reload);
//
//        // Sự kiện khi nhấn vào ngày bắt đầu và kết thúc
//        editTextStartDate.setOnClickListener(v -> showDatePickerDialog(editTextStartDate));
//        editTextEndDate.setOnClickListener(v -> showDatePickerDialog(editTextEndDate));
//
//        // Sự kiện khi bấm nút reload
//        buttonReload.setOnClickListener(v -> loadRevenueData());
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
        } else {
            // Xóa dữ liệu cũ từ RecyclerView (nếu đã có)
            if (adapter_revanue != null) {
                adapter_revanue.clearData(); // Thêm hàm clearData() để xóa dữ liệu cũ trong adapter
            }

            // Tạo adapter mới với danh sách đơn hàng mới và thiết lập lại cho RecyclerView
            adapter_revanue = new Adapter_revanue(orderItems);
            recyclerView.setAdapter(adapter_revanue);
        }
    }

}
