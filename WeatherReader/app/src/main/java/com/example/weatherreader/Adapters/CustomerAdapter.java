package com.example.weatherreader.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weatherreader.Fragments.FragmentOne;
import com.example.weatherreader.Fragments.FragmentThree;
import com.example.weatherreader.Fragments.FragmentTwo;

public class CustomerAdapter extends FragmentStateAdapter {

    public CustomerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;

//        if(position==0){
//            fragment = new FragmentOne();
//        }

        if(position==0){
            fragment = new FragmentTwo();
        }

        if(position==1){
            fragment = new FragmentThree();
        }
        return fragment;

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
