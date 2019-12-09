package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bottomnavigationabar2.MyImageView;
import com.example.bottomnavigationabar2.Post;
import com.example.bottomnavigationabar2.PostDetails;
import com.example.bottomnavigationabar2.R;
import com.example.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private static final String TAG = "HistoryAdapter";
    private Context mcontext;
    private LayoutInflater inflater;
    private List<Post> posts=new ArrayList<>();
    private View convertView;
    public HistoryAdapter(Context mcontext) {
        this.mcontext = mcontext;
        this.inflater = LayoutInflater.from(mcontext);
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        convertView=inflater.inflate(R.layout.history_post_item,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder viewHolder, final int i) {
        final Post post=posts.get(i);
        viewHolder.datetimeView.setText(DateTimeUtil.handlerDateTime(post.getPcreateTime()));
        viewHolder.contentView.setText(post.getContent());
        viewHolder.usernameView.setText(post.getUsername());
        String userImg=post.getUimg();
        if(userImg!=null&&!userImg.trim().equals(""))
            viewHolder.imageView.setCacheImageURL(userImg);
        viewHolder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext, PostDetails.class);
                intent.putExtra("postId",post.getPid());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<Post> posts) {
        this.posts.addAll(posts);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout contentLayout;
        private MyImageView imageView;
        private TextView usernameView;
        private TextView contentView;
        private TextView datetimeView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.userImg);
            usernameView=itemView.findViewById(R.id.username);
            contentView=itemView.findViewById(R.id.content);
            datetimeView=itemView.findViewById(R.id.datetime);
            contentLayout=itemView.findViewById(R.id.contentLayout);
        }


    }
}
