package com.example.testbotom.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testbotom.Database.Create_database;
import com.example.testbotom.LoginAndRegister.LogginActivity;
import com.example.testbotom.R;


public class ProfileAdminFragment extends Fragment {
    TextView txt_revenue,txt_change_pass,txt_logout;

    private Create_database database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_admin, container, false);
        txt_revenue=view.findViewById(R.id.txt_revenue);
        txt_change_pass=view.findViewById(R.id.txt_changepass_admin);
        txt_logout=view.findViewById(R.id.txt_logout_admin);
        database = new Create_database(getContext());

        txt_logout.setOnClickListener(view1 -> logout());

        txt_change_pass.setOnClickListener(view2 -> ShowChangePasswordDialog());

        return view;
    }

    private void ShowChangePasswordDialog(){

        LayoutInflater inflater=LayoutInflater.from(getContext());
        View dialogview=inflater.inflate(R.layout.dialog_change_pass,null);


        EditText editOldpass=dialogview.findViewById(R.id.editTextOldPassword);
        EditText editnewPass= dialogview.findViewById(R.id.editTextNewPassword);
        Button btn_change=dialogview.findViewById(R.id.buttonChangePassword);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Đổi mật khẩu")
                .setView(dialogview)
                .setCancelable(true)
                .create();


        btn_change.setOnClickListener(view -> {
            String oldPassword=editOldpass.getText().toString().trim();
            String newPassword=editnewPass.getText().toString().trim();


            if(oldPassword.isEmpty()||newPassword.isEmpty()){
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if(changePassword(oldPassword,newPassword)){
                Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                return;

            }else {
                Toast.makeText(getContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();

    }


    private boolean changePassword(String oldPassword, String newPassword) {
        // Kiểm tra mật khẩu cũ có đúng không
//        String currentUsername = getCurrentUsername();
        String currentRole = database.loginUser("current_username", oldPassword); // Giả định bạn có username hiện tại
        if (currentRole != null) {
            // Cập nhật mật khẩu mới vào cơ sở dữ liệu
            // Cần thêm hàm để cập nhật mật khẩu trong Create_database
            return database.updatePassword("current_username", newPassword); // Cập nhật mật khẩu mới
        }
        return false; // Nếu mật khẩu cũ không đúng
    }






    private  void logout(){

        Intent intent = new Intent(getContext(), LogginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Xóa stack các activity cũ
        startActivity(intent);
        getActivity().finish(); // Kết thúc Activity hiện tại
    }
}