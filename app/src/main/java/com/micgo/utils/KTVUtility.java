package com.micgo.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liuhongtian on 17/4/19.
 */

public class KTVUtility {

    private static KTVUtility ktvUtility = null;

    private KTVUtility() {}

    public static KTVUtility getInstance() {
        if (ktvUtility == null) {
            ktvUtility = new KTVUtility();
        }
        return ktvUtility;
    }

    public static int pixelToDp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int dpToPixel(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static File getMGFile(String fileName) {
        String path = getMGFileDir().getAbsolutePath() + File.separator + fileName;
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }

        return file;
    }

    public static File getMGFileDir() {
        File sdDir = getSDPath();
        if (sdDir != null && sdDir.isDirectory()) {
            File ktv = new File(sdDir, "mg");
            if (!ktv.exists())
                ktv.mkdirs();
            return ktv;
        }
        return null;
    }

    public static File getSDPath() {
        File sdDir = null;
        try {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } catch (NullPointerException e) {
            sdDir = new File("/mnt/sdcard"); //4.0
            if(sdDir==null || !sdDir.exists()){
                sdDir = new File("/storage/emulated/0"); //4.2
            }
            if(sdDir==null || !sdDir.exists()){
                sdDir = new File("/storage/sdcard0"); //4.1
            }
        }
        return sdDir;
    }

    public final static String getMD5Hex(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }

        byte data[];
        try {
            data = getMD5(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            data = getMD5(str.getBytes());
        }
        StringBuilder sb = new StringBuilder();
        for (byte aData : data) {
            sb.append(Integer.toString((aData & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static byte[] getMD5(byte data[]) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(data);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isInitScreenParam = false;
    private int width;
    private int height;
    private int densityDpi;
    private float density;

    public void initScreenParams(Display display) {
        DisplayMetrics metric = new DisplayMetrics();
        display.getMetrics(metric);
        if (!isInitScreenParam) {
            isInitScreenParam = true;
            this.width = metric.widthPixels; // 屏幕宽度（像素）
            this.height = metric.heightPixels; // 屏幕高度（像素） metric.heightPixels
            // 包括信息栏
            this.densityDpi = metric.densityDpi;
            this.density = metric.density;

            KTVLog.d("KTVUtility", "densityDpi = " + metric.densityDpi + ", density = " + metric.density + ", scaleDensity : " + metric.scaledDensity);
            KTVLog.d("KTVUtility", "width = " + width + " | height = " + height);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
