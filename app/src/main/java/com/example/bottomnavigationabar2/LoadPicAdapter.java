package com.example.bottomnavigationabar2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class LoadPicAdapter extends RecyclerView.Adapter<LoadPicAdapter.MyViewHolder> {
    private static final String TAG = "LoadPicAdapter";
    private MyViewHolder holder;
    Context context;
    List<LoadFileVo> fileList = null;
    View view;
    int index=0;
    int picNum = 8;//列表的图片个数最大值

    public MyViewHolder getHolder() {
        return holder;
    }

    public void setHolder(MyViewHolder holder) {
        this.holder = holder;
    }

    public LoadPicAdapter(Context context, List<LoadFileVo> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    public LoadPicAdapter(Context context, List<LoadFileVo> fileList, int picNum) {
        this.context = context;
        this.fileList = fileList;
        this.picNum = picNum;
    }

    public interface OnItemClickListener {
        void click(View view, int positon);
        void del(View view);
    }

    OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: 1111");
        view = LayoutInflater.from(context).inflate(R.layout.load_item_pic, parent, false);
        holder= new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder:下标"+position);
//        //通过默认设置第一个为空文件为添加退保，且在文件个数小于最大限制值的情况。当图片个数等于最大限制值，第一个则不是添加按钮
//        if (position == 0&&fileList.get(position).getPath()==null) {
//            holder.ivPic.setImageResource(R.drawable.jiahao);//加号图片
//            holder.ivPic.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.click(view, position);
//                }
//            });
//            holder.ivDel.setVisibility(View.INVISIBLE);
//            holder.bg_progressbar.setVisibility(View.GONE);
//        } else {
//            holder.ivPic.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.click(view, position);
//                }
//            });
//            holder.ivDel.setVisibility(View.VISIBLE);
//            holder.bg_progressbar.setVisibility(View.VISIBLE);
//            holder.bg_progressbar.setProgress(fileList.get(position).getPg());
//        }
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.click(view, position);
            }
        });
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断图片是否上传，上传后将无法删除
                if (fileList.get(position).isUpload()) {
                    Toast.makeText(context, "该图片已上传！", Toast.LENGTH_SHORT).show();
                } else {
                    fileList.remove(position);
                    if (fileList.size()==picNum-1&&fileList.get(0).getBitmap()!=null){
                        fileList.add(0,new LoadFileVo());
                    }//如果数量达到最大数时，前面的加号去掉，然后再减去时，则添加前面的加号
                    notifyDataSetChanged();
                    if (listener!=null){
                        listener.del(view);//传递接口，计算图片个数显示在界面中
                    }

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "MyViewHolder";
        MyImageView ivPic;
        MyImageView ivDel;
        ProgressBar bg_progressbar;
        View view;
        MyViewHolder(View view) {
            super(view);
            this.view = view;
            Log.i(TAG, "MyViewHolder: 被创建拉");
            ivPic = (MyImageView) view.findViewById(R.id.ivPic);
            ivDel = (MyImageView) view.findViewById(R.id.ivDel);
            bg_progressbar= (ProgressBar) view.findViewById(R.id.bg_progressbar);
        }
    }
}
