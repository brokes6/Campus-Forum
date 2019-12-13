package com.example.bottomnavigationabar2;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bottomnavigationabar2.MoBan.PostTemplateInterface;
import com.example.bottomnavigationabar2.MoBan.PopularPostTemplate;
import com.example.bottomnavigationabar2.MoBan.NewPostTemplate;
import com.example.bottomnavigationabar2.MoBan.RecommendTemplate;
import com.example.bottomnavigationabar2.activity.SearchActivity;
import com.example.bottomnavigationabar2.adapter.MainTabFragmentAdapter;
import com.example.bottomnavigationabar2.adapter.NineGridTest2Adapter;
import com.example.bottomnavigationabar2.bean.User;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;
import com.example.bottomnavigationabar2.utils.NetWorkUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


import static com.scwang.smartrefresh.layout.internal.InternalClassics.ID_IMAGE_PROGRESS;
import static com.scwang.smartrefresh.layout.internal.InternalClassics.ID_TEXT_TITLE;


/**
 * 老子重构 2019/10/21 14:45
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    public static final int POSTDETAILS=1;
    public static final int SEARCHVIEW=2;
    private Banner mBanner;
    private MyImageLoader mMyImageLoader;
    private ArrayList<Integer> imagePath;
    private ArrayList<String> imageTitle;
    private View view;
    private View mViewBackgroundTop;
    private View mViewBackgroundBottom;
    private PostTemplateInterface postTemplateInterface;
    private TabLayout realTabLayout;
    private PopularPostTemplate moban1;
    private NewPostTemplate moban2;
    private RecommendTemplate moban3;
    private String[] tabTxt = {"热门", "最新", "推荐", "关注",};
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    private int lastPos = 0;
    private  volatile boolean flag;
    private LinearLayout topLayout;
    private int realTabLayoutHeight=-1;
    private AppBarLayout appBarLayout;
    private int topLayoutHeight=-1;
    private CoordinatorLayout layout_top;
    private MainTabFragmentAdapter mainTabFragmentAdapter;
    private ViewPager viewPager;
    private SmartRefreshLayout refreshLayout;
    public static User userData;
    private LinearLayout searchView;
    private NineGridTest2Adapter.ViewHolder viewHolder;
    private boolean showSearchView=true;
    private ImageView Return_top;
    private AppBarLayout mAppBarLayout;
    private CoordinatorLayout.Behavior behavior;
    private int phoneHeight=-1;
    private ValueAnimator valueAnimator;
    public static HomeFragment newInstance() {
        HomeFragment fragment=new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userData= FileCacheUtil.getUser(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.home_fragment_test2, container, false);
//            searchView =view.findViewById(R.id.searchView);
            realTabLayout = view.findViewById(R.id.tablayout_real);
            container = view.findViewById(R.id.container);
            topLayout = view.findViewById(R.id.topLayout);
            Return_top = view.findViewById(R.id.Return_top);
            searchView = view.findViewById(R.id.search);
            layout_top = view.findViewById(R.id.layout_top);
            mAppBarLayout = view.findViewById(R.id.appbar);
            setDefaultFragment();
            //实际的tablayout的点击切换
            viewPager = view.findViewById(R.id.viewPager);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    postTemplateInterface =(PostTemplateInterface) mainTabFragmentAdapter.fragments.get(i);
                }

                @Override
                public void onPageSelected(int i) {

                }

                @Override
                public void onPageScrollStateChanged(int i) {
                }
            });
            realTabLayout.setupWithViewPager(viewPager);
            viewPager.setAdapter(mainTabFragmentAdapter);
            viewPager.setOffscreenPageLimit(3);
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
                    postTemplateInterface = (PostTemplateInterface) mainTabFragmentAdapter.fragments.get(tab.getPosition());
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
//            searchView.setQueryHint("请输入搜索内容");
//            searchView.setIconifiedByDefault(false);

            initData();
            initView();
            initRefreshLayout();
            initAppBarLayout();
/*            initSearchView();*/
        }
        mViewBackgroundTop =view.findViewById(R.id.appbar);
        mViewBackgroundBottom =view.findViewById(R.id.viewPager);
        initTransition();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivityForResult(intent, SEARCHVIEW);
            }
        });

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
                Log.i(TAG, "onTransitionStart: ??????");
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
        phoneHeight=new DisplayMetrics().heightPixels;
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.drawable.english_club);
        imagePath.add(R.drawable.english_club1);
        imagePath.add(R.drawable.english_club2);
        imagePath.add(R.drawable.english_club3);
        imageTitle.add("英语俱乐部合照");
        imageTitle.add("英语俱乐部合照");
        imageTitle.add("英语俱乐部合照");
        imageTitle.add("英语俱乐部合照");
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
        Return_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToTop(true);
                Toast.makeText(getContext(),"点击成功",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "hight为--------------------"+mAppBarLayout.getHeight());
            }
        });

    }


    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }
