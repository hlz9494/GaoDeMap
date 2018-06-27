package com.hlz.gaodemap;

import android.app.Application;

/**
 * Created by A on 2017-09-12.
 */

public class App extends Application {
    private static App app;

    public static App getApp(){
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;

    }
}
