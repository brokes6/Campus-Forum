package com.example.bottomnavigationabar2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Yemian_text extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemian);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.bringToFront();
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        //悬浮按钮 点击事件
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_addPost = new Intent(Yemian_text.this,addPost.class);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                startActivity(intent_addPost);
            }
        });

    }
}
