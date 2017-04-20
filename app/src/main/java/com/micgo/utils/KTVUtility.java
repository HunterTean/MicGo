package com.micgo.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by liuhongtian on 17/4/19.
 */

public class KTVUtility {

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

}
