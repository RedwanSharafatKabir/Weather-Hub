package com.example.weatherreader.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weatherreader.AppAction.MainActivity;
import com.example.weatherreader.BackPageInterface.BackListenerFragment;
import com.example.weatherreader.R;

import org.w3c.dom.Text;

public class FragmentOne extends Fragment implements BackListenerFragment, View.OnClickListener {

    View views;
    TextView today;
    public static BackListenerFragment backBtnListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_fragment_one, container, false);

        today = views.findViewById(R.id.todayId);
        today.setOnClickListener(this);

        return views;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.todayId:
                ((MainActivity) getActivity()).viewPager2.setCurrentItem(1);

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        backBtnListener = this;
    }

    @Override
    public void onPause() {
        backBtnListener = null;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        ((MainActivity) getActivity()).viewPager2.setCurrentItem(1);
    }
}
