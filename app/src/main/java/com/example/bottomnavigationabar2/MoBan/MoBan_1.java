package com.example.bottomnavigationabar2.MoBan;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bottomnavigationabar2.LoadFileVo;
import com.example.bottomnavigationabar2.Post;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.adapter.NineGridTest2Adapter;
import com.example.bottomnavigationabar2.model.NineGridTestModel;
import com.example.bottomnavigationabar2.PostDetails;
import com.example.util.JsonTOBeanUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class MoBan_1 extends Fragment {
    private static final String ARG_LIST = "list";
    private int page=1;
    private String[] mUrls = new String[]{
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            "http://106.54.134.17/image/topicalimg/test.png",
            };
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NineGridTest2Adapter mAdapter;
    private List<NineGridTestModel> mList = new ArrayList<>();
    private View view;
    public static void startActivity(Context context, List<NineGridTestModel> list) {
        Intent intent = new Intent(context, MoBan_1.class);
        intent.putExtra(ARG_LIST, (Serializable) list);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mo_ban_1, container, false);
//        Toast.makeText(getContext(),"gogogo",Toast.LENGTH_SHORT).show();
        initView();
        getPostList();
        return view;
    }
    private void initListData(){
        NineGridTestModel model1 = new NineGridTestModel();
        model1.urlList.add(mUrls[0]);
        model1.isShowAll=true;
        mList.add(model1);

        NineGridTestModel model2 = new NineGridTestModel();
        model2.urlList.add(mUrls[4]);
        model2.isShowAll=true;
        mList.add(model2);
//
//        NineGridTestModel model3 = new NineGridTestModel();
//        model3.urlList.add(mUrls[2]);
//        mList.add(model3);

        NineGridTestModel model4 = new NineGridTestModel();
        for (int i = 0; i < mUrls.length; i++) {
            model4.urlList.add(mUrls[i]);
        }
        model4.isShowAll = true;
        mList.add(model4);

        NineGridTestModel model5 = new NineGridTestModel();
        for (int i = 0; i < mUrls.length; i++) {
            model5.urlList.add(mUrls[i]);
        }
        model5.isShowAll = true;//显示全部图片
        mList.add(model5);

        NineGridTestModel model6 = new NineGridTestModel();
        for (int i = 0; i < 9; i++) {
            model6.urlList.add(mUrls[i]);
        }
        mList.add(model6);

        NineGridTestModel model7 = new NineGridTestModel();
        for (int i = 3; i < 7; i++) {
            model7.urlList.add(mUrls[i]);
        }
        mList.add(model7);

        NineGridTestModel model8 = new NineGridTestModel();
        for (int i = 3; i < 6; i++) {
            model8.urlList.add(mUrls[i]);
        }
        mList.add(model8);
    }
/*    private void getIntentData() {
        mList = (List<NineGridTestModel>)getActivity().getIntent().getSerializableExtra(ARG_LIST);
    }*/
    private void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NineGridTest2Adapter(getContext());
        Log.i(TAG, "initView: "+mList.size());
        mAdapter.setList(mList);
        mRecyclerView.setAdapter(mAdapter);
    }
    private void getPostList(){
        final Request request = new Request.Builder()
                .url("http://106.54.134.17/app/getPopularPost?startPage="+page)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onFailure:失败呃");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String dataStr = response.body().string();
                System.out.println("帖子数据"+dataStr);
                List<Post> posts= (List<Post>) JsonTOBeanUtil.getBean(Post.class,dataStr);
                System.out.println("帖子列表长度"+posts.size());
                for(Post post:posts){
                    NineGridTestModel model1 = new NineGridTestModel();
                    /*String[]imgurls = post.getImgUrl().split(",");*/
                    /*for(String url:imgurls){
                        model1.urlList.add(url);
                    }*/
                    model1.username=post.getUsername();
                    model1.uimg=post.getUimg();
                    model1.datetime=post.getPcreateTime();
                    System.out.println("正文内容"+post.getContent());
                    model1.content=post.getContent();
                    mList.add(model1);
                }
                page++;
            }
        });
    }
    }
