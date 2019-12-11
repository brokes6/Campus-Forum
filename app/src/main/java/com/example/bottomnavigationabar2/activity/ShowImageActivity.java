package com.example.bottomnavigationabar2.activity;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.bottomnavigationabar2.Animation.DepthPageTransformer;
import com.example.bottomnavigationabar2.Animation.ZoomOutPageTransformer;
import com.example.bottomnavigationabar2.Pictureutils.LocalCacheUtils;
import com.example.bottomnavigationabar2.Pictureutils.MemoryCacheUtils;
import com.example.bottomnavigationabar2.Post;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.NineGridTest2Adapter;
import com.example.bottomnavigationabar2.adapter.ShowImageAdapter;
import com.example.bottomnavigationabar2.rewrite.ZoomImageView;
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
    private ImageView back,button_images;
    private Bitmap localbitmap;
    private LinearLayout bottom;
    private Bitmap memorymbitmap;
    private ShowImageAdapter imageAdapter;
    private List<Post> mList=new ArrayList<>();
    private ArrayList<String> urls =null;
    private ArrayList<Boolean> booleans;
    private int position,total;
    private LinearLayout bottom_text;
    private LinearLayout Open_and_Retract;
    private TextView Picture_text,button_text;
    private ScrollView Picture_text_main;
    private Boolean Picture_key;
    private RelativeLayout.LayoutParams linearParams;
    private LayoutTransition transition;
    //子线程不能操作UI，通过Handler设置图片
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    Bitmap bitmap= (Bitmap) msg.obj;
                    SubsamplingScaleImageView iv = (SubsamplingScaleImageView) listViews.get(msg.arg1).findViewById(R.id.view_image);//绑定布局中的id/
                    iv.setImage(ImageSource.bitmap(bitmap));
//                    ZoomImageView iv = (ZoomImageView) listViews.get(msg.arg1).findViewById(R.id.view_image);//绑定布局中的id/
//                    iv.setImageBitmap(bitmap);
                    //取消加载动画
                    viewp.setVisibility(View.VISIBLE);
                    lin.setVisibility(View.GONE);
                    //
                    break;
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
        viewp.setVisibility(View.GONE);
        lin.setVisibility(View.VISIBLE);
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
        Picture_key=false;
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        transition = new LayoutTransition();
        container.setLayoutTransition(transition);
        transition = container.getLayoutTransition();
        transition.enableTransitionType(LayoutTransition.CHANGING);
        Picture_text_main = findViewById(R.id.Picture_text_main);
        button_images = findViewById(R.id.button_images);
        button_text = findViewById(R.id.button_text);
        bottom = findViewById(R.id.bottom);
        Picture_text = findViewById(R.id.Picture_text);
        Open_and_Retract = findViewById(R.id.Open_and_Retract);
        viewPager =findViewById(R.id.show_view_pager);
        //将设置好的动画指定给它
        viewPager.setPageTransformer(true, new DepthPageTransformer());
//        picture_text = findViewById(R.id.picture_text);
        picture_num=findViewById(R.id.picture_num);
    }
    private void initData(){
        Picture_text_main.getBackground().mutate().setAlpha(100);
        bottom.getBackground().mutate().setAlpha(100);
        Bundle bundle=getIntent().getExtras();
        total=bundle.getInt("total",0);
        listViews=new ArrayList<>();
        position=bundle.getInt("id",0);
        picture_num.setText(position+1+"/"+total);
//      picture_text.setText(bundle.getString("content","没有文字喔"));
//      expTv1.setText(bundle.getString("content","没有文字喔"));
        Picture_text.setText(bundle.getString("content","没有文字喔"));
        Log.i(TAG, "initData: postition="+position);
        Open_and_Retract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Picture_key==true){
                    button_text.setText("收起");
                    button_images.setImageResource(R.drawable.retract);
                    linearParams =(RelativeLayout.LayoutParams) Picture_text_main.getLayoutParams();
                    linearParams.height=520;
                    Picture_text_main.setLayoutParams(linearParams);
                    Picture_key=false;
                }else{
                    button_text.setText("展开");
                    button_images.setImageResource(R.drawable.open);
                    linearParams =(RelativeLayout.LayoutParams) Picture_text_main.getLayoutParams();
                    linearParams.height=0;
                    Picture_text_main.setLayoutParams(linearParams);
                    Picture_key=true;
                }

            }
        });
    }
    private void inint() {
        if (urls != null && urls.size() > 0){
            for (int i = 0; i < urls.size(); i++) {  //for循环将试图添加到list中
                View view = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.view_pager_item, null);   //绑定viewpager的item布局文件
//                ImageView iv = (ImageView) view.findViewById(R.id.view_image);   //绑定布局中的id
//                iv.setImageBitmap(urls.get(i));   //设置当前点击的图片
                setImageURL(urls.get(i),i);
                listViews.add(view);
                /**
                 * 图片的长按监听
                 * */
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
    public void setImageURL(final String path, final int index) {
        //从本地获取图片
        //从内存中获取图片；
        memorymbitmap = mMemoryCacheUtils.getBitmapFromMemory(path);
        if(memorymbitmap==null){
            localbitmap = mLocalCacheUtils.getBitmapFromLocal(path);
            if(localbitmap==null){
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            //把传过来的路径转成URL
                            URL url = new URL(path);
                            //获取连接
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            //使用GET方法访问网络
                            connection.setRequestMethod("GET");
                            //超时时间为10秒
                            connection.setConnectTimeout(10000);
                            //获取返回码
                            int code = connection.getResponseCode();
                            //当返回码是200，代表获取成功
                            if (code == 200) {
                                InputStream inputStream = connection.getInputStream();
                                //使用工厂把网络的输入流生产Bitmap
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                //利用Message把图片发给Handler
                                Message msg = Message.obtain();
                                msg.obj = bitmap;
                                msg.what = GET_DATA_SUCCESS;
                                msg.arg1=index;
                                //--将图片缓存进内存和本地--
                                mLocalCacheUtils.setBitmapToLocal(path,bitmap);
                                //将图片缓存进内存
                                mMemoryCacheUtils.setBitmapToMemory(path, bitmap);
                                handler.sendMessage(msg);
                                inputStream.close();
                            }else {
                                //服务启发生错误
                                handler.sendEmptyMessage(SERVER_ERROR);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            //网络连接错误
                            handler.sendEmptyMessage(NETWORK_ERROR);
                        }
                    }
                }.start();
            }else{
                Message msg = Message.obtain();
                msg.obj = localbitmap;
                msg.what = GET_DATA_SUCCESS;
                msg.arg1=index;
                handler.sendMessage(msg);
            }
        }else{
            Message msg = Message.obtain();
            msg.obj = memorymbitmap;
            msg.what = GET_DATA_SUCCESS;
            msg.arg1=index;
            handler.sendMessage(msg);
        }
        //开启一个线程用于联网

    }

}
