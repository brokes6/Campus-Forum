package com.example.bottomnavigationabar2.MoBan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.OrganizationAdapter;
import com.example.bottomnavigationabar2.bean.Organization;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;
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

public class OrganizationFollowTemplate extends Fragment {
    private static final String TAG = "OrganizationFollowTempl";
    public static final String ORGANIZATION_REQUEST_URL = "http://106.54.134.17/app/getOrganizationByUserId";
    private OrganizationAdapter organizationAdapter;
    private RecyclerView recyclerView;
    private LinearLayout loadLayout;
    private Button loadButton;
    private TextView loadTextView;
    private ProgressBar progressBar;
    private View convertView;
    private int startPage=1;
    private String token= FileCacheUtil.getUser(getContext()).getToken();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestStatus.HANDLER_DATA:
                    organizationAdapter.notifyDataSetChanged();
                    loadLayout.setVisibility(View.GONE);
                    break;
                case RequestStatus.NO_RESOURCE:
                    handlerNoResource();
                    break;
                case RequestStatus.NO_NETWORK:
                    handlerNoNetWork();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.gz_layout, container, false);
        return convertView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        loadButton=convertView.findViewById(R.id.loadButton);
        progressBar=convertView.findViewById(R.id.loading);
        loadTextView=convertView.findViewById(R.id.loadTextView);
        loadLayout=convertView.findViewById(R.id.loadLayout);
        recyclerView=convertView.findViewById(R.id.recyclerView);
        organizationAdapter = new OrganizationAdapter(getContext(),1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.i(TAG, "initView: recyclerView="+recyclerView.getLayoutManager());
        recyclerView.setAdapter(organizationAdapter);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTextView.setText("正在努力加载中");
                progressBar.setVisibility(View.VISIBLE);
                loadButton.setVisibility(View.GONE);
                findFollowOrganization(token);
            }
        });
    }

    private void initData() {
        findFollowOrganization(token);
    }
    public void findFollowOrganization(String token) {
        final Request request = new Request.Builder()
                .url(ORGANIZATION_REQUEST_URL + "?startPage=" + startPage+"&token="+token)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=new Message();
                message.what= RequestStatus.NO_NETWORK;
                handler.sendMessage(message);
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.i(TAG, "onResponse: 社团返回结果" + responseBody);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Message message=new Message();
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
                    Message message = new Message();
                    message.what = RequestStatus.HANDLER_DATA;
                    message.obj = organizations;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public  void setData(List<Organization> list){
        this.organizationAdapter.setOrganizations(list);
    }
    private void handlerNoResource(){
        progressBar.setVisibility(View.GONE);
        loadTextView.setText("你暂时还没有关注的社团喔，快去关注社团吧!");
        loadTextView.setTextSize(16);
    }
    private void handlerNoNetWork(){
        progressBar.setVisibility(View.GONE);
        loadTextView.setText("网络连接失败，请重新尝试");
        loadTextView.setTextSize(16);
        loadButton.setVisibility(View.VISIBLE);
    }
}
