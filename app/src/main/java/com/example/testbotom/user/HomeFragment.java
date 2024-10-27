package com.example.testbotom.user;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.testbotom.Adapter.FoodAdapterUserHome;
import com.example.testbotom.Adapter.ImageSlideAdapter;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.Database.Food;
import com.example.testbotom.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPager;
    private List<Food> foodlist;
    private List<Food> filteredFoodList; // Danh sách món ăn sau khi tìm kiếm
    private Create_database database;
    private FoodAdapterUserHome foodAdapterUserHome;
    private RecyclerView recyclerView;
    private AutoCompleteTextView autoCompleteTextView;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFood);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        database = new Create_database(getContext());
        foodlist = new ArrayList<>();
        filteredFoodList = new ArrayList<>();
        loadFoodData(); // Lấy dữ liệu món ăn từ cơ sở dữ liệu

        // Slide image
        viewPager = view.findViewById(R.id.image_viewpager);
        setupViewPager();

        // Tìm kiếm món ăn
        autoCompleteTextView = view.findViewById(R.id.search_items);
        ArrayList<String> foodNames = loadFoodNames(); // Lấy danh sách tên món ăn từ database

        // Tạo ArrayAdapter cho AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, foodNames);
        autoCompleteTextView.setAdapter(adapter);

        // Thêm TextWatcher để lắng nghe khi người dùng nhập từ khóa
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý trước khi thay đổi văn bản
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Gọi hàm lọc món ăn khi người dùng nhập ký tự mới
                filterFoods(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý sau khi thay đổi văn bản
            }
        });

        // Gắn Adapter cho RecyclerView
        foodAdapterUserHome = new FoodAdapterUserHome(filteredFoodList, getContext());
        recyclerView.setAdapter(foodAdapterUserHome);
        return view;
    }

    // Thiết lập slide image
    private void setupViewPager() {
        int[] imageResources = {
                R.drawable.slide1,
                R.drawable.slide4,
                R.drawable.slide2
        };

        ImageSlideAdapter adapter = new ImageSlideAdapter(getContext(), imageResources);
        viewPager.setAdapter(adapter);
    }




    // Lấy dữ liệu món ăn từ CSDL
    private void loadFoodData() {
        Cursor cursor = null;
        try {
            cursor = database.getAllFoods(); // Lấy Cursor chứa dữ liệu món ăn
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") int price = cursor.getInt(cursor.getColumnIndex("price"));
                    @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex("image"));
                    @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));

                    // Thêm vào danh sách món ăn
                    foodlist.add(new Food(id, name, price, image, description));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Khởi tạo danh sách lọc ban đầu
        filteredFoodList.addAll(foodlist);
    }

    // Lấy danh sách tên món ăn để dùng trong AutoCompleteTextView
    private ArrayList<String> loadFoodNames() {
        ArrayList<String> foodNames = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.getAllFoods(); // Lấy Cursor chứa dữ liệu món ăn
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                    foodNames.add(name); // Thêm tên món ăn vào danh sách
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return foodNames;
    }

    // Lọc danh sách món ăn theo từ khóa tìm kiếm
    private void filterFoods(String query) {
        filteredFoodList.clear(); // Xóa danh sách lọc cũ

        for (Food food : foodlist) {
            if (food.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredFoodList.add(food);
            }

        }

        // Cập nhật lại RecyclerView sau khi lọc
        foodAdapterUserHome.updateFoodList(filteredFoodList);
    }
}
