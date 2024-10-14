package com.example.testbotom.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.testbotom.Database.CartItem;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.R;

import java.util.List;

public class CartItemAdapter extends BaseAdapter {
    private Context context;
    private List<CartItem> cartItems;
    private Create_database db;
    private OnTotalItemsChangeListener totalItemsChangeListener;

    public CartItemAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
        this.db = new Create_database(context);
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
        ImageView ivFoodImage = convertView.findViewById(R.id.cart_image);
        ImageView imgIncrease = convertView.findViewById(R.id.img_decrease);
        ImageView imgDecrease = convertView.findViewById(R.id.img_increase);
        Button btnDelete = convertView.findViewById(R.id.btn_delete_item);

        // Set dữ liệu
        if (cartItem != null) {
            tvFoodName.setText(cartItem.getFoodName() != null ? cartItem.getFoodName() : "N/A");
            tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
            tvPrice.setText("Price: " + cartItem.getFoodPrice() + " VNĐ");

            Glide.with(context)
                    .load(cartItem.getFoodImage())
                    .placeholder(R.drawable.anh1)
                    .error(R.drawable.ic_cancel)
                    .into(ivFoodImage);

            // Tăng số lượng
            // Tăng số lượng
            imgDecrease.setOnClickListener(v -> {
                int currentQuantity = cartItem.getQuantity();
                cartItem.setQuantity(currentQuantity + 1);
                tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

                // Lưu thay đổi vào database
                db.updateCartItemQuantity(cartItem.getCartItemId(), cartItem.getQuantity());

                notifyDataSetChanged();

                if (totalItemsChangeListener != null) {
                    totalItemsChangeListener.onTotalItemsChange(getTotalItems());
                }
            });

// Giảm số lượng
            imgIncrease.setOnClickListener(v -> {
                int currentQuantity = cartItem.getQuantity();
                if (currentQuantity > 1) {
                    cartItem.setQuantity(currentQuantity - 1);
                    tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

                    // Lưu thay đổi vào database
                    db.updateCartItemQuantity(cartItem.getCartItemId(), cartItem.getQuantity());

                    notifyDataSetChanged();

                    if (totalItemsChangeListener != null) {
                        totalItemsChangeListener.onTotalItemsChange(getTotalItems());
                    }
                }
            });


            // Xóa món
            btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa món này không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            db.deleteCartItem(cartItem.getCartItemId());
                            cartItems.remove(position);
                            notifyDataSetChanged();

                            if (totalItemsChangeListener != null) {
                                totalItemsChangeListener.onTotalItemsChange(getTotalItems());
                            }
                        })
                        .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                        .show();
            });
        }

        return convertView;
    }

    // Hàm tính tổng số món
    public int getTotalItems() {
        int totalItems = 0;
        for (CartItem item : cartItems) {
            totalItems += item.getQuantity()*item.getFoodPrice();
        }
        return totalItems;
    }

    // Interface để lắng nghe khi tổng số món thay đổi
    public void setOnTotalItemsChangeListener(OnTotalItemsChangeListener listener) {
        this.totalItemsChangeListener = listener;
    }


    public interface OnTotalItemsChangeListener {
        void onTotalItemsChange(int totalItems);
    }
}
