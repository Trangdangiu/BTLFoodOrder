package com.example.testbotom.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.testbotom.R;


public class CartFragment extends Fragment {
    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_cart, container, false);

        ImageView img_back = view.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });



        return view;




    }




}