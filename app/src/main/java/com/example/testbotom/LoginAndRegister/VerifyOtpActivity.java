package com.example.testbotom.LoginAndRegister;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testbotom.Database.Create_database;
import com.example.testbotom.R;

public class VerifyOtpActivity extends AppCompatActivity {
    private Create_database dbHelper;
    private Button btn_confirm_otp, btn_cancel;
    private String sentOtp;
    private EditText editTextOtp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        // khởi tạo db
        dbHelper = new Create_database(this);

        editTextOtp = findViewById(R.id.id_verify_otp);
        btn_confirm_otp = findViewById(R.id.id_btn_confirm_otp);
        btn_cancel = findViewById(R.id.button_cancel);

        // Get the OTP sent from the previous activity
        Intent intent = getIntent();
        sentOtp = intent.getStringExtra("otp");

        // chọn hành động tương ứng
        String action = intent.getStringExtra("action");


        btn_confirm_otp.setOnClickListener(v -> {
            if(action.equals("register")){
                String enteredOtp = editTextOtp.getText().toString().trim();
                if (enteredOtp.equals(sentOtp.trim())) {
                    Toast.makeText(VerifyOtpActivity.this, "Xác thực OTP thành công!", Toast.LENGTH_SHORT).show();
                    String email = intent.getStringExtra("email");
                    String password = intent.getStringExtra("password");
                    String role = intent.getStringExtra("role");

                    // check user Exist by email
                    if(dbHelper.checkUser(email)){
                        Toast.makeText(VerifyOtpActivity.this, "Email đã tồn tại, vui lòng điền email khác", Toast.LENGTH_SHORT).show();
                        // navigate to register
                        Intent intentRegister = new Intent(VerifyOtpActivity.this, RegisterActivity.class);
                        startActivity(intentRegister);
                        finish();
                    } else {
                        if (dbHelper.registerUser(email, password, role)) {
                            Toast.makeText(VerifyOtpActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                            // navigate to login if success
                            Intent intentLogin = new Intent(VerifyOtpActivity.this, LogginActivity.class);
                            startActivity(intentLogin);
                            finish(); // Đóng Activity
                        } else {Log.e("RegisterError", "Đăng ký thất bại cho email: " + email);
                            Toast.makeText(VerifyOtpActivity.this, "Đăng Kí Thất Bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(VerifyOtpActivity.this, "Mã OTP sai, Vui lòng nhập lại.", Toast.LENGTH_SHORT).show();
                }
            } else if (action.equals("reset_password")){
                String enteredOtp = editTextOtp.getText().toString().trim();
                if(enteredOtp.equals(sentOtp.trim())){
                    // nếu nhập đúng sẽ điều hướng sang trang reset_password
                    String email = intent.getStringExtra("email");
                    Intent intentChangePass = new Intent(VerifyOtpActivity.this, ResetPassword.class);
                    intentChangePass.putExtra("email", email);
                    startActivity(intentChangePass);
                    finish();
                } else {
                    Log.e("VerifyOtpError", "Xác nhận mã OTP thất bại");
                    Toast.makeText(VerifyOtpActivity.this, "Xác nhận mã OTP không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel.setOnClickListener(v  -> {
            Intent intentLogin = new Intent(VerifyOtpActivity.this, LogginActivity.class);
            startActivity(intentLogin);
            finish();
        });
    }
}