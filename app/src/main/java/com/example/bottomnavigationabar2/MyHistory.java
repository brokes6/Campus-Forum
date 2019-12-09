package com.example.bottomnavigationabar2;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.UserData;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.adapter.HistoryAdapter;
import com.example.bottomnavigationabar2.bean.User;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;
import com.example.bottomnavigationabar2.utils.PostHitoryUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyHistory extends AppCompatActivity {
    public static final int HANDLER_DATA=1;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private static final String TAG = "MyHistory";
    private User userData;
    private ImageView historyback;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HANDLER_DATA:
                    adapter.notifyDataSetChanged();
                    break;

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
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout= findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        historyback=findViewById(R.id.history_back);
        historyback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initData(){
        userData= FileCacheUtil.getUser(MyHistory.this);
        adapter=new HistoryAdapter(MyHistory.this);
        recyclerView.setAdapter(adapter);
        findHistoryIdDetails();
    }
    private void findHistoryIdDetails(){
        String data= PostHitoryUtil.getSearchHistory(MyHistory.this);
        if(data.trim().equals("")){
            Toast.makeText(this, "你还没有浏览历史喔", Toast.LENGTH_SHORT).show();
            return;
        }
        Request request=new Request.Builder()
                .url("http://106.54.134.17/app/findPostsById?listStr="+data+"&token="+userData.getToken())
                .build();
        OkHttpClient client=new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Log.i(TAG, "onResponse: "+responseData);
                try {
                    JSONObject jsonObject=new JSONObject(responseData);
                    int code=jsonObject.getInt("code");
                    if(code==0){
                        return;
                    }
                    Gson gson=new Gson();
                    List<Post> posts=gson.fromJson(jsonObject.getString("data"),new TypeToken<List<Post>>(){}.getType());
                    adapter.setPosts(posts);
                    Message message=new Message();
                    message.what=HANDLER_DATA;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

