package com.micgo;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    }

    public static Gson getGson() {
        if (gson == null) {
            return (new GsonBuilder()).create();
        }
        return gson;
    }

}
