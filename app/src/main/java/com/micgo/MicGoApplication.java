package com.micgo;

import android.app.Application;
import android.util.DisplayMetrics;
import android.view.Display;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.micgo.utils.KTVLog;

/**
 * Created by liuhongtian on 17/5/8.
 */

public class MicGoApplication extends Application {

    private static Gson gson;

    private static MicGoApplication application;

    public static MicGoApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        KTVLog.d("Tian", " KTV application onc reate");
    }



    public static Gson getGson() {
        if (gson == null) {
            return (new GsonBuilder()).create();
        }
        return gson;
    }

}
