package com.example.designseries;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class UserProfile extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.navbar_dashboard, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Dashboard()).commit();
        bottomMenu();
    }

    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                if(i == R.id.navbar_dashboard){
                    fragment = new Dashboard();
                } else if (i == R.id.navbar_profile) {
                    fragment = new Profile();
                } else if (i == R.id.navbar_manage) {
                    fragment = new Manage();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });

    }
}