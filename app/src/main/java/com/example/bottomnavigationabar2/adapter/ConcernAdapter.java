package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.bottomnavigationabar2.MyImageView;
import com.example.bottomnavigationabar2.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ConcernAdapter extends RecyclerView.Adapter<ConcernAdapter.ViewHolder>{
    private static final String TAG = "ConcernAdapter";
    private Context lcontext;
    public ConcernAdapter(Context context){
        lcontext=context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.image.setBackgroundResource(R.drawable.filter);
        viewHolder.fsshu.setText("13");
        viewHolder.gzshu.setText("13");
        viewHolder.qm.setText("这是个性签名");
        viewHolder.name.setText("你的名字");
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(lcontext).inflate(R.layout.concernitem,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView gzshu,fsshu,name,qm;
        MyImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           this.view=itemView;
            gzshu=(TextView)itemView.findViewById(R.id.concern_gz);
            fsshu=(TextView)itemView.findViewById(R.id.concern_fs);
            name=(TextView)itemView.findViewById(R.id.concern_name);
            qm=(TextView)itemView.findViewById(R.id.concern_qm);
            image=(MyImageView) itemView.findViewById(R.id.concern_img);
        }

    }
}

