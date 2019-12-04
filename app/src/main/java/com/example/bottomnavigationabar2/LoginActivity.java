package com.example.bottomnavigationabar2;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.bean.ResultBean;
import static com.example.bottomnavigationabar2.utils.FileCacheUtil.getCache;
import com.example.bottomnavigationabar2.bean.User;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;
import com.example.bottomnavigationabar2.utils.HandlerUtil;
import com.example.util.JsonTOBeanUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.bottomnavigationabar2.utils.FileCacheUtil.setCache;
import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    public static final int STARTACTIVITY=1;
    public static final int LOGIN_FAILED=2;
    public static final int NO_NETWORK=3;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private LinearLayout loadLayout;
    String user = null;
    User userData;
    String pass = null;
    String token ="1";
    private CheckBox autologin;
    private CheckBox rememberPassword;
    private IntentFilter intentFilter;
    Boolean checkbox = false;
    private NetworkChangeReceiver networkChangeReceiver;
    private SharedPreferences sp;
    private HandlerUtil handlerUtil;
    private Boolean key;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case STARTACTIVITY:
                    loadLayout.setVisibility(View.GONE);
                    User user= (User) msg.obj;
                    JPushInterface.setAlias(LoginActivity.this, String.valueOf(user.getUid()), new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            Log.i(TAG, "gotResult: 成功拉");
                        }
                    });
                    FileCacheUtil.clearData();
                    FileCacheUtil.setCache(user,LoginActivity.this,"USERDATA.txt",0);
                    ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class), compat.toBundle());
                    finish();
                    break;
                case LOGIN_FAILED:
                    Toast.makeText(LoginActivity.this, "用户名或密码输入错误，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case NO_NETWORK:
                    Toast.makeText(LoginActivity.this, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    loadLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verifyStoragePermissions(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        key = getIntent().getBooleanExtra("key",false);
        //过度效果(没写)
        loadLayout=findViewById(R.id.loadLayout);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
        //获取记住密码，自动登录主键
        sp = getSharedPreferences("userInfo", 0);
        autologin = findViewById(R.id.autoLogin);
        rememberPassword = findViewById(R.id.rememberPassword);
        //获取账号 密码 登录按钮
        usernameEdit = (EditText)findViewById(R.id.et_username);
        passwordEdit = (EditText)findViewById(R.id.et_password);
        Button denglu = (Button)findViewById(R.id.denglu);
        handlerUtil = new HandlerUtil(this);
        denglu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                login();
            }
        });
        //将账号密码 和sp值存
        String name=sp.getString("USERNAME", "");
        String pass =sp.getString("PASSWORD", "");
        Log.i(TAG, "onCreate: name="+name);
        Log.i(TAG, "onCreate: password="+pass);
        //获取单选按钮的状态
        boolean choseRemember =sp.getBoolean("remember", false);
        Log.i(TAG, "onCreate: chose="+choseRemember);
        boolean choseAutoLogin =sp.getBoolean("autologin", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
 /*       if(choseAutoLogin){
            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }*/
        if(choseRemember){
            usernameEdit.setText(name);
            passwordEdit.setText(pass);
            rememberPassword.setChecked(true);
        }
        if(key){
            passwordEdit.setText("");
            rememberPassword.setChecked(false);
        }
        TextView textView = (TextView)findViewById(R.id.tex3);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent2,1);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitAPP();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void exitAPP() {

        ActivityManager activityManager = (ActivityManager) this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
    }
    private void login(final String username, final String password){
        loadLayout.setVisibility(View.VISIBLE);
        RequestBody requestBody = new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();
        Request request =new Request.Builder()
                .post(requestBody)
                .url("http://106.54.134.17/app/login")
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: 登录失败");
                Message message=new Message();
                message.what=NO_NETWORK;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JPushInterface.getAlias(LoginActivity.this,1);
                Log.i(TAG, "onResponse: 我的alise别名时:");
                String responseStr = response.body().string();
                Log.i(TAG, "onResponse: 登录json情况"+responseStr);
                try {
                    JSONObject jsonObject =new JSONObject(responseStr);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    SharedPreferences.Editor editor =sp.edit();
                    Gson gson =new Gson();
                    User user=gson.fromJson(jsonObject.getString("data"),User.class);
                    if(code==0){
                        Message message=new Message();
                        message.what=LOGIN_FAILED;
                        handler.sendMessage(message);
                        return;
                    }
                    if( checkbox == true){
                        setCache(token,LoginActivity.this,"User_Key",MODE_PRIVATE);
                        String ge_key = getCache(LoginActivity.this,"User_Key");
                    }
                    if(rememberPassword.isChecked()){
                        Log.i(TAG, "onResponse: ???");
                        editor.putBoolean("remember", true);
                        editor.putString("USERNAME",username);
                        editor.putString("PASSWORD",password);
                    }else{
                        editor.putBoolean("remember", false);
                    }
                    //是否自动登录
                    if(autologin.isChecked()){
                        checkbox=true;
                        editor.putBoolean("autologin", true);
                    }else{
                        editor.putBoolean("autologin", false);
                    }
                    editor.commit();
                    Message message=new Message();
                    message.obj=user;
                    message.what=STARTACTIVITY;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void login(){
        String usernamee =usernameEdit.getText().toString();
        String password=passwordEdit.getText().toString();
        if(TextUtils.isEmpty(usernamee)){
            usernameEdit.setError("账号不能为空");
        }
        if(TextUtils.isEmpty(password)){
            passwordEdit.setError("密码不能为空");
        }
        login(usernamee,password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:String account=data.getStringExtra("account");
                   usernameEdit.setText(account);
                   break;
        }
    }
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
