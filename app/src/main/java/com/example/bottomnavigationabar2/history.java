package com.example.bottomnavigationabar2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class history extends AppCompatActivity {
    private static final String TAG = "history";
    private SmartRefreshLayout smartRefreshLayout1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history1);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        CircleImageView imageView=findViewById(R.id.history_return);
        smartRefreshLayout1= (SmartRefreshLayout) findViewById(R.id.refreshLayout1);
        smartRefreshLayout1.setEnableRefresh(false);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        /* 可不设置，有默认样式
         * 设置 Header 为 Material风格
         * 这两段代码只有在布局里添加了上拉和下拉的代码才有效果
         */
        smartRefreshLayout1.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        //上拉加载
        smartRefreshLayout1.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout1.finishLoadMore(2000);
            }
        });
    }
}