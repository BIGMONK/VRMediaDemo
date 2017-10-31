package com.vrmediademo.vrmediademo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.orhanobut.logger.Logger;

/**
 * Created by djf on 2016/8/9.
 */
public class VersionUtils {


    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Logger.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            Logger.e("VersionInfo", "Exception", e);
        }
        return versionCode;
    }

    /**
     * 返回当前程序版本号
     */
    public static String getAppName(Context context) {
        String  name = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            name = (String) pm.getApplicationLabel(applicationInfo);
        } catch (Exception e) {
            Logger.e("VersionInfo", "Exception", e);
        }
        return name;
    }
}
