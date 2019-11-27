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
    public static final String ORGANIZATION_REQUEST_URL = "http://10.0.2.2:8080/app/getPopularOrganization";
    private OrganizationAdapter organizationAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private View convertView;
    private int startPage=1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.i(TAG, "handleMessage: 提醒要改变拉");
                        organizationAdapter.notifyDataSetChanged();
                        break;

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.tj_layout, container, false);
        initView();
        initData();
        return convertView;
    }

    private void initView() {
        recyclerView=convertView.findViewById(R.id.recyclerView);
        organizationAdapter = new OrganizationAdapter(getContext(),0);
        linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(organizationAdapter);
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
                e.printStackTrace();
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
                        Log.i(TAG, "onResponse: 失败了");
                    }
                    Gson gson = new Gson();
                    String data = jsonObject.getString("data");
                    List<Organization> organizations = gson.fromJson(data, new TypeToken<List<Organization>>() {
                    }.getType());
                    organizationAdapter.setOrganizations(organizations);
                    startPage++;
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
