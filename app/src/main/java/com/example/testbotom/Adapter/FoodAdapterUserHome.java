package com.example.testbotom.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testbotom.Database.Food;
import com.example.testbotom.R;
import com.example.testbotom.user.Food_detail;

import java.util.List;

public class FoodAdapterUserHome extends RecyclerView.Adapter<FoodAdapterUserHome.FoodViewHolder> {

    private List<Food> arrayList; // Danh sách các món ăn
    private final Context context; // Ngữ cảnh để sử dụng trong Adapter
    private List<Food> foodList;

    // Constructor của Adapter
    public FoodAdapterUserHome(List<Food> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item
        View view = LayoutInflater.from(context).inflate(R.layout.layout_items_food, parent, false);
        return new FoodViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        // Lấy đối tượng food ở vị trí hiện tại
        Food food = arrayList.get(position);

        // Hiển thị tên và giá
        holder.txt_name.setText(food.getName());
        holder.txt_price.setText("Giá: " + food.getPrice() + " VND");

        // In ra đường dẫn URL của ảnh để debug
        Log.d("Image URL", food.getImage());

        // Sử dụng Glide để tải ảnh từ URI (content://)
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(food.getImage())) // Chuyển đổi từ String thành URI
                .placeholder(R.drawable.anh1) // Ảnh hiển thị trong khi đang tải
                .error(R.drawable.ic_home) // Ảnh hiển thị nếu có lỗi khi tải
                .into(holder.imgfood);

        ////// sự kiện click item hiện thông tin chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Food_detail.class);

            // Truyền dữ liệu qua Intent
            intent.putExtra("foodName", food.getName());
            intent.putExtra("foodPrice", food.getPrice());
            intent.putExtra("foodImage", food.getImage());
            intent.putExtra("foodDescription", food.getDescription());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size(); // Trả về số lượng phần tử trong danh sách
    }

    // ViewHolder để giữ các thành phần của layout item
    public static class FoodViewHolder extends RecyclerView.ViewHolder{
        ImageView imgfood;
        TextView txt_name, txt_price;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgfood = itemView.findViewById(R.id.image_food); // ImageView để hiển thị ảnh món ăn
            txt_name = itemView.findViewById(R.id.txt_tensp); // TextView để hiển thị tên món ăn
            txt_price = itemView.findViewById(R.id.txt_giasp); // TextView để hiển thị giá món ăn
        }
    }

    public void updateFoodList(List<Food> newFoodList) {
        arrayList = newFoodList;
        notifyDataSetChanged(); // Thông báo RecyclerView cập nhật lại giao diện
    }

}
