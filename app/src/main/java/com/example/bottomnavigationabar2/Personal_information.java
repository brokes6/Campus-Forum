package com.example.bottomnavigationabar2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.bean.User;
import com.example.bottomnavigationabar2.personalpage.More;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Personal_information extends AppCompatActivity {
    private static final String TAG = "Personal_information";
    public static final int SHOW_TOAST=3;
    public static final int SET_USER_IMG=1;
    public static final int EDIT_USERNAME=10;
    public static final int EDIT_USIGN=11;
    private File file;
    private Uri imageUri=null;
    private User userData;
    private CircleImageView circleImageView;
    private LinearLayout myHead;
    private LinearLayout myName;
    private LinearLayout Hobby;
    private TextView account;
    private LinearLayout autograph;
    private LinearLayout more;
    private MyImageView userImg;
    private TextView textView;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    Toast.makeText(Personal_information.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case SET_USER_IMG:
                    String imgUrl=msg.obj.toString();
                    userData.setUimg(imgUrl);
                    userImg.setCacheImageURL(imgUrl);
                    FileCacheUtil.updateUser(userData,Personal_information.this);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null) {
            actionBar.hide();
        }
        initView();
        initData();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0); // 打开相册
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    private void initView(){
        account=findViewById(R.id.accountnum);
        circleImageView=(CircleImageView) findViewById(R.id.personal_return);
        myHead=(LinearLayout)findViewById(R.id.myHead);
        myName=(LinearLayout)findViewById(R.id.myName);
        Hobby=(LinearLayout)findViewById(R.id.Hobby);
        autograph=(LinearLayout)findViewById(R.id.autograph);
        more=(LinearLayout)findViewById(R.id.more);
        userImg=findViewById(R.id.userImg);
        textView=findViewById(R.id.username);
        myHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPic();
            }
        });
        myName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputServer = new EditText(Personal_information.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(Personal_information.this);
                builder.setTitle("你的名字").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateUserInfo(inputServer.getText().toString());
                    }
                });
                builder.show();
            }
        });
        Hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputServer = new EditText(Personal_information.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(Personal_information.this);
                builder.setTitle("我的兴趣").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateUserInfo(inputServer.getText().toString());
                    }
                });
                builder.show();

            }
        });
        autograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputServer = new EditText(Personal_information.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(Personal_information.this);
                builder.setTitle("个性签名").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateUserInfo(inputServer.getText().toString());
                    }
                });
                builder.show();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Personal_information.this, More.class);
                startActivity(intent);

            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void initData(){
        userData= FileCacheUtil.getUser(Personal_information.this);
        account.setText(userData.getAccount());
        textView.setText(userData.getUsername());
        if(userData.getUimg()!=null)
            userImg.setCacheImageURL(userData.getUimg());
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.
                    getAuthority())) {
                Log.i(TAG, "handleImageOnKitKat: 文档");
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.i(TAG, "handleImageOnKitKat: content");
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.i(TAG, "handleImageOnKitKat: 文件");
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }
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
                path = cursor.getString(cursor.getColumnIndex(MediaStore.
                        Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            file=new File(imagePath);
            Log.i(TAG, "displayImage: 相册选择="+file.length());
            netUploadHeadImg(userData.getToken());
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExsit = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExsit) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString();
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    //重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==0){
            Toast.makeText(this,"返回发帖页面",Toast.LENGTH_SHORT);
            return;
        }
        switch (requestCode){
            case 0:
                Log.i(TAG, "onActivityResult: 11");
                if (Build.VERSION.SDK_INT >= 19) {
                    // 4.4及以上系统使用这个方法处理图片
                    handleImageOnKitKat(data);
                } else {
                    // 4.4以下系统使用这个方法处理图片
                    handleImageBeforeKitKat(data);
                }
                break;
            case 1:
                Log.i(TAG, "onActivityResult: "+file.length());
                netUploadHeadImg(userData.getToken());
                break;
            case EDIT_USERNAME:
                Log.i(TAG, "onActivityReenter: 开始设置");
                String name=data.getStringExtra("username");
                textView.setText(name);
                showShortToast("修改用户名成功!");
                break;
        }
    }
    public File  cratephotofile() throws IOException {//返回一个File类的文件
        String name=new SimpleDateFormat("YYYYMMdd_HHmmss").format(new Date());
//年月日小时分秒
        File stordir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //获得公共目录下的图片文件路径
        File image=File.createTempFile(name,".jpeg",stordir);
        //1：字首2：后缀3：在哪个目录下
        return  image;
    }
    //自定义方法selectPic
    private void selectPic() {
        //动态请求权限，除此之外还需进行Androidmanifest.xml中进行请求
        if (ContextCompat.checkSelfPermission(Personal_information.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Personal_information.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Personal_information.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Personal_information.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        final CharSequence[] items = {"相册", "拍照"};
        AlertDialog.Builder dlg = new AlertDialog.Builder(Personal_information.this);
        dlg.setTitle("添加图片");
        dlg.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // 这里item是根据选择的方式，
                if (item == 0) {
                    if (ContextCompat.checkSelfPermission(Personal_information.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Personal_information.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent,  0); // 打开相册
                    }
                } else {
                    try {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        file=cratephotofile();
                        if(!file.getParentFile().exists()){
                            file.getParentFile().mkdir();
                        }
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            imageUri= FileProvider.getUriForFile(Personal_information.this,"com.example.bottomnavigationabar2.fileprovider",file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        }else{
                            imageUri = Uri.fromFile(file);
                        }
                        startActivityForResult(intent,1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).create();
        dlg.show();
    }
    private void netUploadHeadImg(String token){
        Log.i(TAG, "netUploadHeadImg: file="+file.length());
        try {
            MediaType type =MediaType.parse("image/*");
            RequestBody body=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file",file.getName(),RequestBody.create(MediaType.parse("application/octet-stream"),file))
                    .addFormDataPart("token",token)
                    .build();
            final Request request = new Request.Builder()
                    .url("http://106.54.134.17/app/updateUserImg")
                    .post(body)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onFailure:失败呃");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String urls = response.body().string();
                    Log.i(TAG, "onResponse:urls="+urls);
                    try {
                        JSONObject jsonObject =new JSONObject(urls);
                        String data=jsonObject.getString("data");
                        Message message=new Message();
                        message.what=SET_USER_IMG;
                        message.obj=data;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showShortToast("上传图片请求异常！");

        }
    }
    private void showShortToast(String msg) {
        Message message=new Message();
        message.obj=msg;
        message.what=SHOW_TOAST;
        handler.sendMessage(message);
    }
    private void updateUserInfo(String username){
        RequestBody requestBody=new FormBody.Builder()
                .add("username",username)
                .add("token",userData.getToken())
                .build();
        final Request request=new Request.Builder()
                .url("http://10.0.2.2:8080/app/updateUserInfo")
                .post(requestBody)
                .build();
        OkHttpClient client=new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String reponseData=response.body().toString();
                Log.i(TAG, "onResponse: 返回数据"+reponseData);

            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed: 被摧毁拉");
        setResult(1,null);
        finish();
    }

}
