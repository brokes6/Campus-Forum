package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bottomnavigationabar2.MyImageView;
import com.example.bottomnavigationabar2.Post;
import com.example.bottomnavigationabar2.PostDetails;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.model.NineGridTestModel;
import com.example.bottomnavigationabar2.view.NineGridTestLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by HMY on 2016/8/6
 */
public class NineGridTest2Adapter extends RecyclerView.Adapter<NineGridTest2Adapter.ViewHolder> {

    private Context mContext;
    private View convertView;
    private List<Post> mList=new ArrayList<>();
    protected LayoutInflater inflater;
    private static final String TAG = "NineGridTest2Adapter";
    public NineGridTest2Adapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<Post> list) {
        mList.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        convertView = inflater.inflate(R.layout.item_bbs_nine_grid, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        Log.i(TAG, "onCreateViewHolder:111");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: 开始创建"+position);
        holder.content.setText(Html.fromHtml(mList.get(position).getContent()));
        holder.datetime.setText(mList.get(position).getPcreateTime());
        holder.uimg.setImageURL(mList.get(position).getUimg());
        holder.username.setText(mList.get(position).getUsername());
        holder.postId=mList.get(position).getPid();
        holder.layout.setIsShowAll(mList.get(position).isShowAll());
        holder.layout.setUrlList(Arrays.asList(mList.get(position).getImgUrl().split(",")));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: 随便响应"+holder.postId);
                Intent intent = new Intent(mContext, PostDetails.class);
                intent.putExtra("postId",holder.postId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListSize(mList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NineGridTestLayout layout;
        TextView username;
        MyImageView uimg;
        TextView datetime;
        TextView content;
        int postId;
        public ViewHolder(View itemView) {
            super(itemView);
            layout = (NineGridTestLayout) itemView.findViewById(R.id.layout_nine_grid);
            username=itemView.findViewById(R.id.tiezi_username);
            uimg=itemView.findViewById(R.id.tieze_user_img);
            datetime=itemView.findViewById(R.id.tiezi_time);
            content=itemView.findViewById(R.id.tieze_Text);
        }

    }

    private int getListSize(List<Post> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
    public void clear(){
        mList=null;
        mList=new ArrayList<>();
    }
}
