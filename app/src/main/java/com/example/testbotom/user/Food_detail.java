package com.example.testbotom.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.testbotom.Database.CartItem;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Food_detail extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String CART_ID_KEY = "cartId";
    private TextView foodName, foodPrice, foodDescription;
    private ImageView foodImage, image_ic_back, img_backto_cart;
    private Button btn_add_cart;

    private int userId; // Đảm bảo userId được định nghĩa
    private Create_database db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

//        img_backto_cart = findViewById(R.id.img_cart);
        btn_add_cart = findViewById(R.id.btn_cart);
        foodName = findViewById(R.id.product_name);
        foodPrice = findViewById(R.id.product_price);
        foodImage = findViewById(R.id.product_image);
        foodDescription = findViewById(R.id.product_description);
        image_ic_back = findViewById(R.id.image_ic_back);
        db = new Create_database(this);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userIdString = sharedPreferences.getString("user_id", null); // Lấy userId từ Intent
        // check userid
        if (userIdString == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy userId.", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity nếu không tìm thấy userId
            return;
        }else{
            userId=Integer.parseInt(userIdString);
        }
//        img_backto_cart.setOnClickListener(v -> {
//            Fragment fragment= new CartFragment();
//            FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container,fragment);
////                transaction.addToBackStack(null);
//            transaction.commit();
//        });

        image_ic_back.setOnClickListener(v -> {
            finish();
        });
        btn_add_cart.setOnClickListener(v -> {
            String name = getIntent().getStringExtra("foodName");
            int price = getIntent().getIntExtra("foodPrice", 0);
            String imageUri = getIntent().getStringExtra("foodImage");
            String description = getIntent().getStringExtra("foodDescription");
            showAddToCartDialog(name, price, imageUri);
        });

        loaddataFood();
    }

    private void showAddToCartDialog(String name, int price, String image) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_to_cart, null);
        dialog.setContentView(dialogView);
        TextView txt_name = dialogView.findViewById(R.id.txt_tenmon);
        TextView txt_price = dialogView.findViewById(R.id.txt_gia);
        ImageView img_input = dialogView.findViewById(R.id.img_input);
        EditText quantityInput = dialogView.findViewById(R.id.quantity_input);
        Button addButton = dialogView.findViewById(R.id.add_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        txt_name.setText(name);
        txt_price.setText(price + " VNĐ");
        Glide.with(this)
                .load(Uri.parse(image))
                .placeholder(R.drawable.anh1)
                .error(R.drawable.ic_home)
                .into(img_input);
        addButton.setOnClickListener(v -> {
            String quantityText = quantityInput.getText().toString();
            if (!quantityText.isEmpty()) {
                int quantity = Integer.parseInt(quantityText);
                addToCart(name, price, quantity, image);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void addToCart(String name, double price, int quantity, String image) {
        int cartId = getOrCreateUserCartId(userId); // Lấy ID giỏ hàng
        int foodId = getIntent().getIntExtra("foodId", 0);
        CartItem cartItem = new CartItem(0, cartId, foodId, name, price, image, quantity);

        long result = db.addCartItem(cartItem); // Giả sử phương thức trả về ID của bản ghi mới

        if (result != -1) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("cartId", cartId); // Lưu cartId
            editor.apply();
            Toast.makeText(this, "Đã thêm " + quantity + " " + name + " vào giỏ hàng.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Thêm vào giỏ hàng thất bại.", Toast.LENGTH_SHORT).show();
        }
    }

    private int getOrCreateUserCartId(int userId) {
        int cartId = db.getCartIdByUserId(userId); // Kiểm tra giỏ hàng

        if (cartId == -1) {
            // Nếu không tìm thấy giỏ hàng, tạo giỏ hàng mới
            cartId = (int) db.createCart(userId);
        }

        return cartId;
    }




    private void loaddataFood() {
        String name = getIntent().getStringExtra("foodName");
        int price = getIntent().getIntExtra("foodPrice", 0);
        String imageUri = getIntent().getStringExtra("foodImage");
        String description = getIntent().getStringExtra("foodDescription");

        // Set dữ liệu vào view
        foodName.setText(name);
        foodPrice.setText("Giá:  " + price + " VNĐ");
        foodDescription.setText(description);

        // Load ảnh từ URI bằng Glide
        Glide.with(this)
                .load(Uri.parse(imageUri))
                .placeholder(R.drawable.anh1)
                .error(R.drawable.ic_home)
                .into(foodImage);
    }
}
