package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.bottomnavigationabar2.MoBan.MoBan_1;
import com.example.bottomnavigationabar2.MoBan.MoBan_2;
import com.example.bottomnavigationabar2.MoBan.MoBan_3;

import java.util.ArrayList;

public class MainTabFragmentAdapter extends FragmentStatePagerAdapter {
    public ArrayList<Fragment> fragments;
    public Context mContext;
    private String[] titles;

    public MainTabFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        initFragments();
    }

    public MainTabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    private void initFragments() {
        titles = new String[]{
                "热门", "最新", "推荐", "关注"
        };
        fragments = new ArrayList<>();
        fragments.add(new MoBan_1());
        fragments.add(new MoBan_2());
        fragments.add(new MoBan_3());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
