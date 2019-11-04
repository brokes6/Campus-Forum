package com.example.bottomnavigationabar2.MoBan;

import android.support.v7.widget.RecyclerView;


public interface MoBanInterface {
    public static final int NOTIFY=0;
    public static final int SHOWTOAST=1;
    public void getPostList(String token);
    public void clearList();
    public RecyclerView getRecycler();
}
