package com.example.bottomnavigationabar2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.baoyz.widget.PullRefreshLayout;
import com.example.bottomnavigationabar2.MoBan.MoBan_1;
import com.example.bottomnavigationabar2.MoBan.MoBan_2;
import com.example.bottomnavigationabar2.MoBan.MoBan_3;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import android.view.MotionEvent;

import static android.support.constraint.Constraints.TAG;


/**
 * Created by 武当山道士 on 2017/8/16.
 */


public class HomeFragment extends Fragment {
    private Banner mBanner;
    private MyImageLoader mMyImageLoader;
    private ArrayList<Integer> imagePath;
    private ArrayList<String> imageTitle;
    private View view;
    private SmartRefreshLayout smartRefreshLayout;


    //

    /**
     * 占位tablayout，用于滑动过程中去确定实际的tablayout的位置
     */
    private TabLayout holderTabLayout;
    /**
     * 实际操作的tablayout，
     */
    private TabLayout realTabLayout;
    private PullRefreshLayout pullref;
    private CustomScrollView scrollView;
    private LinearLayout container;
    private BottomNavigationBar bottomNavigationBar1;
    private MoBan_1 moban1;
    private MoBan_2 moban2;
    private MoBan_3 moban3;
    private String[] tabTxt = {"热门", "最新", "推荐", "关注",};
    private List<AnchorView> anchorList = new ArrayList<>();
    private boolean isScroll;
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    private int lastPos = 0;
    //监听判断最后一个模块的高度，不满一屏时让最后一个模块撑满屏幕
    private ViewTreeObserver.OnGlobalLayoutListener listener;

    //
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

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
        holderTabLayout = view.findViewById(R.id.tablayout_holder);
        realTabLayout = view.findViewById(R.id.tablayout_real);
        scrollView = view.findViewById(R.id.scrollView);
                            smartRefreshLayout= (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
                            container = view.findViewById(R.id.container);
                            smartRefreshLayout.setRefreshHeader(new MaterialHeader(getContext()).setShowBezierWave(true));
                            smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));
                            smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                                @Override
                                public void onRefresh(RefreshLayout refreshlayout) {
                                    smartRefreshLayout.finishRefresh(2000);
                                    Toast.makeText(getContext(),"您已经下拉",Toast.LENGTH_SHORT).show();
                                }
                            });

                            smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                                @Override
                                public void onLoadmore(RefreshLayout refreshlayout) {
                                    smartRefreshLayout.finishLoadmore(2000);
                                    Toast.makeText(getContext(),"您已经上拉",Toast.LENGTH_SHORT).show();
                                }
                            });


        for (int i = 0; i < tabTxt.length; i++) {
            AnchorView anchorView = new AnchorView(getContext());
            //模板文字
//            anchorView.setAnchorTxt(tabTxt[i]);
//            anchorView.setContentTxt(tabTxt[i]);
            anchorList.add(anchorView);

            //将设置好的模板放入 container中
//            container.addView(anchorView);
        }
        for (int i = 0; i < tabTxt.length; i++) {
            holderTabLayout.addTab(holderTabLayout.newTab().setText(tabTxt[i]));
            realTabLayout.addTab(realTabLayout.newTab().setText(tabTxt[i]));
        }
        final ViewGroup finalContainer = container;
        listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //计算让最后一个view高度撑满屏幕
                int screenH = getScreenHeight();
                int statusBarH = getStatusBarHeight(getActivity());
                int tabH = holderTabLayout.getHeight();
                int lastH = screenH - statusBarH - tabH - 16 * 3;
                AnchorView anchorView = anchorList.get(anchorList.size() - 1);
                if (anchorView.getHeight() < lastH) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.height = lastH;
                    anchorView.setLayoutParams(params);
                }
                //一开始让实际的tablayout 移动到 占位的tablayout处，覆盖占位的tablayout
                realTabLayout.setTranslationY(holderTabLayout.getTop());
                realTabLayout.setVisibility(View.VISIBLE);
                finalContainer.getViewTreeObserver().removeOnGlobalLayoutListener(listener);

            }
        };
        container.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isScroll = true;
                }
                return false;
            }
        });

        //监听scrollview滑动
        scrollView.setCallbacks(new CustomScrollView.Callbacks() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
                //根据滑动的距离y(不断变化的) 和 holderTabLayout距离父布局顶部的距离(这个距离是固定的)对比，
                //当y < holderTabLayout.getTop()时，holderTabLayout 仍在屏幕内，realTabLayout不断移动holderTabLayout.getTop()距离，覆盖holderTabLayout
                //当y > holderTabLayout.getTop()时，holderTabLayout 移出，realTabLayout不断移动y，相对的停留在顶部，看上去是静止的
                int translation = Math.max(y, holderTabLayout.getTop());
                realTabLayout.setTranslationY(translation);
                if (isScroll) {
                    for (int i = tabTxt.length - 1; i >= 0; i--) {
                        //需要y减去顶部内容区域的高度(具体看项目的高度，这里demo写死的200dp)
                        if (y - 200 * 3 > anchorList.get(i).getTop() - 10) {
                            setScrollPos(i);
                            break;
                        }
                    }
                }

            }
        });
        setDefaultFragment();
        //实际的tablayout的点击切换
        realTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
                        }
                        transaction.replace(R.id.container,moban1);
                        break;
                    case "最新":
                        if (moban2 == null) {
                            moban2=new MoBan_2();
                        }
                        transaction.replace(R.id.container,moban2);
                        break;
                    case "推荐":
                        if (moban3 == null) {
                            moban3=new MoBan_3();
                        }
                        transaction.replace(R.id.container,moban3);
                        break;
                }
//                isScroll = false;
//                int pos = tab.getPosition();
//                int top = anchorList.get(pos).getTop();
//                //同样这里滑动要加上顶部内容区域的高度(这里写死的高度)
//                scrollView.smoothScrollTo(0, top + 200 * 3);
                transaction.commit();
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
        initData();
        initView();
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
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        moban1 = new MoBan_1();
        transaction.replace(R.id.container,moban1);
        transaction.commit();
    }
}

