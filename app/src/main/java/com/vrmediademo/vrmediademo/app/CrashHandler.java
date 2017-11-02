package com.vrmediademo.vrmediademo.app;

import android.os.Build;
import android.util.Log;

import com.vrmediademo.vrmediademo.BuildConfig;
import com.vrmediademo.vrmediademo.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 异常捕获
 * Created by hzwangchenyan on 2016/1/25.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public static CrashHandler getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static CrashHandler instance = new CrashHandler();
    }

    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        saveCrashInfo(ex);
        mDefaultHandler.uncaughtException(thread, ex);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");

    private void saveCrashInfo(Throwable ex) {
        String stackTrace = Log.getStackTraceString(ex);
        String filePath = FileUtils.GET_LOCAL_BASEPATH()
                + "/Crash" + BuildConfig.VERSION_NAME + "_" + BuildConfig.VERSION_CODE + "_"
                + sdf.format(System.currentTimeMillis()) + ".txt";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
            bw.write("*** crash  " + BuildConfig.VERSION_NAME + " ***\n");
            bw.write(getInfo());
            bw.write(stackTrace);
            bw.newLine();
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getInfo() {
        String time = sdf.format(new Date());
        String version = "App versionName: " + BuildConfig.VERSION_NAME + "    versionCode:" +
                BuildConfig.VERSION_CODE;
        String device = "Vendor: " + Build.MANUFACTURER
                + " \nModel: " + Build.MODEL
                + " \nPRODUCT: " + Build.PRODUCT
                + " \nBOARD: " + Build.BOARD
                + " \nDEVICE: " + Build.DEVICE
                + " \nBRAND: " + Build.BRAND
                + " \nOS Version: " + Build.VERSION.RELEASE
                + " \nHARDWARE:  " + Build.HARDWARE
                + " \nCPU ABI:  " + Build.CPU_ABI;

        StringBuilder sb = new StringBuilder();
        sb.append("*** time: ").append(time).append(" ***").append("\n\n");
        sb.append("*** version: ").append(version).append(" ***").append("\n\n");
        sb.append("*** device*** \n").append(device).append("\n\n\n");

//        Process pp = null;
//        try {
//            pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
//            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
//            LineNumberReader input = new LineNumberReader(ir);
//            String ss;
//            while ((ss = input.readLine()) != null) {
//                sb.append(ss+"\n");
//            }
//            sb.append("\n\n\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        return sb.toString();
    }
}
