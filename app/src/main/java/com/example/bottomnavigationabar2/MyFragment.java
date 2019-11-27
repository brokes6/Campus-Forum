package com.example.bottomnavigationabar2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.bean.User;
import com.example.bottomnavigationabar2.utils.FileCacheUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 武当山道士 on 2017/8/16.
 */

public class MyFragment extends Fragment {
    private static final String TAG = "MyFragment";
    private LinearLayout Set_up;
    private View view;
    private User userData;
    private TextView usernameView;
    private MyImageView picture;
    public static final int CHOOSE_PHOTO = 2;
    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_fragment, container, false);
        userData=FileCacheUtil.getCache(getContext(),"USERDATA.txt",0, User.class);
        Bundle bundle = getArguments();
        initView();
        initData();
//        TextView Tuichu = (TextView)view.findViewById(R.id.tuichu);
        LinearLayout history=(LinearLayout)view.findViewById(R.id.history);
        LinearLayout collection=(LinearLayout)view.findViewById(R.id.Collection);
        Set_up = view.findViewById(R.id.Set_up);
        Set_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Set_up.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),history.class);
                getActivity().overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                startActivity(intent);
            }
        });
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MyCollection.class);
                getActivity().overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                startActivity(intent);
            }
        });

        /*Tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //系统将终止一切和这个程序包关联的，所有共享同一 uid 的 process全部杀掉，还会停止相关的服务，并且会发送一个广播。
                //注意事项：该方法虽然可以立即杀死与指定包相关联的所有后台进程，但是这些进程如果在将来某一时刻需要使用，便会重新启动。
                // 而且该方法只是结束后台进程的方法，不能结束当前应用移除所有的 Activity。
                // 如果需要退出应用，需要添加System.exit(0)方法一起使用，并且只限栈内只有一个Activity，如果有多个Activity时，正如上面 方法 2 所说，就不起作用了。
                ActivityManager am = (ActivityManager)getActivity().getSystemService (Context.ACTIVITY_SERVICE);
                am.killBackgroundProcesses(getActivity().getPackageName());
                System.exit(0);
            }
        });*/
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Personal_information.class);
                startActivityForResult(intent,1);
            }
        });
        return view;
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor =getActivity().getContentResolver().query(uri, null, selection, null, null);
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
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
    private void initView(){
        usernameView=view.findViewById(R.id.username);
        picture = view.findViewById(R.id.user_head_img);
        LinearLayout history=(LinearLayout)view.findViewById(R.id.history);
        LinearLayout collection=(LinearLayout)view.findViewById(R.id.Collection);
        LinearLayout news = view.findViewById(R.id.news);
        LinearLayout concern=(LinearLayout)view.findViewById(R.id.my_concern);
        concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MyConcern.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),history.class);
                getActivity().overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                startActivity(intent);
            }
        });
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MyCollection.class);
                getActivity().overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                startActivity(intent);
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Mynews.class);
                startActivity(intent);
            }
        });
    }
    private void initData(){
        usernameView.setText(userData.getUsername());
        picture.setCacheImageURL(userData.getUimg());
        Log.i(TAG, "initData: 被调用");
    }
    //先暂时弃用
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==0){
            return;
        }
        switch (requestCode){
            case 1:
                initData();
                break;
        }
    }
}