//    public void scrollToTop() {
//        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
//        if (behavior instanceof AppBarLayout.Behavior) {
//            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
//            int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
//            if (topAndBottomOffset != 0) {
//                appBarLayoutBehavior.setTopAndBottomOffset(0);
//            }
//        }
//    }
public  void  scrollToTop( boolean flag){
    CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
    if (behavior instanceof AppBarLayout.Behavior) {
        AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
        if(true){
            appBarLayoutBehavior.setTopAndBottomOffset(0); //快熟滑动到顶部
        }else {
            int hight= mAppBarLayout.getHeight();
            Log.d(TAG, "hight为--------------------"+hight);
            appBarLayoutBehavior.setTopAndBottomOffset(-hight);//快速滑动实现吸顶效果
        }
    }
}

    private void setDefaultFragment() {
        mainTabFragmentAdapter = new MainTabFragmentAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager(),getActivity());
        postTemplateInterface = (PostTemplateInterface) mainTabFragmentAdapter.getItem(0);
        postTemplateInterface.getPostList(userData.getToken());
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
        postTemplateInterface.getRecycler().scrollToPosition(0);
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
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        initRefreshFootLayout();
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Log.i(TAG, "onLoadMore: 下拉加载");
                refreshLayout.autoLoadMore();
                postTemplateInterface.getPostList(userData.getToken());
                refreshLayout.finishLoadMore();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if(NetWorkUtil.isNetworkConnected(getContext())) {
                    Log.i(TAG, "onRefresh: 上拉刷新");
                    refreshLayout.autoRefresh();
                    postTemplateInterface.clearList();
                    postTemplateInterface.getPostList(userData.getToken());
                    refreshLayout.finishRefresh();
                }else{
                    Toast.makeText(getContext(), "网络连接失败，请检查网络", Toast.LENGTH_SHORT).show();
                    refreshLayout.finishRefresh();
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case POSTDETAILS:break;
//            case SEARCHVIEW:
//                    Log.i(TAG, "onActivityResult: 返回了");
//                    searchView.clearFocus();
//                    searchView.onActionViewCollapsed();
        }
    }

    public void setViewHolder(NineGridTest2Adapter.ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }
    public void updateInfo(Intent intent){
        postTemplateInterface.updateInfo(intent);
    }
    private void initRefreshFootLayout(){
        refreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
        final TextView tv = refreshLayout.getLayout().findViewById(ID_TEXT_TITLE);
        final ImageView iv2 = refreshLayout.getLayout().findViewById(ID_IMAGE_PROGRESS);
        final AtomicBoolean net = new AtomicBoolean(true);
        final AtomicInteger mostTimes = new AtomicInteger(0);//假设只有三屏数据

        //设置多监听器，包括顶部下拉刷新、底部上滑刷新
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){

            /**
             * 根据上拉的状态，设置文字，并且判断条件
             * @param refreshLayout
             * @param oldState
             * @param newState
             */
            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                switch (newState) {
                    case None:
                    case PullUpToLoad:
                        break;
                    case Loading:
                    case LoadReleased:
                        tv.setText("正在加载..."); //在这里修改文字
                        if (!NetWorkUtil.isNetworkConnected(getContext().getApplicationContext())) {
                            net.set(false);
                        } else {
                            net.set(true);
                        }
                        break;
                    case ReleaseToLoad:
                        tv.setText("release");
                        break;
                    case Refreshing:
                        tv.setText("refreshing");
                        break;
                }
            }

            /**
             * 添加是否可以加载更多数据的条件
             * @param refreshLayout
             */
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mostTimes.get() < 3) {

                    mostTimes.getAndIncrement();
                }
                refreshLayout.finishLoadMore(1000); //这个记得设置，否则一直转圈
            }

            /**
             *  在这里根据不同的情况来修改加载完成后的提示语
             * @param footer
             * @param success
             */
            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
                super.onFooterFinish(footer, success);
                if (net.get() == false) {
                    tv.setText("请检查网络设置");
                } else if (mostTimes.get() >= 3) {
                    tv.setText("没有更多消息拉");
                } else {
                    tv.setText("加载完成");
                    if (mostTimes.get() == 2) {
                        mostTimes.getAndIncrement();
                    }
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Log.i(TAG, "onLoadMore: 下拉加载");
                refreshLayout.autoLoadMore();
            }
        });
    }
/*    private void initSearchView(){
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    Log.i(TAG, "onClick: 被点击了");
                    if(showSearchView){
                        valueAnimator=ValueAnimator.ofInt(0,phoneHeight);
                    }else {
                        valueAnimator=ValueAnimator.ofInt(phoneHeight,0);
                    }
                    showSearchView=!showSearchView;
                    valueAnimator.addUpdateListener(new ValueAnimatorUpdateListener());
                    valueAnimator.setDuration(1000);
                    valueAnimator.start();
            }
        });
    }*/
    public class ValueAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
                int h= (int) animation.getAnimatedValue();
                searchView.getLayoutParams().height=h;
                searchView.requestLayout();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: 我homefragment被摧毁了");
    }
}

