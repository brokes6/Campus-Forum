package com.example.bottomnavigationabar2.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.HistorySearchAdapter;
import com.example.bottomnavigationabar2.utils.HistorySearchUtil;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private SearchView searchView;
    private Button searchButton;
    private RecyclerView recyclerView;
    private List<String> historyList=new ArrayList<>();
    private HistorySearchAdapter adapter;
    private LinearLayout searchLayout;
    private ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initView();
        initHistoryRecycler();
        getHistoryList();//得到历史记录数组
        setSearchTvListener();//设置搜索按钮监听器
/*        setHistoryEmptyTvListener();//设置清空记录按钮监听器*/

    }
    private void initView(){
        searchView=findViewById(R.id.searchView);
        searchButton=findViewById(R.id.button);
        recyclerView=findViewById(R.id.recyclerView);
        searchLayout=findViewById(R.id.searchLayout);
        searchView.setQueryHint("请输入搜索内容");
        searchView.setIconifiedByDefault(false);
        back = findViewById(R.id.search_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setSearchTvListener() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryWord=searchView.getQuery().toString();
                if (queryWord.isEmpty()){
                    Toast.makeText(SearchActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }else{
                HistorySearchUtil.getInstance(SearchActivity.this).putNewSearch(queryWord);//保存记录到数据库
                getHistoryList();
                adapter.notifyDataSetChanged();
                showViews();
                Toast.makeText(SearchActivity.this, "此条记录已保存到数据库",
                        Toast.LENGTH_SHORT).show();
                searchView.setQuery("",false);
                searchView.clearFocus();
                Intent intent=new Intent(SearchActivity.this,SearchDetailsActivity.class);
                intent.putExtra("queryWord",queryWord);
                startActivityForResult(intent,1);
                }
            }
        });
    }

/*    private void setHistoryEmptyTvListener() {
        historyEmptyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistorySearchUtil.getInstance(MainActivity.this)
                        .deleteAllHistorySearch();
                getHistoryList();
                adapter.notifyDataSetChanged();//刷新列表
                showViews();
            }
        });
    }*/
    /**
     * 设置历史记录界面可见性，即记录为空时，不显示清空历史记录按钮等view
     */
    private void showViews() {
        if (historyList.size() > 0) {
            searchLayout.setVisibility(View.VISIBLE);
        } else {
            searchLayout.setVisibility(View.GONE);
        }
    }
    private void initHistoryRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);//解决滑动冲突
        adapter=new HistorySearchAdapter(this,historyList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new HistorySearchAdapter.OnItemClickListener() {
            @Override
            public void onItemNameTvClick(View v, String name) {
                HistorySearchUtil.getInstance(SearchActivity.this).putNewSearch(name);//保存记录到数据库
                Intent intent=new Intent(SearchActivity.this,SearchDetailsActivity.class);
                intent.putExtra("queryWord",name);
                startActivity(intent);
            }
            @Override
            public void onItemDeleteImgClick(View v, String name) {
                Log.i(TAG, "onItemDeleteImgClick: "+name);
                historyList.remove(name);
                HistorySearchUtil.getInstance(SearchActivity.this).deleteHistorySearch(name);
                adapter.notifyDataSetChanged();
                showViews();
            }
    });
}
    private void getHistoryList(){
        historyList.clear();
        historyList.addAll(HistorySearchUtil.getInstance(this).queryHistorySearchList());
        System.out.println("历史长度为"+historyList.size());
        adapter.notifyDataSetChanged();
        showViews();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed: 我返回拉");
        setResult(1);
        finish();
    }

}
