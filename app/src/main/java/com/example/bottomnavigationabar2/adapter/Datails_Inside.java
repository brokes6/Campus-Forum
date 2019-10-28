package com.example.bottomnavigationabar2.adapter;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bottomnavigationabar2.R;

/**
 * 创建于2019/10/16 9:07🐎
 */
public class Datails_Inside extends LinearLayout {
    private ImageView imageView1;
    private ImageView Ins_comment;
    private ImageView Ins_give_the_thumbs_up;
    private ImageView Ins_collection;
    public Datails_Inside(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.details_inside, this);
        LinearLayout linearLayout = findViewById(R.id.Ins_Lin_comment);
        LinearLayout linearLayout2 = findViewById(R.id.Ins_Lin_give_the_thumbs_up);
        LinearLayout linearLayout3 = findViewById(R.id.Ins_Lin_Collection);
        imageView1 = findViewById(R.id.comment);

        linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imageView1.setImageDrawable(getResources().getDrawable(R.drawable.dianzanwanc));
            }
        });

        //点赞的点击事件
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"收藏成功",Toast.LENGTH_SHORT).show();

            }
        });
        //收藏的点击事件
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTopActivity(getContext());
            }
        });
    }
    public static String getTopActivity(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Log.d("Chunna.zheng", "pkg:"+cn.getPackageName());//包名
        Log.d("Chunna.zheng", "cls:"+cn.getClassName());//包名加类名
        return cn.getClassName();
    }
}
