package com.example.testbotom.Adapter;

import android.net.Uri;
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

import java.util.List;

public class FoodAdapterHomeAdmin extends RecyclerView.Adapter<FoodAdapterHomeAdmin.FoodViewHolder> {
    private List<Food> foodList;
    private OnFoodListener onFoodListener;

    public FoodAdapterHomeAdmin(List<Food> foodList, OnFoodListener onFoodListener) {
        this.foodList = foodList;
        this.onFoodListener = onFoodListener;
    }

    @NonNull  @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_admin, parent, false);
        return new FoodViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.foodName.setText(food.getName());
        holder.foodPrice.setText("Giá: " + food.getPrice() +""+ " VND");
        holder.foodDescription.setText(food.getDescription());
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(food.getImage())) // Đường dẫn ảnh
                .into(holder.foodImage);

        holder.img_edit.setOnClickListener(v -> onFoodListener.onEditClick(food));
        holder.img_delete.setOnClickListener(v -> onFoodListener.onDeleteClick(food));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodPrice;
       ImageView img_edit,img_delete;
        TextView foodDescription;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodDescription=itemView.findViewById(R.id.txt_description);
            img_edit = itemView.findViewById(R.id.img_edit);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }

    public interface OnFoodListener {
        void onEditClick(Food food);
        void onDeleteClick(Food food);
    }
}
