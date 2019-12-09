package com.example.bottomnavigationabar2.MoBan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bottomnavigationabar2.Post;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.NineGridTest2Adapter;

import java.util.ArrayList;
import java.util.List;

public class SearchPostTemplate extends Fragment {
    private NineGridTest2Adapter mAdapter;
    private View view;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout loadLayout;
    private ProgressBar progressBar;
    private TextView loadTextView;
    private List<Post> posts;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.mo_ban_1,container,false);
        initView();
        return view;
    }
    private void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        loadTextView=view.findViewById(R.id.loadTextView);
        progressBar=view.findViewById(R.id.loading);
        loadLayout=view.findViewById(R.id.loadLayout);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NineGridTest2Adapter(getContext());
        mAdapter.setList(new ArrayList<Post>());
        mRecyclerView.setAdapter(mAdapter);
    }
    public void setData(List<Post> posts){
        if(posts!=null) {
            this.posts=posts;
            mAdapter.setList(posts);
            mAdapter.notifyDataSetChanged();
            loadLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }else {
            handlerNoResource();
        }
    }
    public void handlerNoResource(){
        progressBar.setVisibility(View.GONE);
        loadTextView.setText("没有找到相关数据");
        loadTextView.setTextSize(20);
    }
    public void clear(){
        if(posts!=null) {
            posts.clear();
            mAdapter.notifyDataSetChanged();
        }
    }
}
