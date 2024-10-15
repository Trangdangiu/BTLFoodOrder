package com.example.testbotom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.testbotom.Database.CartItem;
import com.example.testbotom.R;

import java.util.List;

public class Detail_Cart_Food_Item extends ArrayAdapter<CartItem> {
    private Context context;
    private List<CartItem> itemList;

    public Detail_Cart_Food_Item(Context context, List<CartItem> items) {
        super(context, 0, items);
        this.context = context;
        this.itemList = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Tạo view nếu chưa có
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_detail_food_oder, parent, false);
        }

        // Lấy dữ liệu từ vị trí hiện tại
        CartItem currentItem = getItem(position); // Sử dụng phương thức getItem

        // Tham chiếu đến các View trong layout
        TextView tvName = convertView.findViewById(R.id.tv_item_name);
        TextView tvPrice = convertView.findViewById(R.id.tv_item_price);
        TextView tvQuantity = convertView.findViewById(R.id.tv_item_quantity);

        // Thiết lập dữ liệu cho các View
        if (currentItem != null) {
            tvName.setText(currentItem.getFoodName());
            tvPrice.setText(String.format("Giá: %.2f VNĐ", currentItem.getFoodPrice()));
            tvQuantity.setText("Số lượng: " + currentItem.getQuantity());
        }

        return convertView;
    }

}
