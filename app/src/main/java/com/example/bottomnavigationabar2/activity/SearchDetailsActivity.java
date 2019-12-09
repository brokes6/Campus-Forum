package com.example.bottomnavigationabar2.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.MoBan.OrganizationFollowTemplate;
import com.example.bottomnavigationabar2.MoBan.OrganizationRecommendTemplate;
import com.example.bottomnavigationabar2.MoBan.PostTemplateInterface;
import com.example.bottomnavigationabar2.MoBan.SearchPostTemplate;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.MainTabFragmentAdapter;
import com.example.bottomnavigationabar2.bean.SearchDto;
import com.example.bottomnavigationabar2.utils.HistorySearchUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchDetailsActivity extends AppCompatActivity {
    public static final String SEARCH_REQUEST_URL="http://106.54.134.17/app/search";
    private static final String TAG = "SearchDetailsActivity";
    private MainTabFragmentAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String queryWord;
    private SearchView searchView;
    private Button searchButton;
    private SearchPostTemplate postTemplate;
    private OrganizationRecommendTemplate recommendTemplate;
    private ImageView back;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    SearchDto searchDto= (SearchDto) msg.obj;
                    recommendTemplate.setData(searchDto.getOrganizationList());
                    postTemplate.setData(searchDto.getPostList());
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_details);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initView();
        initData();
        getSearchData();
    }
    private void initView(){
        searchButton=findViewById(R.id.button);
        searchView=findViewById(R.id.searchView);
        back = findViewById(R.id.search_details_back);
        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        searchView.setQueryHint("请输入搜索内容");
        searchView.setIconifiedByDefault(false);
        setSearchTvListener();
    }
    private void initData(){
        queryWord=getIntent().getStringExtra("queryWord");
        adapter=new MainTabFragmentAdapter(getSupportFragmentManager(),this,false,-1,2);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        recommendTemplate= (OrganizationRecommendTemplate) adapter.getFragments().get(0);
        postTemplate= (SearchPostTemplate) adapter.getFragments().get(1);
        Log.i(TAG, "initData: "+postTemplate);
    }
    public void getSearchData(){
        new Thread(){
            @Override
            public void run() {
                Log.i(TAG, "getSearchData: queryWord="+queryWord);
                final Request request = new Request.Builder()
                        .url(SEARCH_REQUEST_URL+"?queryWord="+queryWord)
                        .build();
                OkHttpClient okHttpClient=new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody=response.body().string();
                        Log.i(TAG, "onResponse:"+responseBody);
                        try {
                            JSONObject jsonObject=new JSONObject(responseBody);
                            int code=jsonObject.getInt("code");
                            if(code==0){
                                Log.i(TAG, "onResponse: "+jsonObject.getString("msg"));
                            }
                            Gson gson=new Gson();
                            SearchDto searchDto=gson.fromJson(jsonObject.getString("data"),new TypeToken<SearchDto>(){}.getType());
                            Message message=new Message();
                            message.what=1;
                            message.obj=searchDto;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }
    private void setSearchTvListener() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryWord=searchView.getQuery().toString();
                if (queryWord.isEmpty()){
                    Toast.makeText(SearchDetailsActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    HistorySearchUtil.getInstance(SearchDetailsActivity.this).putNewSearch(queryWord);//保存记录到数据库
                    adapter.notifyDataSetChanged();
                    searchView.setQuery("",false);
                    searchView.clearFocus();
                    clear();
                    getSearchData();
                }
            }
        });
    }
    private void clear(){
        recommendTemplate.clear();
        postTemplate.clear();
    }
}
