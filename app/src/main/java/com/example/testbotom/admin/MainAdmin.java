package com.example.testbotom.admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.testbotom.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainAdmin extends AppCompatActivity {

    //    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_admin);
//        frameLayout=findViewById(R.id.fram_layout);
        bottomNavigationView_admin=findViewById(R.id.bottomNavigationView_admin);
        //deafault fragment
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout_admin,new HomeAdminFragment()).commit();

        }

        bottomNavigationView_admin.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // getting  Menuitem id in a variable
                int id=item.getItemId();
                // if else if statement for fragment;
                if(id==R.id.btn_home_admin){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout_admin,new HomeAdminFragment()).commit();

                } else if (id==R.id.btn_feedback_admin) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout_admin,new FeedBackAdminFragment()).commit();


                } else if (id==R.id.btn_order_admin) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout_admin,new OrderAdminFragment()).commit();

                } else if (id==R.id.btn_profile_admin) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout_admin,new ProfileAdminFragment()).commit();

                }

                return true;
            }
        });
    }

}
