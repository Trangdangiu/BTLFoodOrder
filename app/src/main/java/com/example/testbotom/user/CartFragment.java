package com.example.testbotom.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.testbotom.Adapter.CartItemAdapter;
import com.example.testbotom.Adapter.Detail_Cart_Food_Item;
import com.example.testbotom.Database.CartItem;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.Database.OrderItem;
import com.example.testbotom.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CartFragment extends Fragment {
    private List<CartItem> cartItems;
    private ListView listView;
    private CartItemAdapter adapter;
    private Create_database db;
    private TextView tvTotalItems;
    private Button btn_pay_food;
    private Detail_Cart_Food_Item adapter_detail;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Khởi tạo ListView và TextView
        listView = view.findViewById(R.id.lv_cart);
        tvTotalItems = view.findViewById(R.id.txt_total_price); // Tham chiếu đến TextView hiển thị tổng số món
        btn_pay_food = view.findViewById(R.id.button_payment_food);
        db = new Create_database(getActivity());

        // Lấy danh sách các mục trong giỏ hàng
        int cartId = 1; // Có thể thay đổi ID giỏ hàng theo nhu cầu
        cartItems = db.getAllCartItems(cartId); // Sử dụng biến cartItems đã khai báo

        // Tạo adapter
        adapter = new CartItemAdapter(getContext(), cartItems);
        listView.setAdapter(adapter);

        // Hiển thị tổng số món ban đầu
        int totalItems = adapter.getTotalItems();
        tvTotalItems.setText("Tổng số món: " + totalItems);

        // Lắng nghe khi tổng số món thay đổi
        adapter.setOnTotalItemsChangeListener(totalItems1 ->
                tvTotalItems.setText("Tổng số món: " + totalItems1)
        );

        btn_pay_food.setOnClickListener(v -> {
            showBottomSheetDialog(totalItems);
        });

        return view;
    }

    // show_dialog
    private void showBottomSheetDialog(int total_item) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.item_input_order_food, null);

        // Cài đặt Spinner cho phương thức thanh toán
        String[] paymentMethods = {"Tiền mặt", "Thẻ tín dụng", "MoMo"};
        Spinner spinner_payment = bottomSheetView.findViewById(R.id.spiner_payment_order);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_payment.setAdapter(adapter);

        // Lấy thông tin từ người dùng
        EditText editTextFullName = bottomSheetView.findViewById(R.id.enter_fulname_order);
        EditText editTextPhoneNumber = bottomSheetView.findViewById(R.id.enter_sdt_order);
        EditText editTextAddress = bottomSheetView.findViewById(R.id.enter_adress_order);
        TextView txt_total_price = bottomSheetView.findViewById(R.id.txt_price);
        AppCompatButton buttonSubmit = bottomSheetView.findViewById(R.id.btn_submit_order);

        // Thay ListView bằng TextView
        TextView txt_food_items = bottomSheetView.findViewById(R.id.txt_food_items);
        int cartId = 1; // Thay đổi ID giỏ hàng nếu cần
        List<CartItem> cartItemsDetail = db.getAllCartItems(cartId); // Lấy các món ăn chi tiết cho giỏ hàng

        // Xây dựng thông tin thực đơn từ cartItemsDetail
        StringBuilder menuBuilder = new StringBuilder();
        for (CartItem item : cartItemsDetail) {
            // Giả sử CartItem có các phương thức getFoodName(), getPrice(), và getQuantity()
            menuBuilder.append(item.getFoodName())
                    .append(" - Giá: ")
                    .append(item.getFoodPrice())
                    .append(" - Số lượng: ")
                    .append(item.getQuantity())
                    .append("\n"); // Mỗi món ăn sẽ ở một dòng mới
        }
        String menu = menuBuilder.toString();

        // Hiển thị danh sách món ăn trong TextView
        txt_food_items.setText(menu.trim());

        // Hiển thị tổng giá
        txt_total_price.setText(String.valueOf(total_item));

        // Xử lý sự kiện khi nhấn nút Submit
        buttonSubmit.setOnClickListener(v -> {
            String fullName = editTextFullName.getText().toString().trim();
            String phoneNumber = editTextPhoneNumber.getText().toString().trim();
            String address = editTextAddress.getText().toString().trim();

            // Tạo mã đơn hàng ngẫu nhiên
            String orderCode = "OD" + UUID.randomUUID().toString().substring(0, 8);

            // Lấy ngày đặt hàng hiện tại
            String orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            // Lấy phương thức thanh toán đã chọn
            String paymentMethod = spinner_payment.getSelectedItem().toString();

            // Tạo đối tượng OrderItem
            OrderItem orderItem = new OrderItem(0, orderCode, fullName, phoneNumber, address, menu, orderDate, total_item, paymentMethod);

            // Chèn vào cơ sở dữ liệu
            long result = db.addOrderItem(orderItem);
            if (result != -1) {
                Toast.makeText(getContext(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Đặt hàng không thành công. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }

            bottomSheetDialog.dismiss(); // Đóng Bottom Sheet
        });

        // Thiết lập view cho Bottom Sheet Dialog
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }





}
