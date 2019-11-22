package com.application;

import android.app.Application;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/13
 */
public class AppApplication extends Application {
    private static final String TAG = "AppApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        Log.i(TAG, "onCreate: 被调用");
        initImageLoader();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }
}
