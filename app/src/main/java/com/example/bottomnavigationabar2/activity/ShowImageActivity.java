package com.example.bottomnavigationabar2.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.bottomnavigationabar2.Animation.DepthPageTransformer;
import com.example.bottomnavigationabar2.Animation.ZoomOutPageTransformer;
import com.example.bottomnavigationabar2.MyImageView;
import com.example.bottomnavigationabar2.Pictureutils.LocalCacheUtils;
import com.example.bottomnavigationabar2.Pictureutils.MemoryCacheUtils;
import com.example.bottomnavigationabar2.Post;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.NineGridTest2Adapter;
import com.example.bottomnavigationabar2.adapter.ShowImageAdapter;
import com.example.bottomnavigationabar2.rewrite.ZoomImageView;
import com.example.util.ImageUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowImageActivity extends AppCompatActivity {
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    //---两个缓存方法----
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;
    //----
    private static final String TAG = "ShowImageActivity";
    private ViewPager viewPager;
    private TextView picture_text;
    private TextView picture_num;
    private LinearLayout lin;
    private android.support.v4.view.ViewPager  viewp;
    private List<View>  listViews =null;
    private int index=0;
    private ImageView back;
    private Bitmap localbitmap;
    private Bitmap memorymbitmap;
    private ShowImageAdapter imageAdapter;
    private List<Post> mList=new ArrayList<>();
    private ArrayList<String> urls =null;
    private ArrayList<Boolean> booleans;
    private int position,total;
    private LinearLayout bottom_text;
    private ExpandableTextView expTv1;
    //子线程不能操作UI，通过Handler设置图片
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
//                case GET_DATA_SUCCESS:
//                    Bitmap bitmap= (Bitmap) msg.obj;
//                    break;
                case NETWORK_ERROR:
                    Toast.makeText(ShowImageActivity.this,"网络连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(ShowImageActivity.this,"服务器发生错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public void setList(List<Post> list) {
        mList.addAll(list);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_image_layout_text);
        initView();
        initData();
        back = findViewById(R.id.title_back);
        lin = findViewById(R.id.lin_go);
        viewp = findViewById(R.id.show_view_pager);
        //设置加载动画
        //
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mMemoryCacheUtils = MemoryCacheUtils.getInstance();
        mLocalCacheUtils = LocalCacheUtils.getInstance();
    }
    private void initView(){
        bottom_text = findViewById(R.id.bottom_text);
        expTv1 = findViewById(R.id.expand_text_view);
        viewPager =findViewById(R.id.show_view_pager);
        //将设置好的动画指定给它
        viewPager.setPageTransformer(true, new DepthPageTransformer());
//        picture_text = findViewById(R.id.picture_text);
        picture_num=findViewById(R.id.picture_num);
    }
    private void initData(){
        bottom_text.getBackground().mutate().setAlpha(100);
        Bundle bundle=getIntent().getExtras();
        total=bundle.getInt("total",0);
        listViews=new ArrayList<>();
        position=bundle.getInt("id",0);
        picture_num.setText(position+1+"/"+total);
//        picture_text.setText(bundle.getString("content","没有文字喔"));
        expTv1.setText(bundle.getString("content","没有文字喔"));
    }
    private void inint() {
        if (urls != null && urls.size() > 0){
            for (int i = 0; i < urls.size(); i++) {  //for循环将试图添加到list中
                View view = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.view_pager_item, null);   //绑定viewpager的item布局文件
//                ImageView iv = (ImageView) view.findViewById(R.id.view_image);   //绑定布局中的id
//                iv.setImageBitmap(urls.get(i));   //设置当前点击的图片
                listViews.add(view);
                String path=urls.get(i);
                switch (ImageUtils.handlerImagePath(path)){
                    case ImageUtils.JPEG:
                        setImageURL(path,i);
                        break;
                    case ImageUtils.GIF:
                        setGifUrl(path,i);
                        break;
                }
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
        urls = (ArrayList<String>)o;
        booleans=new ArrayList<>(urls.size());
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
            Log.i(TAG, "onPageScrollStateChanged: 滚动啊");
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG, "onPageSelected: 冲啊");
            picture_num.setText(position+1+"/"+total);
//            boolean b = booleans.get(position);
//            if(!b){
//                ProgressBar progressBar=new ProgressBar();
//            }
            index = position;
        }

    }
    //设置网络图片（从网络中获取图片）
    public void setImageURL(String path,int index) {
        Log.i(TAG, "setImageURL: path="+path);
        final SubsamplingScaleImageView imageView =listViews.get(index).findViewById(R.id.view_image);//绑定布局中的id/
        Glide.with(ShowImageActivity.this).load(path).into(new CustomViewTarget<SubsamplingScaleImageView,Drawable>(imageView) {
            @Override
            protected void onResourceCleared(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                Log.i(TAG, "onLoadFailed: 图片加载失败"+errorDrawable.toString());
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Log.i(TAG, "onResourceReady: 加载成功！");
                imageView.setImage(ImageSource.bitmap(ImageUtils.drawableToBitmap(resource)));
                viewp.setVisibility(View.VISIBLE);
                lin.setVisibility(View.GONE);
            }
        });
    }
    private void setGifUrl(String path,int index){
        Log.i(TAG, "setGifURL: path="+path);
        final ImageView imageView =listViews.get(index).findViewById(R.id.gifView);//绑定布局中的id/
        Glide.with(ShowImageActivity.this).asGif().load(path).into(imageView);
    }
}
