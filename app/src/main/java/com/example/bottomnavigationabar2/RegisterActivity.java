package com.example.bottomnavigationabar2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.scwang.smartrefresh.header.material.CircleImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/*
*
*         ----------------注册页面------------------
*
*/
public class RegisterActivity extends AppCompatActivity {
    private de.hdodenhof.circleimageview.CircleImageView userimg;
    String data_user =null;
    ResultBean resultBean;
    EditText name_denglu;
    EditText username_denglu;
    EditText pass_dengli_1;
    EditText email;
    public static final int PICK_PHOTO = 102;
    private Uri imageUri;
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
        //用于屏蔽系统的头部，（以用）
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        final EditText name_denglu = (EditText)findViewById(R.id.ret_name);
        final EditText username_denglu = (EditText)findViewById(R.id.ret_username);
        final EditText pass_denglu = (EditText)findViewById(R.id.ret_password);
        final EditText email = (EditText)findViewById(R.id.ret_email);
        userimg = findViewById(R.id.zhc_userimg);
        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //动态申请获取访问 读写磁盘的权限
                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                } else {
                    //打开相册
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //Intent.ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_PHOTO); // 打开相册
                }
            }
        });
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
            //打开系统相机
            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                switch (requestCode) {
                    case PICK_PHOTO:
                        if (resultCode == RESULT_OK) { // 判断手机系统版本号
                            if (Build.VERSION.SDK_INT >= 19) {
                                // 4.4及以上系统使用这个方法处理图片
                                handleImageOnKitKat(data);
                            } else {
                                // 4.4以下系统使用这个方法处理图片
                                handleImageBeforeKitKat(data);
                            }
                        }

                        break;
                    default:
                        break;
                }
            }
    /**
     * android 4.4以前上的处理方式
     */
            @TargetApi(19)
            private void handleImageOnKitKat(Intent data) {
                String imagePath = null;
                Uri uri = data.getData();
                if (DocumentsContract.isDocumentUri(this, uri)) {
                    // 如果是document类型的Uri，则通过document id处理
                    String docId = DocumentsContract.getDocumentId(uri);
                    if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                        String id = docId.split(":")[1];
                        // 解析出数字格式的id
                        String selection = MediaStore.Images.Media._ID + "=" + id;
                        imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                    } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                        imagePath = getImagePath(contentUri, null);
                    }
                } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                    // 如果是content类型的Uri，则使用普通方式处理
                    imagePath = getImagePath(uri, null);
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    // 如果是file类型的Uri，直接获取图片路径即可
                    imagePath = uri.getPath();
                }
                // 根据图片路径显示图片
                displayImage(imagePath);
            }
    /**
     * android 4.4以前的处理方式
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            userimg.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取相册图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * -
     * 将数据传上服务器
     * -
     */
    private void sendOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    name_denglu = (EditText)findViewById(R.id.ret_name);
                    username_denglu = (EditText)findViewById(R.id.ret_username);
                    pass_dengli_1 = (EditText)findViewById(R.id.ret_password);
                    email = (EditText)findViewById(R.id.ret_email);
                    String regId= JPushInterface.getRegistrationID(RegisterActivity.this);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            //post请求
                            .add("username", name_denglu.getText().toString())
                            .add("account",username_denglu.getText().toString())
                            .add("password",pass_dengli_1.getText().toString())
                            .add("email",email.getText().toString())
                            .add("regId",regId)
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
