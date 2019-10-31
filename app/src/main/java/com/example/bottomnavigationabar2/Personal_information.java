package com.example.bottomnavigationabar2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.bottomnavigationabar2.personalpage.More;

import de.hdodenhof.circleimageview.CircleImageView;

public class Personal_information extends AppCompatActivity {

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
        CircleImageView circleImageView=(CircleImageView) findViewById(R.id.personal_return);
        LinearLayout myHead=(LinearLayout)findViewById(R.id.myHead);
        LinearLayout myName=(LinearLayout)findViewById(R.id.myName);
        final LinearLayout Hobby=(LinearLayout)findViewById(R.id.Hobby);
        LinearLayout autograph=(LinearLayout)findViewById(R.id.autograph);
        LinearLayout more=(LinearLayout)findViewById(R.id.more);
        myHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        myName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Personal_information.this, com.example.bottomnavigationabar2.personalpage.myName.class);
                startActivity(intent);
            }
        });
        Hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Personal_information.this, com.example.bottomnavigationabar2.personalpage.Hobby.class);
                startActivity(intent);

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
                        inputServer.getText().toString();
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
                finish();
            }
        });
    }

}
