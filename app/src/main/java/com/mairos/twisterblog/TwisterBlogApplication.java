package com.mairos.twisterblog;

import android.app.Application;

/**
 * Created by Mike on 17.01.2015.
 */
public class TwisterBlogApplication extends Application {

    public static TwisterBlogApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
