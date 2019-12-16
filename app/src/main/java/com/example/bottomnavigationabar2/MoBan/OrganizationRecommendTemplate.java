package com.example.bottomnavigationabar2.MoBan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bottomnavigationabar2.MainActivity;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.OrganizationAdapter;
import com.example.bottomnavigationabar2.bean.Organization;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrganizationRecommendTemplate extends Fragment {
    private static final String TAG = "OrganizatioinFollowTemp";
    public static final String ORGANIZATION_REQUEST_URL = "http://106.54.134.17/app/getPopularOrganization";
    private OrganizationAdapter organizationAdapter;
    private List<Organization> organizations;
    private LinearLayout contentLayout;
    private LinearLayout loadLayout;
    private ProgressBar progressBar;
    private TextView loadTextView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private View convertView;
    private Button button;
    private Button refreshButton;
    private int startPage=1;
    private boolean initData=true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestStatus.HANDLER_DATA:
                        contentLayout.setVisibility(View.VISIBLE);
                        loadLayout.setVisibility(View.GONE);
                        organizationAdapter.notifyDataSetChanged();
                        break;
                case RequestStatus.NO_RESOURCE:
                        handlerNoResource();
                        break;
                case RequestStatus.NO_NETWORK:
                        handlerNoNetwork();
                        break;
            }
        }
    };
    public static OrganizationRecommendTemplate newIntance(boolean initData){
        OrganizationRecommendTemplate template=new OrganizationRecommendTemplate();
        Bundle bundle=new Bundle();
        bundle.putBoolean("initData",initData);
        template.setArguments(bundle);
        return template;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null)
            initData=bundle.getBoolean("initData");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.tj_layout, container, false);
        initView();
        return convertView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(initData)
            initData();
    }

    private void initView() {
        recyclerView=convertView.findViewById(R.id.recyclerView);
        button=convertView.findViewById(R.id.loadButton);
        organizationAdapter=new OrganizationAdapter(getContext(),0);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(organizationAdapter);
        loadLayout=convertView.findViewById(R.id.loadLayout);
        contentLayout=convertView.findViewById(R.id.contentLayout);
        loadTextView=convertView.findViewById(R.id.loadTextView);
        progressBar=convertView.findViewById(R.id.loading);
        refreshButton=convertView.findViewById(R.id.refresh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTextView.setText("正在努力加载中");
                progressBar.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                loadTextView.setText("正在努力加载中...");
                findRecommendOrganization();
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findRecommendOrganization();
            }
        });
    }

    private void initData() {
        findRecommendOrganization();
    }

    private void findRecommendOrganization() {
        Log.i(TAG, "findRecommendOrganization: 你说我有请求");
        final Request request = new Request.Builder()
                .url(ORGANIZATION_REQUEST_URL + "?startPage=" + startPage)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=new Message();
                message.what=RequestStatus.NO_NETWORK;
                handler.sendMessage(message);
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.i(TAG, "onResponse: 社团返回结果" + responseBody);
                JSONObject jsonObject = null;
                Message message = new Message();
                try {
                    jsonObject = new JSONObject(responseBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Log.i(TAG, "onResponse: 失败了");
                        message.what=RequestStatus.NO_RESOURCE;
                        handler.sendMessage(message);
                        return;
                    }
                    Gson gson = new Gson();
                    String data = jsonObject.getString("data");
                    List<Organization> organizations = gson.fromJson(data, new TypeToken<List<Organization>>() {
                    }.getType());
                    organizationAdapter.setOrganizations(organizations);
                    startPage++;
                    message.what =RequestStatus.HANDLER_DATA;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void setData(List<Organization>list){
        if(list!=null) {
            this.organizations=list;
            this.organizationAdapter.setOrganizations(list);
            organizationAdapter.notifyDataSetChanged();
        }else {
            handlerNoResource();
        }
    }
    public void handlerNoResource(){
        recyclerView.setVisibility(View.GONE);
        if(this.organizations!=null) {
            this.organizations.clear();
        }
        progressBar.setVisibility(View.GONE);
        loadTextView.setText("没有找到相关数据");
        loadTextView.setTextSize(20);
    }
    public void handlerNoNetwork(){
        progressBar.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
        loadTextView.setText("网络连接失败，请重新尝试。。。");
    }
    public void clear(){
        if(organizations!=null) {
            Log.i(TAG, "clear: 开始清空");
            organizations.clear();
            organizationAdapter.notifyDataSetChanged();
        }
    }
}
