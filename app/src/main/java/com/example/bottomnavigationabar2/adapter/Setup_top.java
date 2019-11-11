package com.example.bottomnavigationabar2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bottomnavigationabar2.PostDetails;
import com.example.bottomnavigationabar2.R;

/**
 * 创建于2019/10/28 14:44🐎
 */
public class Setup_top extends LinearLayout {
    public Setup_top(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.setup_top, this);
        ImageView img = findViewById(R.id.setup_back);
        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
        // 上下左右滑动监听

    }
}
