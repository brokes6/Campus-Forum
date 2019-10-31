package com.example.bottomnavigationabar2;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.bean.ResultBean;
import static com.example.bottomnavigationabar2.utils.FileCacheUtil.getCache;
import com.example.bottomnavigationabar2.bean.User;
import com.example.util.JsonTOBeanUtil;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.bottomnavigationabar2.utils.FileCacheUtil.setCache;
import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
        EditText username;
        EditText password;
        String user = null;
        User userData;
        String pass = null;
        private Context mContext;
        private Activity mActivity;
        String token ="1";
        private CheckBox autologin;
        private CheckBox remembermima;
        private IntentFilter intentFilter;
        Boolean checkbox = false;
        private NetworkChangeReceiver networkChangeReceiver;
        private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        mContext = this;
        mActivity = this;
        //过度效果(没写)
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
        //获取记住密码，自动登录主键
        sp = getSharedPreferences("userInfo", 0);
        autologin = findViewById(R.id.jz_mima);
        remembermima = findViewById(R.id.zd_denglu);
        //获取账号 密码 登录按钮
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        Button denglu = (Button)findViewById(R.id.denglu);
        denglu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                sendOkHttp();
            }
        });
        //将账号密码 和sp值存入
        sp = getSharedPreferences("userInfo", 0);
        String name=sp.getString("USER_NAME", "");
        String pass =sp.getString("PASSWORD", "");
        //获取单选按钮的状态
        boolean choseRemember =sp.getBoolean("remember", false);
        boolean choseAutoLogin =sp.getBoolean("autologin", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            username.setText(name);
            password.setText(pass);
            autologin.setChecked(true);
        }
        if(choseAutoLogin){
            autologin.setChecked(true);
        }



        TextView textView = (TextView)findViewById(R.id.tex3);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent2);
            }
        });
        TextView textView1 = (TextView)findViewById(R.id.tex1);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(LoginActivity.this,FintoutPasswordActivity.class);
                startActivity(intent3);
            }
        });
    }

    private void sendOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取账号   密码
                    username = (EditText)findViewById(R.id.username);
                    password = (EditText)findViewById(R.id.password);
                    SharedPreferences.Editor editor =sp.edit();
                    SharedPreferences.Editor editor1 =sp.edit();
                    user = username.getText().toString();
                    pass = password.getText().toString();
                    if(user.equals("") &&pass.equals("")){
                        Toast.makeText(LoginActivity.this,"账号密码有误 !",Toast.LENGTH_SHORT).show();
                    }
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            //post请求
                            .add("username", username.getText().toString())
                            .add("password",password.getText().toString())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.54.134.17/app/login")
                            .post(requestBody)
                            //服务器ip地址
                            .build();
                    Response shuju_jieshou = client.newCall(request).execute();
                    String responseData = shuju_jieshou.body().string();
                    System.out.println(responseData);
                    userData = JsonTOBeanUtil.getBeanSingleData(User.class,responseData);
                    if (userData==null) {
                            Toast.makeText(LoginActivity.this, "账号没有注册", Toast.LENGTH_SHORT).show();
                    }
                    else{
                            editor.putString("USER_NAME", user);
                            editor.putString("PASSWORD", pass);
                            //判断是否记住密码
                            if(remembermima.isChecked()){
                                editor.putBoolean("remember", true);
                            }else{
                                editor.putBoolean("remember", false);
                            }
                            //是否自动登录
                            if(autologin.isChecked()){
                                checkbox=true;
                                editor1.putBoolean("autologin", true);
                            }else{
                                editor1.putBoolean("autologin", false);
                            }
                            editor.commit();
                            //跳转
                            initEvent();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initEvent() {
        findViewById(R.id.denglu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 注意这里，没有带共享元素哦(共享元素的打算放到下面讲)
                 */
                if( checkbox == true){
                    setCache(token,LoginActivity.this,"User_Key",MODE_PRIVATE);
                    String ge_key = getCache(LoginActivity.this,"User_Key");
                    Log.d(TAG, "run: -----------------------2122222222"+ge_key);
                }else{ }
                ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(mActivity);
                startActivity(new Intent(mContext, MainActivity.class), compat.toBundle());
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }
    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
            } else {
                Toast.makeText(context, "检查到没有网络", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
