package com.example.bottomnavigationabar2;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
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
import com.example.bottomnavigationabar2.bean.User;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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
    private View mViewBackgroundTop;
    private View mViewBackgroundBottom;
    private MoBanInterface moBanInterface;
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
    public static User userData;
    public static HomeFragment newInstance(String param1,User user) {
        HomeFragment fragment = new HomeFragment(user);
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
    public HomeFragment(User user) {
        userData=user;
    }

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.home_fragment, container, false);
            Bundle bundle = getArguments();
            SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
            realTabLayout = view.findViewById(R.id.tablayout_real);
            container = view.findViewById(R.id.container);
            topLayout = view.findViewById(R.id.topLayout);
            setDefaultFragment();
            //实际的tablayout的点击切换
            viewPager = view.findViewById(R.id.viewPager);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    Log.i(TAG, "onPageScrolled: what are you doing?!---"+i);
                    moBanInterface=(MoBanInterface) mainTabFragmentAdapter.fragments.get(i);
                }

                @Override
                public void onPageSelected(int i) {

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
            viewPager.setAdapter(mainTabFragmentAdapter);
            viewPager.setOffscreenPageLimit(mainTabFragmentAdapter.getCount());
            viewPager.setOffscreenPageLimit(1);
            topLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    topLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Log.i(TAG, "onGlobalLayout: height=" + topLayout.getHeight());
                    topLayoutHeight = topLayout.getHeight();
                }
            });
            realTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Toast.makeText(getContext(), "以点击" + tab.getText().toString(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onTabSelected: 点击的是" + tab.getPosition());
                    viewPager.setCurrentItem(tab.getPosition());
                    moBanInterface = (MoBanInterface) mainTabFragmentAdapter.fragments.get(tab.getPosition());
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            searchView.setQueryHint("请输入搜索内容");
            searchView.setIconifiedByDefault(false);
            for (int i = 0; i < tabTxt.length; i++) {
                realTabLayout.addTab(realTabLayout.newTab().setText(tabTxt[i]));
            }
            initData();
            initView();
            initRefreshLayout();
            initAppBarLayout();
        }
        mViewBackgroundTop =view.findViewById(R.id.appbar);
        mViewBackgroundBottom =view.findViewById(R.id.viewPager);
        initTransition();
        return view;
    }

    //初始化动画
    private void initTransition() {
        final TransitionSet transitionSet = new TransitionSet();
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.addTarget(R.id.banner);
        transitionSet.addTransition(slide);
        //初始化Explode
        Explode explode = new Explode();
        explode.excludeTarget(android.R.id.statusBarBackground, true);
        explode.excludeTarget(android.R.id.navigationBarBackground, true);
        //将上半部分添加进来
        explode.excludeTarget(R.id.banner, true);
        //将Explode添加进Transition中
        transitionSet.addTransition(explode);
        //设置Transition的 Ordering
        transitionSet.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        //android获取屏幕大小宽度的（ getWindow（） ）
        getActivity().getWindow().setEnterTransition(transitionSet);
        transitionSet.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                //动画开始
                //指定上半部分的动画效果
                mViewBackgroundTop.setVisibility(View.GONE);
                //指定下半部分的动画效果
                mViewBackgroundBottom.setVisibility(View.GONE);
            }
            @Override
            public void onTransitionEnd(Transition transition) {
                mViewBackgroundTop.setVisibility(View.VISIBLE);
                //指定下半部分view的结束动画
                mViewBackgroundBottom.setVisibility(View.VISIBLE);
                Animator animationTop = ViewAnimationUtils.createCircularReveal(mViewBackgroundTop,
                        mViewBackgroundTop.getWidth() / 2,
                        mViewBackgroundTop.getHeight() / 2,
                        0, Math.max(mViewBackgroundTop.getWidth() / 2,
                                mViewBackgroundTop.getHeight() / 2));
                Animator animationBottom = ViewAnimationUtils.createCircularReveal(mViewBackgroundBottom,
                        mViewBackgroundBottom.getWidth(),
                        mViewBackgroundBottom.getHeight(),
                        0, (float) Math.hypot(mViewBackgroundBottom.getWidth(),
                                mViewBackgroundBottom.getHeight()));
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(500L);
                animatorSet.playTogether(animationTop, animationBottom);
                animatorSet.start();
                transitionSet.removeListener(this);
            }
            @Override
            public void onTransitionCancel(Transition transition) {

            }
            @Override
            public void onTransitionPause(Transition transition) {

            }
            @Override
            public void onTransitionResume(Transition transition) {

            }

        });
    }
    //初始化轮播图
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
    private void setDefaultFragment() {
        mainTabFragmentAdapter = new MainTabFragmentAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager(),getActivity());
        moBanInterface= (MoBanInterface) mainTabFragmentAdapter.getItem(0);
        moBanInterface.getPostList(userData.getToken());
        Log.i(TAG, "setDefaultFragment: 11111111111");
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
    public void exit(){
        flag=false;
        moBanInterface.getRecycler().scrollToPosition(0);
        topLayout.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams)topLayout.getLayoutParams();
                mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL| AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                topLayout.setLayoutParams(mParams);
                refreshLayout.setEnableLoadMore(false);
                refreshLayout.setEnableRefresh(false);

            }
        },10);
    }
    //重写下拉刷新
    public void initRefreshLayout(){
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new MaterialHeader(getContext()));
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Log.i(TAG, "onLoadMore: 下拉加载");
                refreshLayout.autoLoadMore();
                moBanInterface.getPostList(userData.getToken());
                refreshLayout.finishLoadMore();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.i(TAG, "onRefresh: 上拉刷新");
                refreshLayout.autoRefresh();
                moBanInterface.clearList();
                moBanInterface.getPostList(userData.getToken());
                refreshLayout.finishRefresh();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: 冲冲冲");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ?????");
        if(view!=null){
            ((ViewGroup)view.getParent()).removeView(view);
        }
    }

}

