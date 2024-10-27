package com.example.testbotom.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testbotom.Adapter.FoodAdapterHomeAdmin;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.Database.Food;
import com.example.testbotom.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeAdminFragment extends Fragment implements FoodAdapterHomeAdmin.OnFoodListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private FoodAdapterHomeAdmin foodAdapterHomeAdmin;
    private List<Food> foodList;
    private Create_database database;

    // Các biến cho ImageView trong dialog
    private ImageView editImageView; // ImageView trong dialog "sửa món ăn"
    private ImageView imageView; // ImageView trong dialog "thêm món ăn"

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);
        database = new Create_database(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodList = new ArrayList<>();
        loadFoodData();
        FloatingActionButton btnadd_data = view.findViewById(R.id.fab_add_food);
        btnadd_data.setOnClickListener(view1 -> showAddFoodDialog());

        foodAdapterHomeAdmin = new FoodAdapterHomeAdmin(foodList, this);
        recyclerView.setAdapter(foodAdapterHomeAdmin);
        return view;
    }

    private void loadFoodData() {
        Cursor cursor = null;
        try {
            cursor = database.getAllFoods();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") int price = cursor.getInt(cursor.getColumnIndex("price"));
                    @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex("image"));
                    @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));

                    foodList.add(new Food(id, name, price, image, description));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onEditClick(Food food) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sửa thông tin món ăn");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_food, null);
        builder.setView(dialogView);

        EditText editFoodName = dialogView.findViewById(R.id.edit_food_name);
        EditText editFoodPrice = dialogView.findViewById(R.id.edit_food_price);
        EditText editFoodDescription = dialogView.findViewById(R.id.edit_food_description);
        editImageView = dialogView.findViewById(R.id.edit_food_image);
        ImageView editImageButton = dialogView.findViewById(R.id.click_folder_image);

        editFoodName.setText(food.getName());
        editFoodPrice.setText(String.valueOf(food.getPrice()));
        editFoodDescription.setText(food.getDescription());
        selectedImageUri = Uri.parse(food.getImage());
        Glide.with(this).load(selectedImageUri).into(editImageView);

        editImageButton.setOnClickListener(v -> openGallery());

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String name = editFoodName.getText().toString();
            int price = Integer.parseInt(editFoodPrice.getText().toString());
            String description = editFoodDescription.getText().toString();
            updateFood(food, name, price, description, selectedImageUri);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateFood(Food food, String name, int price, String description, Uri imageUri) {
        String img = (imageUri != null) ? imageUri.toString() : food.getImage();
        database.updateFood(food.getId(), name, price, description, img);

        food.setName(name);
        food.setPrice(price);
        food.setDescription(description);
        food.setImage(img);

        foodAdapterHomeAdmin.notifyItemChanged(foodList.indexOf(food));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteClick(Food food) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa món ăn")
                .setMessage("Bạn có chắc chắn muốn xóa món ăn " + food.getName() + "?")
                .setPositiveButton("Có", (dialog, which) -> deleteFood(food))
                .setNegativeButton("Không", null)
                .show();
    }

    private void deleteFood(Food food) {
        database.deleteFood(food.getId());
        foodList.remove(food);
        foodAdapterHomeAdmin.notifyDataSetChanged();
    }

    private void showAddFoodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_food, null);
        EditText etName = dialogView.findViewById(R.id.et_food_name);
        EditText etPrice = dialogView.findViewById(R.id.et_food_price);
        EditText etDescription = dialogView.findViewById(R.id.et_description);

        ImageView imageBtnFolder = dialogView.findViewById(R.id.image_folder);
        imageView = dialogView.findViewById(R.id.image_view);
        Button btnAdd = dialogView.findViewById(R.id.btn_add);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        imageBtnFolder.setOnClickListener(v -> openGallery());

        btnAdd.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int price = Integer.parseInt(priceStr);
            String description = etDescription.getText().toString().trim();
            int id = database.getNextFoodId();
            String img = selectedImageUri != null ? selectedImageUri.toString() : "";
            Food newFood = new Food(id, name, price, img, description);

            database.insertFood(newFood);
            foodList.add(newFood);

            foodAdapterHomeAdmin.notifyDataSetChanged();
            dialog.dismiss();
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);

                    if (editImageView != null) {
                        editImageView.setImageBitmap(bitmap);
                    } else if (imageView != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

