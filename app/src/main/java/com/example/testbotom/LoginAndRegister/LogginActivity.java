package com.example.testbotom.LoginAndRegister;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testbotom.Database.Create_database;
import com.example.testbotom.R;
import com.example.testbotom.admin.HomeAdminFragment;
import com.example.testbotom.admin.MainAdmin;
import com.example.testbotom.user.HomeFragment;
import com.example.testbotom.user.MainUser;

public class LogginActivity extends AppCompatActivity {
    // database
    private Create_database dbHelper;
    private EditText editTextEmail, editTextPassword;
    private Button btn_loggin;
    private TextView txt_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
        dbHelper = new Create_database(this);
        editTextEmail = findViewById(R.id.enter_email);
        editTextPassword = findViewById(R.id.enter_password);
        btn_loggin = findViewById(R.id.btn_loggin);
        txt_register=findViewById(R.id.id_dangki);

        txt_register.setOnClickListener(view -> {
            Intent intent = new Intent(LogginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
            btn_loggin.setOnClickListener(view -> {
                String my_email=editTextEmail.getText().toString();
                String my_password=editTextPassword.getText().toString();
                String role= String.valueOf(dbHelper.loginUser(my_email,my_password));
                if(role!=null){
//                    Toast.makeText(LogginActivity.this, "Đăng Nhập thành công với vai trò :"+role, Toast.LENGTH_SHORT).show();
                    if(role.equals("user")){
                        Intent intent = new Intent(LogginActivity.this,MainUser.class);
                        startActivity(intent);
                    } else if (role.equals("admin")) {
                        Intent intent = new Intent(LogginActivity.this,MainAdmin.class);
                        startActivity(intent);
                    }

                    // Lưu email vào SharedPreferences khi người dùng đăng nhập thành công
                    SharedPreferences sharedPreferences = this.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_email", my_email); // 'email' là biến chứa địa chỉ email của người dùng
                    editor.apply();

                }else if(role==null) {
                    Toast.makeText(LogginActivity.this, "Tên người dùng hoặc mật khẩu không đúng !", Toast.LENGTH_SHORT).show();
                }

            });



    }



}