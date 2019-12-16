package com.example.bottomnavigationabar2;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.MoBan.OrganizationRecommendTemplate;
import com.example.bottomnavigationabar2.MoBan.PostTemplateInterface;
import com.example.bottomnavigationabar2.adapter.MainTabFragmentAdapter;
import com.example.bottomnavigationabar2.adapter.OrganizationTabFragmentAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.util.ArrayList;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class ScanFragment extends Fragment{
    private static final String TAG = "ScanFragment";
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private TextView followText;
    private TextView recommendText;
    private TextView signin;
    private int offset = 0;
    private View view;
    private SmartRefreshLayout refreshLayout;
    public static ScanFragment newInstance(String param1) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ScanFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("我要重新创建？");
        if(view==null) {
            view = inflater.inflate(R.layout.mscan_fragment, container, false);
            initView();
        }
        return view;
    }
    private void initView(){
        followText=view.findViewById(R.id.followText);
        recommendText=view.findViewById(R.id.recommendText);
        viewPager=view.findViewById(R.id.viewPager);
        OrganizationTabFragmentAdapter adapter=new OrganizationTabFragmentAdapter(((MainActivity)getContext()).getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        System.out.println("viewpager"+viewPager);
        followText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        recommendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if(i==1){
                    recommendText.setTextSize(16);
                    followText.setTextSize(20);
                }else {
                    recommendText.setTextSize(20);
                    followText.setTextSize(16);
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
        initRefreshLayout();
    }

/*    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    qiandao.setVisibility(View.VISIBLE);
                    *//**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **//*
                    animation = new TranslateAnimation(one, 0, 0, 0);
                    break;
                case 1:
                    qiandao.setVisibility(View.GONE);
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    break;
            }
            //arg0为切换到的页的编码
            currIndex = arg0;
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("被摧毁");
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("开始拉");
    }
    private void initRefreshLayout(){
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new MaterialHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        initView();
//    }
}

