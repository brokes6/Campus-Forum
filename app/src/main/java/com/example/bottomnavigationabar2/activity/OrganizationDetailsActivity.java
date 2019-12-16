package com.example.bottomnavigationabar2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.UserData;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.MyImageView;
import com.example.bottomnavigationabar2.OrganizationActivity;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.bean.Organization;
import com.example.bottomnavigationabar2.bean.User;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrganizationDetailsActivity extends AppCompatActivity {
    private static final int HANDLER_DATA=1;
    private static final int FOLLOW=2;
    private static final String TAG = "OrganizationDetailsActi";
    public static final String ORGANIZATION_DETAILS_URL="http://106.54.134.17/app/findOrganizationById";
    public static final String FOLLOW_URL="http://106.54.134.17/app/follow";
    private RecyclerView recyclerView;
    private TextView organizationName;
    private MyImageView organizationImg;
    private TextView followNum;
    private TextView postNum;
    private TextView introduce;
    private int type;
    private Button followButton,enter;
    private int organizationId;
    private User userData;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: 我设置了啊");
            switch (msg.what){
                case HANDLER_DATA:
                    final Organization organization= (Organization) msg.obj;
                    //后面修改
//                    type=organization.getCollection();
                    organizationName.setText(organization.getOname());
                    organizationImg.setImageURL(organization.getOimg());
                    followNum.setText(String.valueOf(organization.getLoveNum()));
                    postNum.setText(String.valueOf(organization.getPnum()));
                    introduce.setText(organization.getOintroduce());
                    Log.i(TAG, "handleMessage: 我设置了啊");
                    type=organization.getCollection();
                    switch (type){
                        case 0:followButton.setText("未关注");
                                break;
                        case 1:followButton.setText("已关注");
                                break;
                        default:break;
                    }
                    enter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(OrganizationDetailsActivity.this,OrganizationActivity.class);
                            intent.putExtra("oid",organization.getOid());
                            intent.putExtra("oname",organization.getOname());
                            intent.putExtra("oimg",organization.getOimg());
                            startActivity(intent);
                            finish();
                        }
                    });
                    break;
                case FOLLOW:
                    Toast.makeText(OrganizationDetailsActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    switch (type){
                        case 0:followButton.setText("未关注");
                            break;
                        case 1:followButton.setText("已关注");
                            break;
                        default:break;
                    }
                    break;
                default:break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_information);
        initView();
        initData();
    }
    private void initView(){
        organizationName=findViewById(R.id.organizationName);
        organizationImg=findViewById(R.id.organizationImg);
        followButton=findViewById(R.id.followButton);
        followNum=findViewById(R.id.followNum);
        postNum=findViewById(R.id.postNum);
        introduce=findViewById(R.id.introduce);
        enter=findViewById(R.id.enter);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=type==0?1:0;
                follow(userData.getToken(),organizationId,type);
            }
        });
    }
    private void initData(){
        userData= FileCacheUtil.getUser(this);
        Intent data=getIntent();
        organizationId=data.getIntExtra("oid",-1);
        getOrganizationDetails();
    }
    public void getOrganizationDetails(){
        new Thread(){
            @Override
            public void run() {
                final Request request = new Request.Builder()
                        .url(ORGANIZATION_DETAILS_URL + "?organizationId=" + organizationId+"&token="+userData.getToken())
                        .build();
                final OkHttpClient okHttpClient=new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody=response.body().string();
                        Log.i(TAG, "onResponse: "+responseBody);
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(responseBody);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            int code=jsonObject.getInt("code");
                            if(code==0){
                                return;
                            }
                            Gson gson=new Gson();
                            Organization organization=gson.fromJson(jsonObject.getString("data"),new TypeToken<Organization>(){}.getType());
                            Log.i(TAG, "onResponse:"+organization.getOname());
                            Message message=new Message();
                            message.what=HANDLER_DATA;
                            message.obj=organization;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
        
    }
    public void follow(final String token, final int id, final int type){
        new Thread(){
            @Override
            public void run() {
                final Request request = new Request.Builder()
                        .url(FOLLOW_URL + "?token=" +token+"&tid="+id+"&type="+type)
                        .build();
                OkHttpClient okHttpClient=new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData=response.body().string();
                        Log.i(TAG, "onResponse:"+responseData);
                        try {
                            Message message=new Message();
                            JSONObject jsonObject=new JSONObject(responseData);
                            message.arg1=jsonObject.getInt("code");
                            message.obj=jsonObject.getString("msg");
                            message.what=FOLLOW;
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
