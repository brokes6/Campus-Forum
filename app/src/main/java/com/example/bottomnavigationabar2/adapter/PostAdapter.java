package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bottomnavigationabar2.MoerReply;
import com.example.bottomnavigationabar2.MyImageView;
import com.example.bottomnavigationabar2.PostDetails;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.bean.UserMessage;
import com.example.bottomnavigationabar2.dto.CommentDto;
import com.example.util.DateTimeUtil;
import com.example.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private static final String TAG = "PostAdapter";
    private List<UserMessage> userMessages=new ArrayList<>();
    private View convertView;
    private Context mcontext;
    private LayoutInflater inflater;
    public PostAdapter(Context context) {
        mcontext=context;
        inflater=LayoutInflater.from(context);
    }

    public void setUserMessages(List<UserMessage> userMessages) {
        this.userMessages.addAll(userMessages);
    }

    @Override
    public int getItemCount() {
        return userMessages.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        convertView=inflater.inflate(R.layout.news_template,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserMessage userMessage=userMessages.get(position);
        final int action=userMessage.getAction();
        final Map<String,Integer>map= MessageUtil.convertReplyMap(userMessage.getMapId());
        handlerIntroduce(holder.introduce,action);
        holder.username.setText(userMessage.getUsername());
        holder.datetime.setText(DateTimeUtil.handlerDateTime(userMessage.getCreateTime()));
        holder.content.setText(userMessage.getContent());
        holder.postContent.setText(Html.fromHtml(userMessage.getRepliesContent()));
        holder.postContent.setMaxLines(1);
        holder.postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: 被点击了");
                Intent intent=null;
                switch (action){
                    case 1:
                        intent=new Intent(mcontext, PostDetails.class);
                        intent.putExtra("postId",map.get("postId"));
                        break;
                    case 2:
                        intent=new Intent(mcontext, MoerReply.class);
                        intent.putExtra("postId",map.get("postId"));
                        intent.putExtra("cid",map.get("commentId"));
                        intent.putExtra("new",true);
                        break;
                }
                mcontext.startActivity(intent);
            }
        });
        String uimg=userMessages.get(position).getUimg();
        if(uimg!=null&&!uimg.trim().equals(""))
            holder.userImg.setCacheImageURL(uimg);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        MyImageView userImg;
        TextView username,datetime,content,postContent,introduce;
        LinearLayout postLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            userImg=itemView.findViewById(R.id.news_user_img);
            username=itemView.findViewById(R.id.news_username);
            datetime=itemView.findViewById(R.id.news_time);
            content=itemView.findViewById(R.id.news_user_text);
            postContent=itemView.findViewById(R.id.postContent);
            postLayout=itemView.findViewById(R.id.postLayout);
            introduce=itemView.findViewById(R.id.introduce);
        }
    }
    private void handlerIntroduce(TextView introduce,int action){
        switch (action){
            case 1:
                introduce.setText("评论我的帖子");
                break;
            case 2:
                introduce.setText("回复我的评论");
                break;
            default:
                break;
        }
    }
}
