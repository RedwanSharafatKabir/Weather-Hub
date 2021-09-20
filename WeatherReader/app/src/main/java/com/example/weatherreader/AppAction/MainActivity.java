package com.example.weatherreader.AppAction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.weatherreader.Adapters.CustomerAdapter;
import com.example.weatherreader.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout linearLayout;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        linearLayout = findViewById(R.id.linearLayoutId);
//        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.bakgroundViewPagerLayoutId);

//        setupViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);

        // Fragment এর Object
        FragmentManager fragmentManager = getSupportFragmentManager();

        // CustomAdapter Class এর Object
        CustomerAdapter obj = new CustomerAdapter(fragmentManager);
        viewPager.setAdapter(obj);

        viewPager.setCurrentItem(1);
    }
}
