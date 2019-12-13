package com.example.bottomnavigationabar2;//这里换成你自己的

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.bottomnavigationabar2.activity.BaseActivity;
import com.example.bottomnavigationabar2.bean.User;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener{

    private BottomNavigationBar bottomNavigationBar;
    public User userData;
    String user_name;
    int lastSelectedPosition = 0;
    private static final String TAG = "MainActivity";
    private MyFragment mMyFragment;
    private ScanFragment mScanFragment;
    private HomeFragment mHomeFragment;
    private SmartRefreshLayout refreshLayout;
    int sum= 0;
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        Intent intent = getIntent();
        user_name = intent.getStringExtra("username");
        Toast.makeText(this, "账号" + user_name, Toast.LENGTH_SHORT).show();
        userData=FileCacheUtil.getCache(this,"USERDATA.txt",0, User.class);
        /**
         * bottomNavigation 设置
         */

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        /** 导航基础设置 包括按钮选中效果 导航栏背景色等 */
        bottomNavigationBar
                .setTabSelectedListener(this)
                .setMode(BottomNavigationBar.MODE_FIXED)
                /**
                 *  setMode() 内的参数有三种模式类型：
                 *  MODE_DEFAULT 自动模式：导航栏Item的个数<=3 用 MODE_FIXED 模式，否则用 MODE_SHIFTING 模式
                 *  MODE_FIXED 固定模式：未选中的Item显示文字，无切换动画效果。
                 *  MODE_SHIFTING 切换模式：未选中的Item不显示文字，选中的显示文字，有切换动画效果。
                 */

                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                /**
                 *  setBackgroundStyle() 内的参数有三种样式
                 *  BACKGROUND_STYLE_DEFAULT: 默认样式 如果设置的Mode为MODE_FIXED，将使用BACKGROUND_STYLE_STATIC
                 *                                    如果Mode为MODE_SHIFTING将使用BACKGROUND_STYLE_RIPPLE。
                 *  BACKGROUND_STYLE_STATIC: 静态样式 点击无波纹效果
                 *  BACKGROUND_STYLE_RIPPLE: 波纹样式 点击有波纹效果
                 */
                .setActiveColor("#000000") //选中颜色
                .setInActiveColor("#6D6969") //未选中颜色
                .setBarBackgroundColor("#FFFFFF");//导航栏背景色

        /** 添加导航按钮 */
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.home, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.fenlei, "分类"))
                .addItem(new BottomNavigationItem(R.drawable.personal, "个人设置"))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise(); //initialise 一定要放在 所有设置的最后一项

        setDefaultFragment();//设置默认导航栏

    }

    //------------手势--------------
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        FragmentManager fm = this.getFragmentManager();
//        //开启事务
//        FragmentTransaction transaction = fm.beginTransaction();
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            //当手指按下的时候
//            x1 = event.getX();
//            y1 = event.getY();
//        }
//        if(event.getAction() == MotionEvent.ACTION_UP) {
//            //当手指离开的时候
//            x2 = event.getX();
//            y2 = event.getY();
//            if(y1 - y2 > 50) {
//                Toast.makeText(MainActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
//            } else if(y2 - y1 > 50) {
//                Toast.makeText(MainActivity.this, "向下滑", Toast.LENGTH_SHORT).show();
//            } else if(x1 - x2 > 50) {
//                if (mScanFragment == null) {
//                    mScanFragment = ScanFragment.newInstance("扫一扫");
//                }
//                transaction.replace(R.id.tb, mScanFragment);
//                transaction.commit();
//                Toast.makeText(MainActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
//            } else if(x2 - x1 > 50) {
//                Toast.makeText(MainActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
//            }
//        }
//        return super.onTouchEvent(event);
//    }


    /**
     * 设置默认导航栏
     */
    private void setDefaultFragment() {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction =  manager.beginTransaction();
        mHomeFragment = HomeFragment.newInstance();
        transaction.replace(R.id.tb,mHomeFragment);
        transaction.commit();
    }


    /**
     * 设置导航选中的事件
     */
    @Override
    public void onTabSelected(int position) {
        num = position;
        Log.d(TAG, "onTabSelected() called with: " + "position = [" + position + "]");
        FragmentManager manager=getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = manager.beginTransaction();
        switch (position) {
            case 0:
                Log.i(TAG, "onTabSelected: 我mhome被点击了啊");
                if (mHomeFragment == null) {
                    Log.i(TAG, "onTabSelected:进入");
                    mHomeFragment = HomeFragment.newInstance();
                }
                transaction.replace(R.id.tb,mHomeFragment);
                break;
            case 1:
                if (mScanFragment == null) {
                    mScanFragment = ScanFragment.newInstance("分类");
                    Log.i(TAG, "onTabSelected: 开始创建mscanfragment");
                }
                transaction.replace(R.id.tb,mScanFragment);
                break;
            case 2:
                if (mMyFragment == null) {
                    mMyFragment = MyFragment.newInstance("个人");
                }
                transaction.replace(R.id.tb,mMyFragment);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 设置未选中Fragment 事务
     */
    @Override
    public void onTabUnselected(int position) {

    }

    /**
     * 设置释放Fragment 事务
     */
    @Override
    public void onTabReselected(int position) {

    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System .currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
           if (mHomeFragment.isFlag()) {
               mHomeFragment.exit();
               }

            } else {
                //如果不需要拉出顶部的header，直接关闭当前的界面
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case HomeFragment.POSTDETAILS:
                Log.i(TAG, "onActivityResult: 开始了");
                mHomeFragment.updateInfo(data);
                break;
        }
    }
    private void hideFragment(android.support.v4.app.FragmentTransaction transaction){
        if(mHomeFragment!=null){
            Log.i(TAG, "hideFragment: 隐藏home");
            transaction.hide(mHomeFragment);
        }
        if(mScanFragment!=null){
            Log.i(TAG, "hideFragment: 隐藏scan");
            transaction.hide(mScanFragment);
        }
        if(mMyFragment!=null){
            Log.i(TAG, "hideFragment: 隐藏my");
            transaction.hide(mMyFragment);
        }

    }

}
