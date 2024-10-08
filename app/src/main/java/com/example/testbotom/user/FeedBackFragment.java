package com.example.testbotom.user;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testbotom.Database.Create_database;
import com.example.testbotom.R;

public class FeedBackFragment extends Fragment {

    private EditText edtHoTen, edtSdt, edtBinhLuan, edtEmail;
    private Button btnSubmit;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout for the fragment
        View view = inflater.inflate(R.layout.fragment_feed_back, container, false);

        // Khởi tạo EditText và Button
        edtHoTen = view.findViewById(R.id.enter_hoten);
        edtSdt = view.findViewById(R.id.enter_sđt);
        edtBinhLuan = view.findViewById(R.id.enter_comment);
        edtEmail = view.findViewById(R.id.enter_email);
        btnSubmit = view.findViewById(R.id.enter_feedback);

        // Lấy email từ SharedPreferences và hiển thị
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", null);
        edtEmail.setText(email); // Hiển thị email
        edtEmail.setEnabled(false); // Không cho phép chỉnh sửa email

        // Xử lý sự kiện gửi phản hồi
        btnSubmit.setOnClickListener(v -> {
            String hoTen = edtHoTen.getText().toString().trim();
            String sdt = edtSdt.getText().toString().trim();
            String binhLuan = edtBinhLuan.getText().toString().trim();

            if (hoTen.isEmpty() || sdt.isEmpty() || binhLuan.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                // Lưu phản hồi vào cơ sở dữ liệu
                Create_database db = new Create_database(getContext());
                boolean success = db.insertFeedback(hoTen, sdt, email, binhLuan);
                if (success) {
                    Toast.makeText(getContext(), "Phản hồi đã được gửi thành công!", Toast.LENGTH_SHORT).show();
                    // Reset các trường
                    edtHoTen.setText("");
                    edtSdt.setText("");
                    edtBinhLuan.setText("");
                } else {
                    Toast.makeText(getContext(), "Có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
