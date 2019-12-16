package com.example.bottomnavigationabar2;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationabar2.adapter.Setup_top;

import java.util.Set;

public class Set_up extends AppCompatActivity {
        private TextView user_back;
        private ImageView set_back;
        private LinearLayout message;
        private LinearLayout image;
        private LinearLayout clear;
        private LinearLayout help;
        private LinearLayout user;
        private LinearLayout privacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //获取参数
        set_back=findViewById(R.id.set_back);
        user_back=findViewById(R.id.user_back);
        set_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        user_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Set_up.this,"已退出",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Set_up.this,LoginActivity.class);
                intent.putExtra("key",true);
                startActivity(intent);
            }
        });
        initView();
        setDate();
    }
    private void initView(){
        set_back=findViewById(R.id.set_back);
        user_back=findViewById(R.id.user_back);
        message=findViewById(R.id.message_tishi);
        image=findViewById(R.id.image_model);
        clear=findViewById(R.id.clear);
        help=findViewById(R.id.help);
        user=findViewById(R.id.user_agreement);
        privacy=findViewById(R.id.privacy);
    }
    private void setDate(){
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Set_up.this,Error.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Set_up.this, Error.class);
                startActivity(intent);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Set_up.this,Error.class);
                startActivity(intent);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Set_up.this,Error.class);
                startActivity(intent);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Set_up.this,Error.class);
                startActivity(intent);
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Set_up.this,Error.class);
                startActivity(intent);
            }
        });
    }
}
