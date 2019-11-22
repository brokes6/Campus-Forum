package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bottomnavigationabar2.MyImageView;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.bean.UserMessage;
import com.example.bottomnavigationabar2.dto.CommentDto;
import com.example.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        holder.username.setText(userMessages.get(position).getUsername());
        holder.datetime.setText(DateTimeUtil.handlerDateTime(userMessages.get(position).getCreateTime()));
        holder.content.setText(userMessages.get(position).getContent());
        holder.postContent.setText(userMessages.get(position).getRepliesContent());
        holder.userImg.setImageURL(userMessages.get(position).getUimg());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        MyImageView userImg;
        TextView username;
        TextView datetime;
        TextView content;
        TextView postContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            userImg=itemView.findViewById(R.id.news_user_img);
            username=itemView.findViewById(R.id.news_username);
            datetime=itemView.findViewById(R.id.news_time);
            content=itemView.findViewById(R.id.news_user_text);
            postContent=itemView.findViewById(R.id.postContent);
        }
    }
}
