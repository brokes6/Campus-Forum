package com.example.bottomnavigationabar2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bottomnavigationabar2.MyImageView;
import com.example.bottomnavigationabar2.R;
import com.example.bottomnavigationabar2.OrganizationActivity;
import com.example.bottomnavigationabar2.bean.Organization;

import java.util.ArrayList;
import java.util.List;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.ViewHolder> {
    private static final String TAG = "OrganizaionAdapter";
    private View convertView;
    private LayoutInflater inflater;
    private List<Organization> organizations=new ArrayList<>();
    private Context context;
    private int layoutId;
    public OrganizationAdapter(Context context,int mode) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.context=context;
        if(mode==0){
            layoutId=R.layout.organization_recommend_template;
        }else {
            layoutId=R.layout.organization_template;
        }
    }

    public OrganizationAdapter() {
        Log.i(TAG, "OrganizationAdapter: 我真的受不了了");
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations.addAll(organizations);
        Log.i(TAG, "setOrganizations: 我被设置了啊"+this.organizations.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.i(TAG, "onCreateViewHolder: 开始创建拉");
        convertView=inflater.inflate(layoutId,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final Organization organization=organizations.get(position);
        viewHolder.myImageView.setCacheImageURL(organization.getOimg());
        viewHolder.organizationName.setText(organization.getOname());
        viewHolder.introduce.setText(organization.getOintroduce());
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OrganizationActivity.class);
                intent.putExtra("oid",organization.getOid());
                intent.putExtra("oname",organization.getOname());
                intent.putExtra("oimg",organization.getOimg());
                context.startActivity(intent);
            }
        });
        if(layoutId==R.layout.organization_recommend_template){
            viewHolder.followNum.setText(String.valueOf(organization.getLoveNum()));
            viewHolder.postNum.setText(String.valueOf(organization.getPnum()));
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: 嗡嗡嗡嗡嗡嗡");
        return organizations.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        MyImageView myImageView;
        TextView organizationName,introduce,postNum,followNum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.linerLayout);
            myImageView=itemView.findViewById(R.id.organizationImg);
            organizationName=itemView.findViewById(R.id.organizationName);
            introduce=itemView.findViewById(R.id.introduce);
            postNum=itemView.findViewById(R.id.postNum);
            followNum=itemView.findViewById(R.id.followNum);
        }
    }
    private int getListSize(List<Organization> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }
}
