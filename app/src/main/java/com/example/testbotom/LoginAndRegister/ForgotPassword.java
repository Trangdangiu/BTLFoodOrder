package com.example.testbotom.LoginAndRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testbotom.Database.Create_database;
import com.example.testbotom.R;
import com.example.testbotom.user.OtpGenerator;
import com.example.testbotom.user.SendEmailTask;

public class ForgotPassword extends AppCompatActivity {
    private Create_database dbHelper;
    private EditText forgot_password_email;
    private Button btn_reset_password, btn_cancel;
    private String generateOtp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        dbHelper = new Create_database(this);
        forgot_password_email = findViewById(R.id.forgot_password_email);
        btn_reset_password = findViewById(R.id.btn_reset_password);
        btn_cancel = findViewById(R.id.button_cancel);

        // xử lý khi nhấp enter thay vì xuống dòng sẽ click button
        forgot_password_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    btn_reset_password.performClick();
                    return true;
                }
                return false;
            }
        });

        btn_reset_password.setOnClickListener(view -> {
            String email = forgot_password_email.getText().toString().trim();
            System.out.println(email);
            // check email exist
            if(dbHelper.checkUser(email)){
                // Nếu email hợp lệ, gửi Otp và điều hướng tới verifyOtpActivity
                generateOtp = generateOtp();
                new SendEmailTask().execute(email,generateOtp);

                // navigate
                Intent intentVerify = new Intent(ForgotPassword.this, VerifyOtpActivity.class);
                intentVerify.putExtra("otp", generateOtp);
                intentVerify.putExtra("action", "reset_password");
                intentVerify.putExtra("email", email);
                startActivity(intentVerify);
                finish();
            } else {
                Toast.makeText(ForgotPassword.this, "Email chưa được đăng kí, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });

        btn_cancel.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPassword.this, LogginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private String generateOtp(){return OtpGenerator.generateOtp();
    }
}