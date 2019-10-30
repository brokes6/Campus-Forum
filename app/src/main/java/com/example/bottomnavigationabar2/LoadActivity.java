package com.example.bottomnavigationabar2;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
public class LoadActivity extends AppCompatActivity {
    private final int time = 3000;
    private boolean lag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //延时time秒后，将运行如下代码
                if(lag){
                    finish();
                    Toast.makeText(LoadActivity.this , "wait 5s!" , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoadActivity.this , MainActivity.class);
                    startActivity(intent);
                }
            }
            } , time);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null) {
            actionBar.hide();
        }
    }
}
