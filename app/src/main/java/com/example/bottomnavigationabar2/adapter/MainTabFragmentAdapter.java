package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.bottomnavigationabar2.MoBan.OrganizationFollowTemplate;
import com.example.bottomnavigationabar2.MoBan.OrganizationRecommendTemplate;
import com.example.bottomnavigationabar2.MoBan.PopularPostTemplate;
import com.example.bottomnavigationabar2.MoBan.NewPostTemplate;
import com.example.bottomnavigationabar2.MoBan.PostTemplateInterface;
import com.example.bottomnavigationabar2.MoBan.RecommendTemplate;
import com.example.bottomnavigationabar2.MoBan.FollowTemplate;
import com.example.bottomnavigationabar2.MoBan.SearchPostTemplate;

import java.util.ArrayList;

public class MainTabFragmentAdapter extends FragmentStatePagerAdapter {
    public ArrayList<Fragment> fragments;
    public Context mContext;
    private String[] titles;
    private static final String TAG = "MainTabFragmentAdapter";
    public MainTabFragmentAdapter(FragmentManager fm, Context context) {
        this(fm,context,false,-1,0);
    }
    public MainTabFragmentAdapter(FragmentManager fm,Context context,boolean flag,int tagId,int mode){
        super(fm);
        Log.i(TAG, "MainTabFragmentAdapter: 我没事被调用了？");
        mContext = context;
        initFragments(flag,tagId,mode);
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
                titles = new String[]{
                        "热门", "最新", "推荐", "关注"
                };
                fragments.add(PopularPostTemplate.newIntance(flag,tagId,PostTemplateInterface.STANDARD_POPULAR_URL));
                fragments.add(NewPostTemplate.newIntance(flag,tagId,PostTemplateInterface.STANDARD_NEW_URL));
                fragments.add(RecommendTemplate.newIntance(flag,tagId,PostTemplateInterface.STANDARD_POPULAR_URL));
                fragments.add(FollowTemplate.newIntance(flag,tagId,PostTemplateInterface.STANDARD_NEW_URL));
                break;
            case 1:
                titles = new String[]{
                        "热门", "最新", "推荐", "关注"
                };
                fragments.add(PopularPostTemplate.newIntance(flag,tagId,PostTemplateInterface.TAG_POPULAR_URL));
                fragments.add(NewPostTemplate.newIntance(flag,tagId,PostTemplateInterface.TAG_NEW_URL));
                fragments.add(RecommendTemplate.newIntance(flag,tagId,PostTemplateInterface.STANDARD_POPULAR_URL));
                fragments.add(FollowTemplate.newIntance(flag,tagId,PostTemplateInterface.STANDARD_NEW_URL));
                break;
            case 2:
                Log.i(TAG, "initFragments: fragementsize="+fragments.size());
                Log.i(TAG, "initFragments: 初始化2啊");
                titles = new String[]{
                        "贴吧", "帖子"
                };
                fragments.add(OrganizationRecommendTemplate.newIntance(false));
                fragments.add(new SearchPostTemplate());
                break;

        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
