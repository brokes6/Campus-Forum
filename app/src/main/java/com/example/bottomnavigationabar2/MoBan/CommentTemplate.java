package com.example.bottomnavigationabar2.MoBan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.UserData;
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

import com.example.bottomnavigationabar2.HomeFragment;
import com.example.bottomnavigationabar2.Post;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.NineGridTest2Adapter;
import com.example.bottomnavigationabar2.adapter.PostAdapter;
import com.example.bottomnavigationabar2.bean.UserMessage;
import com.example.bottomnavigationabar2.dto.CommentDto;
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

import static android.support.constraint.Constraints.TAG;

public class CommentTemplate extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PostAdapter postAdapter;
    private LinearLayout loadLayout;
    private ProgressBar progressBar;
    private TextView loadTextView;
    private Button loadButton;
    private List<UserMessage> userMessages = new ArrayList<>();
    private View view;
    private int startPage=1;
    public volatile static boolean flag=true;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RequestStatus.HANDLER_DATA:
                    loadLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    postAdapter.notifyDataSetChanged();
                    break;
                case RequestStatus.NO_RESOURCE:
                    loadTextView.setText("你暂时还没有消息喔~");
                    loadTextView.setTextSize(18);
                    break;
                case RequestStatus.NO_NETWORK:
                    progressBar.setVisibility(View.GONE);
                    loadTextView.setText("网络连接失败，请重新尝试!");
                    loadTextView.setTextSize(18);
                    loadButton.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ------------");
        view = inflater.inflate(R.layout.mo_ban_1, container, false);
        initView();
        getMessage(HomeFragment.userData.getToken());
        return view;
    }
    private void initView() {
        loadButton=view.findViewById(R.id.loadButton);
        mRecyclerView =view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        postAdapter = new PostAdapter(getContext());
        postAdapter.setUserMessages(userMessages);
        mRecyclerView.setAdapter(postAdapter);
        loadLayout=view.findViewById(R.id.loadLayout);
        loadTextView=view.findViewById(R.id.loadTextView);
        progressBar=view.findViewById(R.id.loading);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                loadButton.setVisibility(View.GONE);
                getMessage(HomeFragment.userData.getToken());
            }
        });
    }
    public void getMessage(String token){
        Request request =new Request.Builder()
                .url("http://106.54.134.17/app/pushUserMessage?token="+token+"&startPage="+startPage)
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
                String reponseData=response.body().string();
                Log.i(TAG, "onResponse: responsedata="+reponseData);
                try {
                    JSONObject jsonObject=new JSONObject(reponseData);
                    int code = jsonObject.getInt("code");
                    if(code==0){
                        Message message=new Message();
                        message.what=RequestStatus.NO_RESOURCE;
                        handler.sendMessage(message);
                    }
                    Gson gson=new Gson();
                    userMessages=gson.fromJson(jsonObject.getString("data"),new TypeToken<List<UserMessage>>(){}.getType());
                    postAdapter.setUserMessages(userMessages);
                    Message message=new Message();
                    message.what=RequestStatus.HANDLER_DATA;
                    startPage++;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
