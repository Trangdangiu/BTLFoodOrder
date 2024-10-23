package com.example.testbotom.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.testbotom.R;

public class ContactFragment extends Fragment {
    private String infogmail="Thanhnamnb1004@gmail.com";
    private String infoMessage = "Số Zalo: 0858369925";
    private ImageView imageView, image_gmail, image_youtube, image_zalo;
    private String facebookLink = "https://www.facebook.com/YOUR_IMAGE_LINK"; // Thay thế bằng liên kết đến hình ảnh
    private String emailLink = "https://mail.google.com/mail/mu/mp/152/#tl/priority/%5Esmartlabel_personal";
    private String youtubelink = "https://www.youtube.com/playlist?list=PL6aoXCbsHwIayYCo9aDuzZ3dMC9oShs1u";
    private String zalolink = "https://mail.google.com/mail/mu/mp/152/#tl/priority/%5Esmartlabel_personal";

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        imageView = view.findViewById(R.id.img_facebook);
        image_gmail = view.findViewById(R.id.img_gmail);
        image_youtube = view.findViewById(R.id.image_youtube);
        image_zalo = view.findViewById(R.id.image_zalo);
        // Sự kiện nhấp vào hình ảnh
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebookLink();
            }
        });

        image_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmail();
            }
        });

        image_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openyoutube();
            }
        });

        image_zalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });

        return view;
    }

    // Mở liên kết đến Facebook
    private void openFacebookLink() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookLink));
        startActivity(browserIntent);
    }



    private void openyoutube() {
        Intent youtubeitent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubelink));
        startActivity(youtubeitent);
    }
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//    private void showInfoDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Thông tin");
//
//        // Nếu bạn muốn sử dụng layout tùy chỉnh
//        LayoutInflater inflater = requireActivity().getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_info_zalo_contact, null);
//        TextView dialogTextView = dialogView.findViewById(R.id.dialogTextView);
//        dialogTextView.setText(infoMessage); // Đặt văn bản vào TextView trong dialog
//        builder.setView(dialogView);
//
//        // Nút đóng dialog
//            builder.setPositiveButton("Đóng", (dialog, which) -> {dialog.dismiss();});
//
//        // Tạo và hiển thị dialog
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
    private void openEmail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông tin gmail"); // Tiêu đề của dialog
        builder.setMessage(infogmail); // Nội dung của dialog

        // Nút đóng dialog
        builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());

        // Tạo và hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Zalo"); // Tiêu đề của dialog
        builder.setMessage(infoMessage); // Nội dung của dialog

        // Nút đóng dialog
        builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());

        // Tạo và hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
