/*
 * Copyright 2014-2024 setNone. All rights reserved.
 */
package com.king.smart.car.mysmartcarapp.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * 应用程序类，用于全局初始化
 */
public class App extends Application {
    private static final String TAG = "Application App";
    public static App instance = null;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        new Runnable() {
            @Override
            public void run() {
                SDKInitializer.initialize(instance);
            }
        }.run();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}
