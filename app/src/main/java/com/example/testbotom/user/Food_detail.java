package com.example.testbotom.user;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.testbotom.R;

public class Food_detail extends AppCompatActivity {
    private TextView foodName, foodPrice, foodDescription;
    private ImageView foodImage, image_ic_back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // Liên kết các view
        foodName = findViewById(R.id.product_name);
        foodPrice = findViewById(R.id.product_price);
        foodImage = findViewById(R.id.product_image);
        foodDescription = findViewById(R.id.product_description);
        image_ic_back = findViewById(R.id.image_ic_back);
        image_ic_back.setOnClickListener(v -> {
            finish();
        });


        // Lấy dữ liệu từ Intent
        String name = getIntent().getStringExtra("foodName");
        int price = getIntent().getIntExtra("foodPrice", 0);
        String imageUri = getIntent().getStringExtra("foodImage"); // Nhận URI ảnh dưới dạng String
        String description = getIntent().getStringExtra("foodDescription");

        // Set dữ liệu vào view
        foodName.setText(name);
        foodPrice.setText("Giá:  " + String.valueOf(price) + "VNĐ");
        foodDescription.setText(description);

        // Load ảnh từ URI bằng Glide
        Glide.with(this)
                .load(Uri.parse(imageUri)) // Chuyển đổi lại từ String sang Uri
                .placeholder(R.drawable.anh1)
                .error(R.drawable.ic_home)
                .into(foodImage);
    }
}
