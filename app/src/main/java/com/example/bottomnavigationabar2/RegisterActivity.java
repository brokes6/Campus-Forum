package com.example.bottomnavigationabar2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
    private static final String TAG = "RegisterActivity";
    String regex1 = "[a-zA-Z0-9_]*@[a-zA-Z0-9]+[.][a-zA-Z0-9]+";
    String Data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        final EditText name_denglu = (EditText)findViewById(R.id.name_denglu);
        final EditText username_denglu = (EditText)findViewById(R.id.username_denglu);
        final EditText pass_denglu = (EditText)findViewById(R.id.password_denglu);
        final EditText pass_dengli_1 = (EditText)findViewById(R.id.password_again);
        final EditText email = (EditText)findViewById(R.id.youxiang);
        Button button_denglu = (Button)findViewById(R.id.button_denglu);
        button_denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String email_1 = email.getText().toString();
                String userpass_1 = pass_denglu.getText().toString();
                String pass_again = pass_dengli_1.getText().toString();
                boolean youxiang = email_1.matches(regex1);
                boolean mima = userpass_1.equals(pass_again);
                if (email_1.matches(regex1)){
                }
                else{
                    Toast.makeText(RegisterActivity.this,"邮箱格式不对",Toast.LENGTH_SHORT).show();
                }
                if (userpass_1.equals(pass_again)) {

                }else{
                    Toast.makeText(RegisterActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                }
                if(youxiang == true &&mima == true){
                   Log.d(TAG, "为"+mima);
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
                    name_denglu = (EditText)findViewById(R.id.name_denglu);
                    username_denglu = (EditText)findViewById(R.id.username_denglu);
                    pass_dengli_1 = (EditText)findViewById(R.id.password_again);
                    email = (EditText)findViewById(R.id.youxiang);
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
                        System.out.println("注册成功！");
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
