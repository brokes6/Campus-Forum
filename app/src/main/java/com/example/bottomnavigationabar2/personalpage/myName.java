package com.example.bottomnavigationabar2.personalpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bottomnavigationabar2.Personal_information;
import com.example.bottomnavigationabar2.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class myName extends AppCompatActivity {
    private static final String TAG = "myName";
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_name);
        final String token=getIntent().getStringExtra("token");
        ImageView imageView=(ImageView)findViewById(R.id.myname_return);
        final TextView textView=(TextView)findViewById(R.id.myname_sure);
        editText=findViewById(R.id.edit_Username);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo(editText.getText().toString(),token);
            }
        });
    }
    private void updateUserInfo(String username,String token){
        RequestBody requestBody=new FormBody.Builder()
                .add("username",username)
                .add("token",token)
                .build();
        final Request request=new Request.Builder()
                .url("http://10.0.2.2:8080/app/updateUserInfo")
                .post(requestBody)
                .build();
        OkHttpClient client=new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String reponseData=response.body().string();
                Log.i(TAG, "onResponse: 返回数据"+reponseData);
                Intent intent=new Intent();
                intent.putExtra("username",editText.getText().toString());
                setResult(Personal_information.EDIT_USERNAME,intent);
                finish();
            }
        });
    }
}
