package com.example.bottomnavigationabar2;

import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bottomnavigationabar2.MoBan.CommentTemplate;
import com.example.bottomnavigationabar2.adapter.MainTabFragmentAdapter;
import com.example.bottomnavigationabar2.adapter.MessageTabFragementAdapter;
import com.example.bottomnavigationabar2.adapter.PostAdapter;
import com.example.bottomnavigationabar2.bean.User;
import com.example.bottomnavigationabar2.bean.UserMessage;
import com.example.bottomnavigationabar2.dto.CommentDto;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;
import com.example.bottomnavigationabar2.utils.NetWorkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;
import static com.scwang.smartrefresh.layout.internal.InternalClassics.ID_IMAGE_PROGRESS;
import static com.scwang.smartrefresh.layout.internal.InternalClassics.ID_TEXT_TITLE;

public class Mynews extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Mynews";
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private TextView qiandao;
    private TextView te2_blue;
    private TextView te1_blue;
    private int currIndex = 0;
    private int offset = 0;
    private int one;
    private TextView OneLayout;
    private TextView TwoLayout;
    private PostAdapter postAdapter;
    private List<UserMessage> userMessages=new ArrayList<>();
    private User userData;
    private int startPage=1;
    private boolean flag=true;
    private SmartRefreshLayout refreshLayout;
    private CommentTemplate template;
    private MessageTabFragementAdapter messageTabFragementAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mynews);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initView();
        initRefreshLayout();
        initData();
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    qiandao.setVisibility(View.VISIBLE);
                    te1_blue.setVisibility(View.VISIBLE);
                    te2_blue.setVisibility(View.GONE);
                    animation = new TranslateAnimation(one, 0, 0, 0);
                    break;
                case 1:
                    te1_blue.setVisibility(View.GONE);
                    te2_blue.setVisibility(View.VISIBLE);
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
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_one:
                te1_blue.setVisibility(View.VISIBLE);
                te2_blue.setVisibility(View.GONE);
                viewPager.setCurrentItem(0);
                qiandao.setVisibility(view.VISIBLE);
                break;
            case R.id.tv_two:
                te1_blue.setVisibility(View.GONE);
                te2_blue.setVisibility(View.VISIBLE);
                qiandao.setVisibility(view.GONE);
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }
    private void initView(){
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        LayoutInflater inflater1 =getLayoutInflater();
        qiandao = findViewById(R.id.tv_search);
        OneLayout = (TextView)findViewById(R.id.tv_one);
        TwoLayout = (TextView)findViewById(R.id.tv_two);
        te1_blue = findViewById(R.id.te1_blue);
        te2_blue = findViewById(R.id.te2_blue);
        ImageView newreturn=findViewById(R.id.news_return);
        OneLayout.setOnClickListener(this);
        TwoLayout.setOnClickListener(this);
        messageTabFragementAdapter=new MessageTabFragementAdapter(this.getSupportFragmentManager(),this);
        viewPager.setAdapter(messageTabFragementAdapter);
        //设置viewPager的初始界面为第一个界面
        te1_blue.setVisibility(View.VISIBLE);
        viewPager.setCurrentItem(0);
        //添加切换界面的监听器
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        newreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void initRefreshLayout(){
        refreshLayout=findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));

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
                if (template.flag) {
                    template.getMessage(userData.getToken());
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
                } else if (!template.flag) {
                    tv.setText("没有更多消息拉");
                } else {
                    tv.setText("加载完成");
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
    private void initData(){
        userData= FileCacheUtil.getUser(Mynews.this);
        template= (CommentTemplate) messageTabFragementAdapter.getItem(0);
    }

}
