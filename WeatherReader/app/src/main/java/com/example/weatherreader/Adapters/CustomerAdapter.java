package com.example.weatherreader.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.weatherreader.Fragments.FragmentOne;
import com.example.weatherreader.Fragments.FragmentThree;
import com.example.weatherreader.Fragments.FragmentTwo;

public class CustomerAdapter extends FragmentStatePagerAdapter {

    public CustomerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if(position==0){
            fragment = new FragmentOne();
        }

        if(position==1){
            fragment = new FragmentTwo();
        }

        if(position==2){
            fragment = new FragmentThree();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // Total number of fragment pages
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "Previous 10 Days";
        }

        if(position==1){
            return "Today";
        }

        if(position==2){
            return "Next 10 Days";
        }

        return null;
    }
}
