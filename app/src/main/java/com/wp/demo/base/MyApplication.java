package com.wp.demo.base;

import android.app.Application;

import com.wp.demo.utils.SpModel;

public class MyApplication extends Application {

    private static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        SpModel.init(app);
    }

    public static Application getInstance() {
        return app;
    }
}
