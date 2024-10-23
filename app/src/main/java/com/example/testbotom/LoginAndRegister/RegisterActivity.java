package com.example.testbotom.LoginAndRegister;

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

public class RegisterActivity extends AppCompatActivity {
    private Create_database dbHelper;
    private EditText editTextEmail, editTextPassword;
    private RadioGroup radioGroupRole;
    private ImageView img_forgot_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        dbHelper = new Create_database(this);
        editTextEmail = findViewById(R.id.id_edit_emaildki);
        editTextPassword = findViewById(R.id.id_edit_passdki);
        radioGroupRole = findViewById(R.id.id_role);
        Button buttonRegister = findViewById(R.id.id_btn_dki);
        buttonRegister.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();


            // Kiểm tra người dùng đã chọn vai trò
            int selectedId = radioGroupRole.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(RegisterActivity.this, "Vui lòng chọn vai trò", Toast.LENGTH_SHORT).show();
                return;
            }


            String role = ((RadioButton) findViewById(selectedId)).getText().toString();

            if (dbHelper.registerUser(email, password, role)) {
                Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(RegisterActivity.this, LogginActivity.class);
                startActivity(intent1);
                finish(); // Đóng Activity đăng ký
            } else {
                Log.e("RegisterError", "Đăng ký thất bại cho username: " + email);
                Toast.makeText(RegisterActivity.this, "Đăng Kí Thất Bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

}