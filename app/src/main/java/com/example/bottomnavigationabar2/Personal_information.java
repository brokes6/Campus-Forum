package com.example.bottomnavigationabar2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Switch;

import de.hdodenhof.circleimageview.CircleImageView;

public class Personal_information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
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
                Intent intent=new Intent(Personal_information.this,myName.class);
                startActivity(intent);
            }
        });
        Hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Personal_information.this,Hobby.class);
                startActivity(intent);

            }
        });
        autograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Personal_information.this,Autograph.class);
                startActivity(intent);

            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Personal_information.this,More.class);
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
