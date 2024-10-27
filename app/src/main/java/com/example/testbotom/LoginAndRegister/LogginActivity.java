package com.example.testbotom.LoginAndRegister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testbotom.Database.Create_database;
import com.example.testbotom.Database.User;
import com.example.testbotom.R;
import com.example.testbotom.admin.MainAdmin;
import com.example.testbotom.user.MainUser;

public class LogginActivity extends AppCompatActivity {
    // database
    private Create_database dbHelper;
    private EditText editTextEmail, editTextPassword;
    private Button btn_loggin;
    private TextView txt_register,txt_forgot_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);

        // Khởi tạo các thành phần UI
        dbHelper = new Create_database(this);
        editTextEmail = findViewById(R.id.enter_email);
        editTextPassword = findViewById(R.id.enter_password);
        btn_loggin = findViewById(R.id.btn_loggin);
        txt_register = findViewById(R.id.id_dangki);
        txt_forgot_pass=findViewById(R.id.img_forgot_pass);


        // Sự kiện khi nhấn vào nút đăng ký
        txt_register.setOnClickListener(view -> {
            Intent intent = new Intent(LogginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Sự kiện khi nhấn vào nút đăng nhập
        btn_loggin.setOnClickListener(view -> {
            String my_email = editTextEmail.getText().toString();
            String my_password = editTextPassword.getText().toString();

            // check formmat
            if (my_email.isEmpty() || my_password.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(my_email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // if email exist
            if(dbHelper.checkUser(my_email)){
                // check password is correct ?
                if(dbHelper.loginUser(my_email, my_password)){
                    User user = dbHelper.getUserByEmail(my_email);
                    String role = user.getRole();
                    int my_id = user.getId();

                    System.out.println(user);
                    System.out.println("role: "+role);
                    System.out.println("id from email: "+my_id);
                    if (role != null) {

                        // Kiểm tra nếu my_id không phải là null, LOAD CART BY USER ID
                        if (my_id != -1) {
                            Integer cartId = dbHelper.getCartIdByUserId(my_id);

                            // Kiểm tra nếu cartId là null, thì tạo mới giỏ hàng,
                            if (cartId == null) {
                                Toast.makeText(LogginActivity.this, "Không tìm thấy giỏ hàng, tạo giỏ hàng mới!", Toast.LENGTH_SHORT).show();
                                dbHelper.createCart(my_id); // Tạo mới giỏ hàng cho user
                                cartId = dbHelper.getCartIdByUserId(my_id); // Lấy lại cartId mới tạo
                            }

                            // Lưu email, user_id và cartId vào SharedPreferences
                            SharedPreferences sharedPreferences = this.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_email", my_email);
                            editor.putString("user_id", my_id+"");
                            editor.putInt("cartId", cartId); // Lưu cartId
                            editor.apply();
                        }

                        Intent intent;
                        if (role.equals("user")) {
                            intent = new Intent(LogginActivity.this, MainUser.class);
                            Toast.makeText(this, "Đăng Nhập Thành Công Vai Trò User!", Toast.LENGTH_SHORT).show();
                            // intent
                            startActivity(intent);
                        } else if (role.equals("admin")) {
                            intent = new Intent(LogginActivity.this, MainAdmin.class);
                            Toast.makeText(this, "Đăng Nhập Thành Công Vai Trò Admin!", Toast.LENGTH_SHORT).show();
                            // intent
                            startActivity(intent);
                        }
                    }
                } else {
                    // if wrong password
                    Toast.makeText(LogginActivity.this, "Tên người dùng hoặc mật khẩu không đúng !", Toast.LENGTH_SHORT).show();
                }
            } else { // email not exist
                Toast.makeText(LogginActivity.this, "Email chưa được đăng kí, vui lòng nhập lại", Toast.LENGTH_SHORT).show();
            }
        });

        // if forgot password
        txt_forgot_pass.setOnClickListener(view -> {
            Intent intent = new Intent(LogginActivity.this, ForgotPassword.class);
            startActivity(intent);
        });
    }
}