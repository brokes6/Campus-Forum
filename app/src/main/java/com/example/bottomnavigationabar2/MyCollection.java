package com.example.bottomnavigationabar2;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.MoBan.RequestStatus;
import com.example.bottomnavigationabar2.adapter.HistoryAdapter;
import com.example.bottomnavigationabar2.bean.User;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;
import com.example.bottomnavigationabar2.utils.PostHitoryUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyCollection extends AppCompatActivity {
    private static final String TAG = "history";
    public static final int HANDLER_DATA=1;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private User userData;
    private TextView title;
    private LinearLayout loadLayout;
    private TextView loadingTextView;
    private ProgressBar progressBar;
    private Button loadButton;
    private LinearLayout Back;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestStatus.HANDLER_DATA:
                    recyclerView.setVisibility(View.VISIBLE);
                    loadLayout.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    break;
                case RequestStatus.NO_RESOURCE:
                     progressBar.setVisibility(View.GONE);
                     loadingTextView.setText("你暂时还没有收藏的文章喔，快去收藏吧！");
                     loadingTextView.setTextSize(18);
                case RequestStatus.NO_NETWORK:
                    progressBar.setVisibility(View.GONE);
                    loadingTextView.setText("网络连接失败，请重新尝试!");
                    loadingTextView.setTextSize(18);
                    loadButton.setVisibility(View.VISIBLE);


            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
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
    }
    private void initView(){
        Back=findViewById(R.id.back);
        loadButton=findViewById(R.id.loadButton);
        loadingTextView=findViewById(R.id.loadTextView);
        progressBar=findViewById(R.id.loading);
        loadLayout=findViewById(R.id.loadLayout);
        title=findViewById(R.id.titleTextView);
        title.setText("我的收藏");
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout= findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingTextView.setText("正在努力加载中");
                progressBar.setVisibility(View.VISIBLE);
                loadButton.setVisibility(View.GONE);
                findHistoryIdDetails();
            }
        });
    }
    private void initData(){
        userData= FileCacheUtil.getUser(this);
        adapter=new HistoryAdapter(this);
        recyclerView.setAdapter(adapter);
        findHistoryIdDetails();
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void findHistoryIdDetails(){
        final Request request=new Request.Builder()
                .url("http://106.54.134.17/app/findCollection?token="+userData.getToken())
                .build();
        OkHttpClient client=new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=new Message();
                message.what= RequestStatus.NO_NETWORK;
                handler.sendMessage(message);
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Log.i(TAG, "onResponse: "+responseData);
                try {
                    JSONObject jsonObject=new JSONObject(responseData);
                    int code=jsonObject.getInt("code");
                    if(code==0){
                        Message message=new Message();
                        message.what=RequestStatus.NO_RESOURCE;
                        handler.sendMessage(message);
                        return;
                    }
                    Gson gson=new Gson();
                    List<Post> posts=gson.fromJson(jsonObject.getString("data"),new TypeToken<List<Post>>(){}.getType());
                    adapter.setPosts(posts);
                    Message message=new Message();
                    message.what=RequestStatus.HANDLER_DATA;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}