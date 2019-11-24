package com.example.bottomnavigationabar2.MoBan;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;


public interface PostTemplateInterface {
    public static final String STANDARD_POPULAR_URL="http://106.54.134.17/app/getPopularPost";
    public static final String STANDARD_NEW_URL="http://106.54.134.17/app/getNewPost";
    public static final String TAG_POPULAR_URL="http://106.54.134.17/app/getPopularPostByTag";
    public static final String TAG_NEW_URL="http://106.54.134.17/app/getNewPostByTag";
    public static final int NOTIFY=0;
    public static final int SHOWTOAST=1;
    public void getPostList(String token);
    public void clearList();
    public RecyclerView getRecycler();
    public void updateInfo(Intent intent);
}
