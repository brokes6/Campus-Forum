package com.example.bottomnavigationabar2;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class ScanFragment extends Fragment implements View.OnClickListener{
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private TextView videoLayout;
    private TextView musicLayout;
    private TextView qiandao;
    private int currIndex = 0;
    private int offset = 0;
    private int one;
    private View view;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mscan_fragment, container, false);
        Bundle bundle = getArguments();
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater1 =getActivity().getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.gz_layout, null);
        View view2 = inflater1.inflate(R.layout.tj_layout, null);
        Button button1 = view2.findViewById(R.id.Refresh);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"准备刷新",Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayout test =view2.findViewById(R.id.anniu1);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_yemian = new Intent(getActivity(),Yemian_text.class);
                startActivity(intent_yemian);
            }
        });
        qiandao = (TextView)view.findViewById(R.id.tv_qian);
        videoLayout = (TextView)view.findViewById(R.id.tv_gz);
        musicLayout = (TextView)view.findViewById(R.id.tv_tj);
        videoLayout.setOnClickListener(this);
        musicLayout.setOnClickListener(this);
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
        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        //设置viewPager的初始界面为第一个界面
        viewPager.setCurrentItem(0);
        //添加切换界面的监听器
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        // 获取滚动条的宽度
        //为了获取屏幕宽度，新建一个DisplayMetrics对象
        //将当前窗口的一些信息放在DisplayMetrics类中
        //得到屏幕的宽度
        //计算出滚动条初始的偏移量
        //计算出切换一个界面时，滚动条的位移量
        return view;

    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    qiandao.setVisibility(View.VISIBLE);
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/
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
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_gz:
                viewPager.setCurrentItem(0);
                qiandao.setVisibility(view.VISIBLE);
                break;
            case R.id.tv_tj:
                qiandao.setVisibility(view.GONE);
                viewPager.setCurrentItem(1);

                break;
            default:
                break;
        }
    }
}

