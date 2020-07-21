package com.example.myapplication;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class LoginAfter extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewPager vp = findViewById(R.id.viewpager);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        TabLayout tab = findViewById(R.id.tab);
        tab.setupWithViewPager(vp);

        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_heart_foreground);
        images.add(R.drawable.ic_food_foreground);
        images.add(R.drawable.ic_map_foreground);

        for(int i=0; i<3; i++){
            tab.getTabAt(i).setIcon(images.get(i));
        }

        Objects.requireNonNull(tab.getTabAt(0).getIcon()).setColorFilter(Color.parseColor("#F5C5FF"), PorterDuff.Mode.SRC_ATOP);
        Objects.requireNonNull(tab.getTabAt(1).getIcon()).setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_ATOP);
        Objects.requireNonNull(tab.getTabAt(2).getIcon()).setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_ATOP);

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#F5C5FF"), PorterDuff.Mode.SRC_ATOP);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_ATOP);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}

