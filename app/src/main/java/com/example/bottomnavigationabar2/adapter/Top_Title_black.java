package com.example.bottomnavigationabar2.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.bottomnavigationabar2.R;

public class Top_Title_black extends LinearLayout {
    public Top_Title_black(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_black, this);
        LinearLayout img = findViewById(R.id.title_back);
        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
        // 上下左右滑动监听

    }
}
