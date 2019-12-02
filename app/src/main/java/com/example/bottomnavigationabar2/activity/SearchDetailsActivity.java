package com.example.bottomnavigationabar2.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bottomnavigationabar2.MoBan.OrganizationFollowTemplate;
import com.example.bottomnavigationabar2.MoBan.OrganizationRecommendTemplate;
import com.example.bottomnavigationabar2.MoBan.PostTemplateInterface;
import com.example.bottomnavigationabar2.MoBan.SearchPostTemplate;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.MainTabFragmentAdapter;
import com.example.bottomnavigationabar2.bean.SearchDto;
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
    public static final String SEARCH_REQUEST_URL="http://10.0.2.2:8080/app/search";
    private static final String TAG = "SearchDetailsActivity";
    private MainTabFragmentAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String queryWord;
    private SearchPostTemplate postTemplate;
    private OrganizationRecommendTemplate recommendTemplate;
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
        initView();
        initData();
        getSearchData();
    }
    private void initView(){
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
    }
    private void initData(){
        queryWord=getIntent().getStringExtra("queryWord");
        adapter=new MainTabFragmentAdapter(getSupportFragmentManager(),this,false,-1,2);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
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
                            Log.i(TAG, "onResponse: 没脑子为什么那么块？");
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
}
