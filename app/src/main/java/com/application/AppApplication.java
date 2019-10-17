package com.application;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/13
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("爷呗调用了");
        initImageLoader();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

    }
}
