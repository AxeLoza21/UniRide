package com.example.uniride.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uniride.R;
import com.example.uniride.tabs.CompletedFragment;
import com.example.uniride.tabs.InProgressFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class TravelFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_travel, container, false);
        viewPager = root.findViewById(R.id.viewPager);
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager.setSaveEnabled(false);// Arreglar crasheo de fragments


        setUpViewPagerAndTabs();

        return root;
    }





    private void setUpViewPagerAndTabs() {
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity != null) {
            viewPager.setAdapter(new TravelPagerAdapter(fragmentActivity));
            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> tab.setText(position == 0 ? "Viajes Solicitados" : "Viajes Creados"))
                    .attach();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }



    private class TravelPagerAdapter extends FragmentStateAdapter {
        public TravelPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new InProgressFragment();
                case 1:
                    return new CompletedFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}