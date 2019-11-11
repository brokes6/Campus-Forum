package com.example.bottomnavigationabar2;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bottomnavigationabar2.bean.ResultBean;
import com.example.util.JsonTOBeanUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    String data_user =null;
    ResultBean resultBean;
    EditText name_denglu;
    EditText username_denglu;
    EditText pass_dengli_1;
    EditText email;
    private Context mContext;
    private Activity mActivity;
    private static final String TAG = "RegisterActivity";
    String regex1 = "[a-zA-Z0-9_]*@[a-zA-Z0-9]+[.][a-zA-Z0-9]+";
    String Data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //用于屏蔽系统的头部，（现已无用）
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        final EditText name_denglu = (EditText)findViewById(R.id.ret_name);
        final EditText username_denglu = (EditText)findViewById(R.id.ret_username);
        final EditText pass_denglu = (EditText)findViewById(R.id.ret_password);
        final EditText email = (EditText)findViewById(R.id.ret_email);
        Button button_denglu = (Button)findViewById(R.id.button_denglu);
        button_denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_denglu.getText().toString();
                String email_1 = email.getText().toString();
                boolean youxiang = email_1.matches(regex1);
                if (email_1.matches(regex1)){
                }
                else{
                    email.setError("邮箱格式错误");
                }
                if(youxiang == true){
                    sendOkHttp();
                }
            }
        });
        }
    private void sendOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    name_denglu = (EditText)findViewById(R.id.ret_name);
                    username_denglu = (EditText)findViewById(R.id.ret_username);
                    pass_dengli_1 = (EditText)findViewById(R.id.ret_password);
                    email = (EditText)findViewById(R.id.ret_email);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            //post请求
                            .add("username", name_denglu.getText().toString())
                            .add("account",username_denglu.getText().toString())
                            .add("password",pass_dengli_1.getText().toString())
                            .add("email",email.getText().toString())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.54.134.17/app/register")
                            .post(requestBody)
                            //服务器ip地址
                            .build();
                    Response shuju_jieshou = client.newCall(request).execute();
                    String responseData = shuju_jieshou.body().string();
                    System.out.println(responseData);
                    resultBean = JsonTOBeanUtil.getResultBean(responseData);
                    if(resultBean.getCode()==1){
                        data_user=name_denglu.getText().toString();
                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        intent.putExtra("username",data_user);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_out,R.anim.fade_in);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}
