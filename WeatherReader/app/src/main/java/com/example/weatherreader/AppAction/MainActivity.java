package com.example.weatherreader.AppAction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherreader.Adapters.CustomerAdapter;
import com.example.weatherreader.BackPageInterface.BackListenerFragment;
import com.example.weatherreader.Fragments.FragmentOne;
import com.example.weatherreader.Fragments.FragmentThree;
import com.example.weatherreader.Fragments.FragmentTwo;
import com.example.weatherreader.ModelClasses.Main;
import com.example.weatherreader.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    public ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewPagerId);

        CustomerAdapter customerAdapter = new CustomerAdapter(this);
        viewPager2.setAdapter(customerAdapter);

        viewPager2.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
//        if (FragmentOne.backBtnListener!=null){
//            FragmentOne.backBtnListener.onBackPressed();
//        }

        if(FragmentTwo.backBtnListener!=null){
            FragmentTwo.backBtnListener.onBackPressed();
        }

        if(FragmentThree.backBtnListener!=null){
            FragmentThree.backBtnListener.onBackPressed();
        }
    }
}
