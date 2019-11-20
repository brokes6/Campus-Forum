package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bottomnavigationabar2.Post;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.utils.NetWorkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建于2019/11/19 19:47🐎
 */
//public class NewsAdapter extends RecyclerView.Adapter<NineGridTest2Adapter.ViewHolder>{
//        private Context mContext;
//        private View convertView;
//        protected LayoutInflater inflater;
//        private NetWorkUtil netWorkUtil;
//        private List<Post> mList=new ArrayList<>();
//    public NewsAdapter(Context context) {
//        mContext = context;
//        inflater = LayoutInflater.from(context);
//        netWorkUtil=new NetWorkUtil(context);
//    }
//    public void setList(List<Post> list) {
//        mList.addAll(list);
//    }
//
//    @Override
//    public ViewHoldere onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        convertView = inflater.inflate(R.layout.news_template, viewGroup,false);
//        ViewHolder viewHolder = new ViewHolder(convertView);
//        return viewHolder;
//    }
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//    }
//        public ViewHolder(View itemView) {
//
//    }
//}
