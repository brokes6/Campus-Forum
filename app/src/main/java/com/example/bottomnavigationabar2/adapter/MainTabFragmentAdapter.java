package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.bottomnavigationabar2.MoBan.PopularPostTemplate;
import com.example.bottomnavigationabar2.MoBan.NewPostTemplate;
import com.example.bottomnavigationabar2.MoBan.PostTemplateInterface;
import com.example.bottomnavigationabar2.MoBan.PostTemplate_3;
import com.example.bottomnavigationabar2.MoBan.PostTemplate_4;

import java.util.ArrayList;

public class MainTabFragmentAdapter extends FragmentStatePagerAdapter {
    public ArrayList<Fragment> fragments;
    public Context mContext;
    private String[] titles = new String[]{
        "热门", "最新", "推荐", "关注"
    };
    private static final String TAG = "MainTabFragmentAdapter";
    public MainTabFragmentAdapter(FragmentManager fm, Context context) {
        this(fm,context,false,-1,0);
    }
    public MainTabFragmentAdapter(FragmentManager fm,Context context,boolean flag,int tagId,int mode){
        super(fm);
        mContext = context;
        initFragments(false,tagId,mode);
        Log.i(TAG, "MainTabFragmentAdapter: 又要初始化？");
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

    private void initFragments(boolean flag,int tagId,int mode) {
        fragments = new ArrayList<>();
        switch (mode){
            case 0:
                fragments.add(new PopularPostTemplate(flag,tagId,PostTemplateInterface.STANDARD_POPULAR_URL));
               /* fragments.add(new NewPostTemplate(flag,tagId,PostTemplateInterface.STANDARD_NEW_URL));
                fragments.add(new PostTemplate_3(flag,tagId,PostTemplateInterface.STANDARD_POPULAR_URL)));
                fragments.add(new PostTemplate_4(flag,tagId,PostTemplateInterface.STANDARD_NEW_URL));*/
               break;
            case 1:

        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
