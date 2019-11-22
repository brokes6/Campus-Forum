package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.bottomnavigationabar2.MoBan.CommentTemplate;
import com.example.bottomnavigationabar2.MoBan.MoBan_1;
import com.example.bottomnavigationabar2.MoBan.MoBan_2;
import com.example.bottomnavigationabar2.MoBan.MoBan_3;
import com.example.bottomnavigationabar2.MoBan.MoBan_4;

import java.util.ArrayList;

public class MessageTabFragementAdapter extends FragmentStatePagerAdapter {
    public ArrayList<Fragment> fragments;
    public Context mContext;
    private String[] titles;
    private static final String TAG = "MessageTabFragementAdap";
    public MessageTabFragementAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        initFragments();
        Log.i(TAG, "MainTabFragmentAdapter: 又要初始化？");
    }

    public MessageTabFragementAdapter(FragmentManager fm) {
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
                "回复我的", "xx"
        };
        fragments = new ArrayList<>();
        fragments.add(new CommentTemplate());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
