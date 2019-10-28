package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.bean.CommentDetailBean;
import com.example.bottomnavigationabar2.bean.ReplyDetailBean;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;

/**
 * Author: fuxinbo
 * Desc: 评论与回复列表的适配器
 */

public class CommentExpandAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "CommentExpandAdapter";
    private List<CommentDetailBean> commentBeanList;
    private Context context;
    private int pageIndex = 1;

    public CommentExpandAdapter(Context context, List<CommentDetailBean> commentBeanList) {
        this.context = context;
        this.commentBeanList = commentBeanList;
    }

    public void setCommentBeanList(List<CommentDetailBean> commentBeanList) {
        if(commentBeanList==null){
            commentBeanList=new ArrayList<>();
        }
        this.commentBeanList.addAll(commentBeanList);
    }

    public List<CommentDetailBean> getCommentBeanList() {
        return commentBeanList;
    }

    @Override
    public int getGroupCount() {
        return commentBeanList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        Log.i(TAG, "getChildrenCount: -----");
        if(commentBeanList.get(i).getReplyVoList() == null){
            return 0;
        }else {
            Log.i(TAG, "getChildrenCount:进入");
            return commentBeanList.get(i).getReplyVoList().size()>0 ? commentBeanList.get(i).getReplyVoList().size():0;
        }

    }

    @Override
    public Object getGroup(int i) {
        return commentBeanList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return commentBeanList.get(i).getReplyVoList().get(i1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    boolean isLike = false;

    @Override
    public View getGroupView(final int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
        final GroupHolder groupHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, viewGroup, false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        }else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
//        Glide.with(context).load(R.drawable.user_other)
//                //不清楚，但注释掉没问题
//               .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .error(R.mipmap.ic_launcher)
//                .centerCrop()
//                .into(groupHolder.logo);
        groupHolder.tv_name.setText(commentBeanList.get(groupPosition).getUsername());
        groupHolder.tv_time.setText(commentBeanList.get(groupPosition).getCcreateTime());
        groupHolder.tv_content.setText(commentBeanList.get(groupPosition).getContent());
        groupHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLike){
                    isLike = false;
                    Toast.makeText(context, "取消赞", Toast.LENGTH_SHORT).show();
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#aaaaaa"));
                }else {
                    isLike = true;
                    Toast.makeText(context, "点赞", Toast.LENGTH_SHORT).show();
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#FF5C5C"));
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final ChildHolder childHolder;
        Log.i(TAG, "getChildView: 11111111111111");
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout,viewGroup, false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        }
        else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        String replyUser = commentBeanList.get(groupPosition).getReplyVoList().get(childPosition).getUsername();
        if(!TextUtils.isEmpty(replyUser)){
            childHolder.tv_name.setText(replyUser + ":");
        }else {
            childHolder.tv_name.setText("无名"+":");
        }

        childHolder.tv_content.setText(commentBeanList.get(groupPosition).getReplyVoList().get(childPosition).getContent());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupHolder{
        private CircleImageView logo;
        private TextView tv_name, tv_content, tv_time;
        private ImageView iv_like;
        public GroupHolder(View view) {
            logo = (CircleImageView) view.findViewById(R.id.comment_item_logo);
            tv_content = (TextView) view.findViewById(R.id.comment_item_content);
            tv_name = (TextView) view.findViewById(R.id.comment_item_userName);
            tv_time = (TextView) view.findViewById(R.id.comment_item_time);
            iv_like = (ImageView) view.findViewById(R.id.comment_item_like);
        }
    }

    private class ChildHolder{
        private TextView tv_name, tv_content;
        public ChildHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.reply_item_user);
            tv_content = (TextView) view.findViewById(R.id.reply_item_content);
        }
    }


    /**
     * by moos on 2018/04/20
     * func:评论成功后插入一条数据
     * @param commentDetailBean 新的评论数据
     */
    public void addTheCommentData(CommentDetailBean commentDetailBean){
        if(commentDetailBean!=null){
            commentBeanList.add(0,commentDetailBean);
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("评论数据为空!");
        }

    }

    /**
     * by moos on 2018/04/20
     * func:回复成功后插入一条数据
     * @param replyDetailBean 新的回复数据
     */
    public void addTheReplyData(ReplyDetailBean replyDetailBean, int groupPosition){
        if(replyDetailBean!=null){
            Log.e(TAG, "addTheReplyData: >>>>该刷新回复列表了:"+replyDetailBean.toString() );
            if(commentBeanList.get(groupPosition).getReplyVoList() != null ){
                commentBeanList.get(groupPosition).getReplyVoList().add(replyDetailBean);
            }else {
                List<ReplyDetailBean> replyList = new ArrayList<>();
                replyList.add(replyDetailBean);
                commentBeanList.get(groupPosition).setReplyVoList(replyList);
            }
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("回复数据为空!");
        }

    }

    /**
     * by moos on 2018/04/20
     * func:添加和展示所有回复
     * @param replyBeanList 所有回复数据
     * @param groupPosition 当前的评论
     */
    private void addReplyList(List<ReplyDetailBean> replyBeanList, int groupPosition){
        if(commentBeanList.get(groupPosition).getReplyVoList() != null ){
            commentBeanList.get(groupPosition).getReplyVoList().clear();
            commentBeanList.get(groupPosition).getReplyVoList().addAll(replyBeanList);
        }else {

            commentBeanList.get(groupPosition).setReplyVoList(replyBeanList);
        }

        notifyDataSetChanged();
    }
}
