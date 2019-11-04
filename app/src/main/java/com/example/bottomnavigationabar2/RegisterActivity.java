package com.example.bottomnavigationabar2;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
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


import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    ResultBean resultBean;
    private EditText usernameEdit;
    private EditText accountEdit;
    private EditText passwordEdit;
    private EditText password_againEdit;
    private EditText emailEdit;
    private static final String TAG = "RegisterActivity";
    Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //用于屏蔽系统的头部，（现已无用）
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        usernameEdit= (EditText)findViewById(R.id.usernameEdit);
        accountEdit = (EditText)findViewById(R.id.accountEdit);
        passwordEdit = (EditText)findViewById(R.id.passwordEdit);
        password_againEdit=findViewById(R.id.password_againEdit);
        emailEdit = (EditText)findViewById(R.id.emailEdit);
        Button button_denglu = (Button)findViewById(R.id.button_denglu);
        button_denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameEdit.getText().toString();
                String account=accountEdit.getText().toString();
                String password=passwordEdit.getText().toString();
                String pass_again=password_againEdit.getText().toString();
                String email=emailEdit.getText().toString();
                boolean emailFlag = pattern.matcher(email).matches();
                boolean passwordFlag = password.equals(pass_again);
                if((TextUtils.isEmpty(username)||TextUtils.isEmpty(account)||TextUtils.isEmpty(password)||TextUtils.isEmpty(pass_again)||TextUtils.isEmpty(email))){
                    Toast.makeText(RegisterActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
/*                if (!emailFlag){
                    Toast.makeText(RegisterActivity.this,"邮箱格式不对",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if (passwordFlag) {
                    Toast.makeText(RegisterActivity.this,"请检查密码输入，密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }
                sendOkHttp(account,username,password,email);
            }
        });
        }
    private void sendOkHttp(final String account, final String username, final String password, final String email){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            //post请求
                            .add("username",username)
                            .add("account",account)
                            .add("password",password)
                            .add("email",email)
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
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        intent.putExtra("account",account);
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
