package com.example.testbotom.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.testbotom.Database.User;
import com.example.testbotom.LoginAndRegister.LogginActivity;
import com.example.testbotom.R;


public class ProfileAdminFragment extends Fragment {
    TextView txt_revenue,txt_change_pass,txt_logout,txt_profile_show;

    private Create_database database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_admin, container, false);
        txt_revenue=view.findViewById(R.id.txt_revenue);
        txt_change_pass=view.findViewById(R.id.txt_changepass_admin);
        txt_logout=view.findViewById(R.id.txt_logout_admin);
        txt_profile_show=view.findViewById(R.id.txt_admin_show);
        database = new Create_database(getContext());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentEmail = sharedPreferences.getString("user_email", null);
        txt_profile_show.setText(currentEmail);
        txt_revenue.setOnClickListener(v -> {
            showRevenue();
        });
        txt_logout.setOnClickListener(view1 -> logout());

        txt_change_pass.setOnClickListener(view2 -> ShowChangePasswordDialog());

        return view;
    }


    private void showRevenue(){
        Intent intent = new Intent(getContext(), Revenue.class);
        startActivity(intent);
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


            if(oldPassword.isEmpty()||newPassword.isEmpty()){Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
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
        boolean checkChange = false;
        // Lấy email người dùng hiện tại từ SharedPreferences lưu trũ ở login
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentEmail = sharedPreferences.getString("user_email", "Không tìm thấy email");
        User userExist = database.getUserByEmail(currentEmail);
        if(oldPassword.equals(userExist.getPassword())){
            // update pass word
            database.updatePassword(currentEmail, newPassword);
            checkChange = true;
        } else {
            Toast.makeText(getContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
        }

        return checkChange; // Nếu mật khẩu cũ không đúng
    }






    private  void logout(){

        Intent intent = new Intent(getContext(), LogginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Xóa stack các activity cũ
        startActivity(intent);
        getActivity().finish(); // Kết thúc Activity hiện tại
    }
}