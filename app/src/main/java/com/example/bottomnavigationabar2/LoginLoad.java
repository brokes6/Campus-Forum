package com.example.bottomnavigationabar2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginLoad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_load);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(LoginLoad.this,LoginActivity.class);
                LoginLoad.this.startActivity(intent);
                LoginLoad.this.finish();
            }
        },2000);
    }
}
