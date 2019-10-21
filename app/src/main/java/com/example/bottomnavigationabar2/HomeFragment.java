package com.example.bottomnavigationabar2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bottomnavigationabar2.MoBan.MoBanInterface;
import com.example.bottomnavigationabar2.MoBan.MoBan_1;
import com.example.bottomnavigationabar2.MoBan.MoBan_2;
import com.example.bottomnavigationabar2.MoBan.MoBan_3;
import com.example.bottomnavigationabar2.adapter.MainTabFragmentAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;


/**
 * 老子重构 2019/10/21 14:45
 */

public class HomeFragment extends Fragment {
    private Banner mBanner;
    private MyImageLoader mMyImageLoader;
    private ArrayList<Integer> imagePath;
    private ArrayList<String> imageTitle;
    private View view;
    private MoBanInterface moBanInterface;
    /**
     * 实际操作的tablayout，
     */
    private TabLayout realTabLayout;
    private MoBan_1 moban1;
    private MoBan_2 moban2;
    private MoBan_3 moban3;
    private String[] tabTxt = {"热门", "最新", "推荐", "关注",};
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    private int lastPos = 0;
    private  volatile boolean flag;
    private LinearLayout topLayout;
    private int realTabLayoutHeight=-1;
    private AppBarLayout appBarLayout;
    private int topLayoutHeight=-1;
    private MainTabFragmentAdapter mainTabFragmentAdapter;
    private ViewPager viewPager;
    private SmartRefreshLayout refreshLayout;
    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
    public HomeFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        Bundle bundle = getArguments();
        SearchView searchView = (SearchView)view.findViewById(R.id.searchView);
        realTabLayout = view.findViewById(R.id.tablayout_real);
        container = view.findViewById(R.id.container);
        topLayout=view.findViewById(R.id.topLayout);
        setDefaultFragment();
        //实际的tablayout的点击切换
        viewPager=view.findViewById(R.id.viewPager);
        viewPager.setAdapter(mainTabFragmentAdapter);
        viewPager.setOffscreenPageLimit(mainTabFragmentAdapter.getCount());
        topLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                topLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.i(TAG, "onGlobalLayout: height="+topLayout.getHeight());
                topLayoutHeight=topLayout.getHeight();
            }
        });
        /*realTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(getContext(),"以点击"+tab.getText().toString(),Toast.LENGTH_SHORT).show();
                FragmentManager fm = getActivity().getFragmentManager();
                //开启事务
                FragmentTransaction transaction = fm.beginTransaction();
                Log.i(TAG, "onTabSelected: 点击的是"+tab.getText().toString());
                switch (tab.getText().toString()){
                    case "热门":
                        if (moban1 == null) {
                            moban1=new MoBan_1();
                            moBanInterface=moban1;
                        }
                        transaction.replace(R.id.container,moban1);
                        break;
                    case "最新":
                        if (moban2 == null) {
                            moban2=new MoBan_2();
                            moBanInterface=moban2;
                        }
                        transaction.replace(R.id.container,moban2);
                        break;
                    case "推荐":
                        if (moban3 == null) {
                            moban3=new MoBan_3();
                            moBanInterface=moban3;
                        }
                        transaction.replace(R.id.container,moban3);
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
        searchView.setQueryHint("请输入搜索内容");
        searchView.setIconifiedByDefault(false);
        for (int i = 0; i < tabTxt.length; i++) {
            realTabLayout.addTab(realTabLayout.newTab().setText(tabTxt[i]));
        }
        initData();
        initView();
        initRefreshLayout();
        initAppBarLayout();
        return view;
    }
    private void initData() {
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.mipmap.mao1);
        imagePath.add(R.mipmap.mao2);
        imagePath.add(R.mipmap.mao3);
        imageTitle.add("我是猫1");
        imageTitle.add("我是猫2");
        imageTitle.add("我是猫3");
    }
    private void initView() {
        mMyImageLoader = new MyImageLoader();
        mBanner =view.findViewById(R.id.banner);
        //设置样式，里面有很多种样式可以自己都看看效果
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(mMyImageLoader);
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        //轮播图片的文字
        mBanner.setBannerTitles(imageTitle);
        //设置轮播间隔时间
        mBanner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载地址
        mBanner.setImages(imagePath)
                //轮播图的监听
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Toast.makeText(getActivity(), "你点了第" + (position + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
                    }
                })
                //开始调用的方法，启动轮播图。
                .start();

    }
    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }
    private void setScrollPos(int newPos) {
        if (lastPos != newPos) {
            realTabLayout.setScrollPosition(newPos, 0, true);
        }
        lastPos = newPos;
    }

    private int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    private void setDefaultFragment() {
        mainTabFragmentAdapter = new MainTabFragmentAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager(),getActivity());
    }

    public boolean isFlag() {
        return flag;
    }
    public void initAppBarLayout(){
        appBarLayout = view.findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int i) {
                i=Math.abs(i);
                if(topLayoutHeight>=0&&i>=topLayoutHeight){
                    Log.d(TAG, "onOffsetChanged: i="+i);
                    Log.d(TAG, "initAppBarLayout: topLayoutHeight="+topLayoutHeight);
                    flag=true;
                    //延时任务
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) topLayout.getLayoutParams();
                            params.setScrollFlags(0);
                            topLayout.setLayoutParams(params);
                            topLayout.setVisibility(View.GONE);
                            refreshLayout.setEnableLoadMore(true);
                            refreshLayout.setEnableRefresh(true);
                        }
                    },100);
                }
            }
        });
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public void exit(){
        flag=false;
        ((MoBanInterface)mainTabFragmentAdapter.getFragments().get(0)).getRecycler().scrollToPosition(0);
        topLayout.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams)topLayout.getLayoutParams();
                mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL| AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                topLayout.setLayoutParams(mParams);
            }
        },300);
    }
    public void initRefreshLayout(){
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new MaterialHeader(getContext()));
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
        });
    }
}

