package com.example.bottomnavigationabar2.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.ShowImageAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ShowImageActivity extends AppCompatActivity {
    private static final String TAG = "ShowImageActivity";
    private ViewPager viewPager;
    private List<View>  listViews =null;
    private int index=0;
    private ShowImageAdapter imageAdapter;
    private ArrayList<Bitmap> bitmaps =null;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_image_layout);
        initView();
        initData();
    }
    private void initView(){
        viewPager =findViewById(R.id.show_view_pager);
    }
    private void initData(){
        listViews=new ArrayList<>();
        position=getIntent().getIntExtra("id",0);
        Log.i(TAG, "initData: postition="+position);
    }
    private void inint() {
        if (bitmaps != null && bitmaps.size() > 0){
            for (int i = 0; i < bitmaps.size(); i++) {  //for循环将试图添加到list中
                View view = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.view_pager_item, null);   //绑定viewpager的item布局文件
//                ImageView iv = (ImageView) view.findViewById(R.id.view_image);   //绑定布局中的id
//                iv.setImageBitmap(bitmaps.get(i));   //设置当前点击的图片

                SubsamplingScaleImageView iv = (SubsamplingScaleImageView) view.findViewById(R.id.view_image);   //绑定布局中的id
                iv.setImage(ImageSource.bitmap(bitmaps.get(i)));
                listViews.add(view);
                /**
                 * 图片的长按监听
                 * */
                iv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //弹出提示，提示内容为当前的图片位置
                        Toast.makeText(ShowImageActivity.this, "这是第" + (index + 1) + "图片", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }
            imageAdapter = new ShowImageAdapter(listViews);
            viewPager.setAdapter(imageAdapter);
            viewPager.setOnPageChangeListener(new PageChangeListener()); //设置viewpager的改变监听
            //截取intent获取传递的值
            viewPager.setCurrentItem(position);    //viewpager显示指定的位置
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEventMainThread(Object o) {
        bitmaps = (ArrayList<Bitmap>)o;
        int byteCount = bitmaps.get(0).getByteCount();
        inint();   //初始化
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            index = arg0;
        }

    }
}
