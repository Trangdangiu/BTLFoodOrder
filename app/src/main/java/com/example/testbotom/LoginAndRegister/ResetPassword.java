package com.example.testbotom.LoginAndRegister;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testbotom.Database.Create_database;
import com.example.testbotom.R;

public class ResetPassword extends AppCompatActivity {
    private Create_database dbHelper;
    private EditText newPassword, newPassConfirm;
    private Button btn_cancel, btn_change_pass;
    private TextView textView_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        dbHelper = new Create_database(this);
        newPassword = findViewById(R.id.editTextNewPassword);
        newPassConfirm = findViewById(R.id.editTextNewPasswordConfirm);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_change_pass = findViewById(R.id.btn_change_password);
        textView_title = findViewById(R.id.textView_title);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        textView_title.setText("Khôi phục lại mật khẩu cho tài khoản " + email);

        btn_change_pass.setOnClickListener(view -> {
            String newPass = newPassword.getText().toString().trim();
            String passConfirm = newPassConfirm.getText().toString().trim();

            // compare
            if(newPass.equals(passConfirm)){

                if(dbHelper.updatePasswordByEmail(email, newPass)){
                    Toast.makeText(ResetPassword.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    // navigate to login
                    Intent intentLogin = new Intent(ResetPassword.this, LogginActivity.class);
                    startActivity(intentLogin);
                    finish();
                } else {
                    Toast.makeText(ResetPassword.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ResetPassword.this, "Mật khẩu không khớp, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }

        });

        btn_cancel.setOnClickListener(view -> {
            Intent intentLogin = new Intent(ResetPassword.this, LogginActivity.class);
            startActivity(intentLogin);
            finish();
        });
    }
}