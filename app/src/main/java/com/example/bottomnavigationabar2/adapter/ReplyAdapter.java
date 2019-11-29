package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bottomnavigationabar2.MyImageView;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.bean.ReplyDetailBean;
import com.example.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {
    private static final String TAG = "ReplyAdapter";
    private View convertView;
    private Context mcontext;
    private LayoutInflater layoutInflater;
    private List<ReplyDetailBean> replyDetailBeans=new ArrayList<>();
    public ReplyAdapter(Context mcontext) {
        this.mcontext = mcontext;
        layoutInflater=LayoutInflater.from(mcontext);
    }

    @NonNull
    @Override
    public ReplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        convertView=layoutInflater.from(mcontext).inflate(R.layout.comment_item_layout,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyAdapter.ViewHolder viewHolder, int i) {
        ReplyDetailBean bean=replyDetailBeans.get(i);
        viewHolder.usernameView.setText(bean.getUsername());
        viewHolder.datetimeView.setText(DateTimeUtil.handlerDateTime(bean.getRcreateTime()));
        viewHolder.userImgView.setImageURL(bean.getUimg());
        viewHolder.replyContentView.setText(bean.getContent());
    }

    @Override
    public int getItemCount() {
        return replyDetailBeans.size();
    }

    public void setReplyDetailBeans(List<ReplyDetailBean> replyDetailBeans) {
        this.replyDetailBeans.addAll(replyDetailBeans);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        MyImageView userImgView;
        TextView usernameView;
        TextView datetimeView;
        TextView replyContentView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImgView=itemView.findViewById(R.id.comment_item_logo);
            usernameView=itemView.findViewById(R.id.comment_item_userName);
            datetimeView=itemView.findViewById(R.id.comment_item_time);
            replyContentView=itemView.findViewById(R.id.comment_item_content);
        }
    }
}
