package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;
import com.example.bottomnavigationabar2.MyImageView;
import com.example.bottomnavigationabar2.Post;
import com.example.bottomnavigationabar2.PostDetails;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.model.NineGridTestModel;
import com.example.bottomnavigationabar2.view.NineGridTestLayout;
import com.example.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Request;

/**
 * Created by HMY on 2016/8/6
 */
public class NineGridTest2Adapter extends RecyclerView.Adapter<NineGridTest2Adapter.ViewHolder> {
    private  Boolean oh =true;
    private  Boolean oh2 =true;
    private ImageView shoucang;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout2;
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
        linearLayout = convertView.findViewById(R.id.Lin_comment);
        linearLayout2 = convertView.findViewById(R.id.Lin_give_the_thumbs_up);
        Log.i(TAG, "onCreateViewHolder:111");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder: 开始创建"+position);
        holder.content.setText(Html.fromHtml(mList.get(position).getContent()));
        holder.datetime.setText(DateTimeUtil.handlerDateTime(mList.get(position).getPcreateTime()));
        holder.uimg.setImageURL(mList.get(position).getUimg());
        holder.username.setText(mList.get(position).getUsername());
        holder.postId=mList.get(position).getPid();
        holder.layout.setIsShowAll(mList.get(position).isShowAll());
        holder.loveNumStr.setText(String.valueOf(mList.get(position).getLoveCount()));
        holder.layout.setUrlList(Arrays.asList(mList.get(position).getImgUrl().split(",")));
        linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (oh){
                    holder.loveNum.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dianzanwanc));
                    oh=false;
                }else{
                    holder.loveNum.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dianzan));
                    oh=true;
                }
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oh2==true){
                    holder.collection.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shocangwanc));
                    oh2=false;
                    return;
                }else if(oh2==false){
                    holder.collection.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shocang));
                    oh2=true;
                    return;
                }
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: 随便响应"+mList.get(position).getPid());
                Intent intent = new Intent(mContext, PostDetails.class);
                intent.putExtra("postId",mList.get(position).getPid());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return getListSize(mList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        NineGridTestLayout layout;
        MyImageView uimg;
        TextView datetime,content,username,loveNumStr,collectionStr,talkNumStr;
        ImageView loveNum,collection;
        int postId;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
            layout = (NineGridTestLayout) itemView.findViewById(R.id.layout_nine_grid);
            username=itemView.findViewById(R.id.tiezi_username);
            uimg=itemView.findViewById(R.id.tieze_user_img);
            datetime=itemView.findViewById(R.id.tiezi_time);
            content=itemView.findViewById(R.id.tieze_Text);
            loveNum=itemView.findViewById(R.id.loveNum);
            collection=itemView.findViewById(R.id.collection);
            loveNumStr=itemView.findViewById(R.id.loveNumStr);
            collectionStr=itemView.findViewById(R.id.collectionStr);
            talkNumStr=itemView.findViewById(R.id.talkNumStr);
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
/*    private void updatePostLove(int postId,String token){
        Request request = new Request.Builder()
                .url()
    }*/
}
