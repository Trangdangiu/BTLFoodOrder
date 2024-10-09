package com.example.testbotom.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.testbotom.Adapter.CartItemAdapter;
import com.example.testbotom.Database.CartItem;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.R;

import java.util.List;

public class CartFragment extends Fragment {

    private ListView listView;
    private CartItemAdapter adapter;
    private Create_database db;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        // Khởi tạo ListView
        listView = view.findViewById(R.id.lv_cart);
        db = new Create_database(getActivity()); // Sử dụng getActivity() để khởi tạo database

        // Lấy danh sách các mục trong giỏ hàng (cartId cần thay đổi cho phù hợp)
        int cartId = 1; // Thay đổi cartId theo nhu cầu của bạn
        List<CartItem> cartItems = db.getAllCartItems(cartId); // Lấy danh sách CartItem từ database

        // Tạo adapter và thiết lập cho ListView
        adapter = new CartItemAdapter(getContext(), cartItems);
        listView.setAdapter(adapter);

        return view;
    }
}
