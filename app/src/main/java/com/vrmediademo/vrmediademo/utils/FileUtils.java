package com.vrmediademo.vrmediademo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by djf on 2017/5/4.
 */

public class FileUtils {
    private static final String TAG = "FileUtils";
    public static ArrayList<String> getDiskPath() {
        ArrayList<String> list = new ArrayList<>();
        File file = new File("/proc/mounts");
        try {
            if (file.canRead()) {
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String lines;
                while ((lines = reader.readLine()) != null) {
                    String[] parts = lines.split("\\s+");
                    //                    list.add(lines);
                    if (parts.length >= 2 && parts[0].contains("vold") || parts[0].contains
                            ("fuse")) {
//                        File file1 = new File(parts[1]);
//                        if (file1.getTotalSpace() > 0) {
//                            list.add(file1.getAbsolutePath());
//                        }

                        list.add(parts[1]);
                    }
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getFilePath(File file) {
        ArrayList<String> list = new ArrayList<>();
        if (file.isFile() && (file.getName().endsWith("mp4") || file.getName().endsWith("MP4"))) {
            list.add(file.getAbsolutePath());
//            Log.d(TAG, "getFilePath: "+file.getAbsolutePath());
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    list.addAll(getFilePath(files[i]));
                }
            }
        }
        return list;
    }


}
