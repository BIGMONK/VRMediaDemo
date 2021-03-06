package com.vrmediademo.vrmediademo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
    /**
     * sharp电视外部存储卡
     */
    public static String EXTERNAL_SD_TV = "sdcard";
    /**
     * sharp电视外接usb存储
     */
    public static String USB_DISK_TV1 = "sda1";
    /**
     * sharp电视外接usb存储
     */
    public static String USB_DISK_TV2 = "sda2";
    /**
     * 盒子外部存储卡
     */
    public static String EXTERNAL_SD = "external_sd";
    /**
     * 盒子内部存储卡
     */
    public static String INTERNAL_SD = "internal_sd";
    /**
     * 盒子外接usb存储
     */
    public static String USB_DISK2 = "USB_DISK2";
    /**
     * 盒子外接usb存储
     */
    public static String USB_DISK3 = "USB_DISK3";
    /**
     * 盒子外接usb存储
     */
    public static String USB_STORAGE = "usb_storage";
    /**
     * 乐视路径适配U盘正则表达式
     * 乐视：/mnt/usb/82D9-2459
     */
    static Pattern LeTVUDisk = Pattern.compile("^/mnt/usb/[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}$");
    /**
     * 乐视路径适配硬盘和SD卡正则表达式：9EEE4AA6EE4A1745
     * 乐视：/mnt/usb/9EEE4AA6EE4A1745
     */
    static Pattern LeTVNoUDisk = Pattern.compile("^/mnt/usb/([0-9a-zA-Z]{16})$");
    /**
     * 小米电视(6.0.1)路径适配U盘正则表达式 ：82D9-2459
     * 小米：/storage/82D9-2459
     */
    static Pattern MiTVUDisk = Pattern.compile("^/storage/[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}$");
    /**
     * 小米电视(6.0.1)路径适配硬盘和SD卡正则表达式：9EEE4AA6EE4A1745
     * 小米：/storage/9EEE4AA6EE4A1745
     */
    static Pattern MiTVNoUDisk = Pattern.compile("^/storage/([0-9a-zA-Z]{16})$");
    /**
     * 小米盒子
     * /storage/usbotg/usbotg-sdb1       读卡器
     * /storage/usbotg/usbotg-sda1      U盘
     */
    static String MiBOXOTG = "usbotg";
    public static String GET_LOCAL_BASEPATH() {
        try {
            File file = new File("/proc/mounts");
            if (file.canRead()) {
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String lines;
                while ((lines = reader.readLine()) != null) {
                    String[] parts = lines.split("\\s+");
                    if (parts.length >= 2 && parts[0].contains("vold") || parts[0].contains
                            ("fuse")) {
                        if (parts[1].contains(EXTERNAL_SD)) {
//                            result.put(EXTERNAL_SD, parts[1]);
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        } else if (parts[1].contains(USB_DISK2)) {
//                            result.put(USB_DISK2, parts[1]);
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        } else if (parts[1].contains(USB_DISK3)) {
//                            result.put(USB_DISK3, parts[1]);
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        } else if (parts[1].contains(USB_STORAGE)) {
//                            result.put(USB_STORAGE, parts[1]);
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        }
                        //else if (parts[1].contains(INTERNAL_SD)) {
//                            result.put(INTERNAL_SD, parts[1]);
//                        }
                        else if (parts[1].contains(USB_DISK_TV1)) {
//                            result.put(USB_DISK_TV1, "/storage/sda1");
//                            return "/storage/sda1";
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        } else if (parts[1].contains(USB_DISK_TV2)) {
//                            result.put(USB_DISK_TV2, "/storage/sda2");
//                            return "/storage/sda2";
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        } else if (parts[1].contains(EXTERNAL_SD_TV)) {
//                            result.put(EXTERNAL_SD_TV, "/storage/sdcard");
//                            return "/storage/sdcard";
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        } else if (LeTVNoUDisk.matcher(parts[1]).matches()) {//乐视电视
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        } else if (LeTVUDisk.matcher(parts[1]).matches()) {//乐视电视
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        } else if (MiTVUDisk.matcher(parts[1]).matches()) {//小米电视
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        } else if (MiTVNoUDisk.matcher(parts[1]).matches()) {//小米电视
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        } else if (parts[1].contains(MiBOXOTG)) {//小米电视
                            File file1 = new File(parts[1]);
                            if (file1.exists() && file1.getTotalSpace() > 0)
                                return parts[1];
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
