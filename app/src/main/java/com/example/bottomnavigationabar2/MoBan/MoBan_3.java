package com.example.bottomnavigationabar2.MoBan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bottomnavigationabar2.R;

public class MoBan_3 extends Fragment implements MoBanInterface{
    private View view;

    @Override
    public RecyclerView getRecycler() {
        return null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mo_ban_3, container, false);
        return view;
    }

    @Override
    public void getPostList() {

    }
    @Override
    public void clearList() {
       /* mList.clear();*/
    }
}
