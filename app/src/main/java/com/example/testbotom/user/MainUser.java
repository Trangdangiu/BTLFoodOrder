package com.example.testbotom.user;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testbotom.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainUser extends AppCompatActivity {

//    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_user);
//        frameLayout=findViewById(R.id.fram_layout);
         bottomNavigationView=findViewById(R.id.bottomNavigationView);
        //deafault fragment
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout,new HomeFragment()).commit();

        }

         bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // getting  Menuitem id in a variable
                  int id=item.getItemId();
                  // if else if statement for fragment;
                 if(id==R.id.btn_home){
                     getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout,new HomeFragment()).commit();
                     
                 } else if (id==R.id.btn_profile) {
                     getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout,new ProfileFragment()).commit();

                     
                 } else if (id==R.id.btn_cart) {
                     getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout,new CartFragment()).commit();

                 } else if (id==R.id.btn_contact) {
                     getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout,new ContactFragment()).commit();
                     
                 } else if (id==R.id.btn_feed_back) {
                     getSupportFragmentManager().beginTransaction().replace(R.id.fram_layout,new FeedBackFragment()).commit();
                 }


                 return true;
             }
         });
    }

}
