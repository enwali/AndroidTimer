package com.wp.demo.base;

import android.app.Application;

import com.wp.demo.utils.SpModel;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    private static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        SpModel.init(app);
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }

    public static Application getInstance() {
        return app;
    }
}
