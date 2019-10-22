package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bottomnavigationabar2.PostDetails;
import com.example.bottomnavigationabar2.R;

/**
 * 创建于2019/10/16 9:07🐎
 */
public class DatailsText extends LinearLayout {
    private ImageView comment;
    private ImageView give_the_thumbs_up;
    private ImageView collection;
    public DatailsText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_details, this);
        LinearLayout linearLayout = findViewById(R.id.Lin_comment);
        LinearLayout linearLayout2 = findViewById(R.id.Lin_give_the_thumbs_up);
        LinearLayout linearLayout3 = findViewById(R.id.Lin_Collection);
        linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostDetails.class);
                getContext().startActivity(intent);
            }
        });

        //点赞的点击事件
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostDetails.class);
                getContext().startActivity(intent);

            }
        });
        //收藏的点击事件
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostDetails.class);
                getContext().startActivity(intent);
            }
        });
    }
}
