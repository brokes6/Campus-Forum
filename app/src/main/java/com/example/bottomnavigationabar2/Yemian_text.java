package com.example.bottomnavigationabar2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.MoBan.PostTemplateInterface;
import com.example.bottomnavigationabar2.adapter.MainTabFragmentAdapter;
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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.scwang.smartrefresh.layout.internal.InternalClassics.ID_IMAGE_PROGRESS;
import static com.scwang.smartrefresh.layout.internal.InternalClassics.ID_TEXT_TITLE;

public class Yemian_text extends AppCompatActivity {
    private static final String TAG = "Yemian_text";
    private FloatingActionButton addPostButton;
    private ViewPager viewPager;
    private MainTabFragmentAdapter mainTabFragmentAdapter;
    private TabLayout tabLayout;
    private PostTemplateInterface postTemplateInterface;
    private User userData;
    private AppBarLayout appBarLayout;
    private LinearLayout topLayout;
    private int topLayoutHeight=-1;
    private boolean flag=false;
    private SmartRefreshLayout refreshLayout;
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemian);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        initView();
        initData();
        setDefaultFragment();
        initRefreshLayout();
        initAppBarLayout();
    }
    private void initView(){
        //悬浮按钮 点击事件
        addPostButton=findViewById(R.id.fab);
        addPostButton.bringToFront();
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_addPost = new Intent(Yemian_text.this,addPost.class);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                startActivity(intent_addPost);
            }
        });
        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);
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
        topLayout = findViewById(R.id.topLayout);
        appBarLayout = findViewById(R.id.appbar);
    }
    private void initData(){
        userData= FileCacheUtil.getUser(this);
        mainTabFragmentAdapter=new MainTabFragmentAdapter(getSupportFragmentManager(),this);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(mainTabFragmentAdapter);
        viewPager.setOffscreenPageLimit(1);
        topLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                topLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.i(TAG, "onGlobalLayout: height=" + topLayout.getHeight());
                topLayoutHeight = topLayout.getHeight();
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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
    }
    private void setDefaultFragment() {
        postTemplateInterface = (PostTemplateInterface) mainTabFragmentAdapter.getItem(0);
        Log.i(TAG, "setDefaultFragment: 11111111111");
    }
    private void initAppBarLayout(){
        Log.i(TAG, "initAppBarLayout: 初始化了");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int i) {
                i=Math.abs(i);
                Log.i(TAG, "onOffsetChanged: toplayoutheight="+topLayoutHeight);
                Log.i(TAG, "onOffsetChanged: i="+i);
                if(topLayoutHeight>=0&&i>=topLayoutHeight){
                    Log.d(TAG, "onOffsetChanged: i="+i);
                    Log.d(TAG, "initAppBarLayout: topLayoutHeight="+topLayoutHeight);
                    flag=true;
                    //延时任务
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "run: 我有被执行？");
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
    public void initRefreshLayout(){
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new MaterialHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
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
                Log.i(TAG, "onRefresh: 上拉刷新");
                refreshLayout.autoRefresh();
                postTemplateInterface.clearList();
                postTemplateInterface.getPostList(userData.getToken());
                refreshLayout.finishRefresh();
            }
        });
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
                        if (!NetWorkUtil.isNetworkConnected(getApplicationContext())) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System .currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                if (flag) {
                    exit();
                }

            } else {
                //如果不需要拉出顶部的header，直接关闭当前的界面
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
}
