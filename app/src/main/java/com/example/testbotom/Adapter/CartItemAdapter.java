package com.example.testbotom.Adapter; // CartItemAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide; // Thư viện Glide để tải hình ảnh
import com.example.testbotom.Database.CartItem;
import com.example.testbotom.R;

import java.util.List;

public class CartItemAdapter extends BaseAdapter {
    private Context context;
    private List<CartItem> cartItems;

    public CartItemAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        }

        CartItem cartItem = cartItems.get(position);

        TextView tvFoodName = convertView.findViewById(R.id.cart_foodname);
        TextView tvQuantity = convertView.findViewById(R.id.cartfoodsluong);
        TextView tvPrice = convertView.findViewById(R.id.cart_foodprice);
        ImageView ivFoodImage = convertView.findViewById(R.id.cart_image); // Thêm ImageView
        ImageView img_increase = convertView.findViewById(R.id.img_decrease);
        ImageView img_decrease = convertView.findViewById(R.id.img_increase);
        Button btn_delete=convertView.findViewById(R.id.btn_delete_item);
        // Kiểm tra null cho các giá trị
        if (cartItem != null) {
            tvFoodName.setText(cartItem.getFoodName() != null ? cartItem.getFoodName() : "N/A");
            tvQuantity.setText("" + cartItem.getQuantity());

            // Định dạng giá với đơn vị tiền tệ
//            String formattedPrice = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"))
//                    .format(cartItem.getFoodPrice());
            tvPrice.setText("Price: " + cartItem.getFoodPrice() + " VNĐ");

            // Tải hình ảnh vào ImageView bằng Glide
            Glide.with(context)
                    .load(cartItem.getFoodImage()) // Lấy URL ảnh từ đối tượng CartItem
                    .placeholder(R.drawable.anh1) // Ảnh placeholder
                    .error(R.drawable.ic_cancel) // Ảnh khi có lỗi
                    .into(ivFoodImage);

            img_decrease.setOnClickListener(v -> {
                int currentQuantity = cartItem.getQuantity();
                cartItem.setQuantity(currentQuantity + 1);  // Tăng số lượng
                tvQuantity.setText(String.valueOf(cartItem.getQuantity()));  // Cập nhật hiển thị
                notifyDataSetChanged();  // Cập nhật giao diện
            });


            img_increase.setOnClickListener(v -> {
                int currentQuantity = cartItem.getQuantity();
                if (currentQuantity > 1) {  // Kiểm tra để không giảm dưới 1
                    cartItem.setQuantity(currentQuantity - 1);  // Giảm số lượng
                    tvQuantity.setText(String.valueOf(cartItem.getQuantity()));  // Cập nhật hiển thị
                    notifyDataSetChanged();  // Cập nhật giao diện
                }
            });

            btn_delete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa món này không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            cartItems.remove(position);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                        .show();
            });


        }

        return convertView;
    }
}
