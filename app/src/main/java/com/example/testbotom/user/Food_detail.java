package com.example.testbotom.user;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.testbotom.Database.CartItem;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Food_detail extends AppCompatActivity {
    private TextView foodName, foodPrice, foodDescription;
    private ImageView foodImage, image_ic_back;
    private Button btn_add_cart;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // Liên kết các view
        btn_add_cart = findViewById(R.id.btn_cart);
        foodName = findViewById(R.id.product_name);
        foodPrice = findViewById(R.id.product_price);
        foodImage = findViewById(R.id.product_image);
        foodDescription = findViewById(R.id.product_description);
        image_ic_back = findViewById(R.id.image_ic_back);
        image_ic_back.setOnClickListener(v -> {
            finish();
        });

        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getIntent().getStringExtra("foodName");
                int price = getIntent().getIntExtra("foodPrice", 0);
                String imageUri = getIntent().getStringExtra("foodImage"); // Nhận URI ảnh dưới dạng String
                String description = getIntent().getStringExtra("foodDescription");
            showAddToCartDialog(name,price,imageUri);
            }
        });


        // Lấy dữ liệu từ Intent
//        String name = getIntent().getStringExtra("foodName");
//        int price = getIntent().getIntExtra("foodPrice", 0);
//        String imageUri = getIntent().getStringExtra("foodImage"); // Nhận URI ảnh dưới dạng String
//        String description = getIntent().getStringExtra("foodDescription");
//
//        // Set dữ liệu vào view
//        foodName.setText(name);
//        foodPrice.setText("Giá:  " + String.valueOf(price) + "VNĐ");
//        foodDescription.setText(description);
//
//        // Load ảnh từ URI bằng Glide
//        Glide.with(this)
//                .load(Uri.parse(imageUri)) // Chuyển đổi lại từ String sang Uri
//                .placeholder(R.drawable.anh1)
//                .error(R.drawable.ic_home)
//                .into(foodImage);
        loaddataFood();
    }


    private void showAddToCartDialog(String name, int price,String image) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_to_cart, null);
        dialog.setContentView(dialogView);
        TextView txt_name= dialogView.findViewById(R.id.txt_tenmon);
        TextView txt_price=dialogView.findViewById(R.id.txt_gia);
        ImageView img_input=dialogView.findViewById(R.id.img_input);
        EditText quantityInput = dialogView.findViewById(R.id.quantity_input); // soluong
        Button addButton = dialogView.findViewById(R.id.add_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        // hieenj ảnh
        txt_name.setText(name);
        txt_price.setText(price+" VNĐ");
        Glide.with(this)
                .load(Uri.parse(image)) // Chuyển đổi lại từ String sang Uri
                .placeholder(R.drawable.anh1)
                .error(R.drawable.ic_home)
                .into(img_input);
        addButton.setOnClickListener(v -> {
            String quantityText = quantityInput.getText().toString();
            if (!quantityText.isEmpty()) {
                int quantity = Integer.parseInt(quantityText);
                // Thêm logic để thêm sản phẩm vào giỏ hàng
                addToCart(name, price, quantity,image);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void addToCart(String name, double price, int quantity, String image) {
        int cartId = 1; // Thay đổi giá trị này theo giỏ hàng hiện tại
        int foodId = getIntent().getIntExtra("foodId", 0);

        CartItem cartItem = new CartItem(0, cartId, foodId, name, price, image, quantity);

        Create_database db = new Create_database(this);
        long result = db.addCartItem(cartItem); // Giả sử phương thức trả về ID của bản ghi mới

        if (result != -1) {
            Toast.makeText(this, "Đã thêm " + quantity + " " + name + " vào giỏ hàng.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Thêm vào giỏ hàng thất bại.", Toast.LENGTH_SHORT).show();
        }
    }




    private void loaddataFood() {
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
