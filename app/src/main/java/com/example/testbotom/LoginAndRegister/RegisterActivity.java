package com.example.testbotom.LoginAndRegister;

import static com.example.testbotom.user.OtpGenerator.generateOtp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testbotom.Database.Create_database;
import com.example.testbotom.R;
import com.example.testbotom.user.OtpGenerator;
import com.example.testbotom.user.SendEmailTask;

public class RegisterActivity extends AppCompatActivity {
    private Create_database dbHelper;
    private EditText editTextEmail, editTextPassword, editTextOtp;
    private RadioGroup radioGroupRole;
    private  Button btn_cancel_register, buttonRegister;
    private String generatedOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        dbHelper = new Create_database(this);
        editTextEmail = findViewById(R.id.id_edit_emaildki);
        editTextPassword = findViewById(R.id.id_edit_passdki);
        radioGroupRole = findViewById(R.id.id_role);
        buttonRegister = findViewById(R.id.id_btn_dki);
        btn_cancel_register = findViewById(R.id.id_btn_cancel);

        buttonRegister.setOnClickListener(view -> {
            // Kiểm tra người dùng đã chọn vai trò
            int selectedId = radioGroupRole.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(RegisterActivity.this, "Vui lòng chọn vai trò", Toast.LENGTH_SHORT).show();
                return;
            }

            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nếu email hợp lệ, gửi Otp và điều hướng tới verifyOtpActivity
            generatedOtp = generateOtp();
            new SendEmailTask().execute(email,generatedOtp);
            Toast.makeText(RegisterActivity.this, "Mã OTP đã được gửi qua email "+ email +" vui lòng kiểm tra hộp thư", Toast.LENGTH_SHORT).show();

            // điều hướng
            Intent intent = new Intent(RegisterActivity.this, VerifyOtpActivity.class);
            intent.putExtra("action", "register");
            intent.putExtra("otp", generatedOtp);
            intent.putExtra("role", ((RadioButton) findViewById(selectedId)).getText().toString());
            intent.putExtra("email", email);
            intent.putExtra("password", password);startActivity(intent);
            finish();
        });

        // Handle cancel button click event
        btn_cancel_register.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LogginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private String generateOtp(){
        return OtpGenerator.generateOtp();
    }
}