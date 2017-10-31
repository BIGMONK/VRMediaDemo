package com.vrmediademo.vrmediademo.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by djf on 2017/8/22.
 */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppCache.init(this);
        CrashReport.initCrashReport(getApplicationContext(), "f30b450220", true);
    }
}
