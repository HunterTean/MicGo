package com.micgo.utils;

import android.util.Log;

/**
 * Created by liuhongtian on 17/2/25.
 */

public class KTVLog {

    public static void d(String message) {
        Log.d("MicGo", message);
    }

    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

}
