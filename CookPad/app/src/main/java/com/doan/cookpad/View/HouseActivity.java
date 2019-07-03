package com.doan.cookpad.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.doan.cookpad.Fragment.Fragment_House_Tab1;
import com.doan.cookpad.Fragment.Fragment_House_Tab2;
import com.doan.cookpad.Fragment.Fragment_House_Tab4;
import com.doan.cookpad.Fragment.Fragment_House_Tab5;
import com.doan.cookpad.R;

// Class này có chức năng tạo và sử dụng BottomNavigation
public class HouseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        BottomNavigationView navView = findViewById(R.id.navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new Fragment_House_Tab1());
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab1:
                    loadFragment(new Fragment_House_Tab1());
                    break;
                case R.id.tab2:
                    loadFragment(new Fragment_House_Tab2());
                    break;
                case R.id.tab3:
                    startActivity(new Intent(getApplicationContext(),CreateFood.class));
                    break;
                case R.id.tab4:
                    loadFragment(new Fragment_House_Tab4());
                    break;
                case R.id.tab5:
                    loadFragment(new Fragment_House_Tab5());
                    break;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
