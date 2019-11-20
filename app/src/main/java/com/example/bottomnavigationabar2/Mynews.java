package com.example.bottomnavigationabar2;

import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import java.util.ArrayList;

public class Mynews extends AppCompatActivity implements View.OnClickListener{
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private TextView qiandao;
    private TextView te2_blue;
    private TextView te1_blue;
    private int currIndex = 0;
    private int offset = 0;
    private int one;
    private TextView OneLayout;
    private TextView TwoLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mynews);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        LayoutInflater inflater1 =getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.news_one, null);
        View view2 = inflater1.inflate(R.layout.news_two, null);
        qiandao = findViewById(R.id.tv_search);
        OneLayout = (TextView)findViewById(R.id.tv_one);
        TwoLayout = (TextView)findViewById(R.id.tv_two);
        te1_blue = findViewById(R.id.te1_blue);
        te2_blue = findViewById(R.id.te2_blue);
        OneLayout.setOnClickListener(this);
        TwoLayout.setOnClickListener(this);
        pageview =new ArrayList<View>();
        //添加想要切换的界面
        pageview.add(view1);
        pageview.add(view2);
        PagerAdapter mPagerAdapter = new PagerAdapter(){
            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }
            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0==arg1;
            }
            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageview.get(arg1));
            }
            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1){
                ((ViewPager)arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }
        };
        viewPager.setAdapter(mPagerAdapter);
        //设置viewPager的初始界面为第一个界面
        te1_blue.setVisibility(View.VISIBLE);
        viewPager.setCurrentItem(0);
        //添加切换界面的监听器
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
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
}
