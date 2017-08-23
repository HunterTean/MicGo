package com.micgo.utils;

/**
 * Created by liuhongtian on 17/5/8.
 */

public class DownloadExceptionLogger {

    private DownloadExceptionLogger(){

    }

    public static DownloadExceptionLogger getInstance() {
        return LazyHolder.INST;
    }

    private static class LazyHolder {
        private static final DownloadExceptionLogger INST;

        static  {
            INST = new DownloadExceptionLogger();
        }
    }

}
