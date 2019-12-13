package com.example.bottomnavigationabar2.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.bottomnavigationabar2.MoBan.OrganizationFollowTemplate;
import com.example.bottomnavigationabar2.MoBan.OrganizationRecommendTemplate;

import java.util.ArrayList;

public class OrganizationTabFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments;
    private String[] titles = new String[]{
            "推荐", "关注"
    };
    public OrganizationTabFragmentAdapter(FragmentManager fm) {
        super(fm);
        initFragments();
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    private void initFragments(){
        fragments=new ArrayList<>();
        fragments.add(new OrganizationRecommendTemplate());
        fragments.add(new OrganizationFollowTemplate());
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
