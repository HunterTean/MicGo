package com.micgo.studio;

/**
 * Created by liuhongtian on 17/2/25.
 */

public class NativeGo {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("nativego");
    }

    private NativeGo() {}

    public static NativeGo getInstance() {
        return LazyHolder.INST;
    }

    private static class LazyHolder {
        private static final NativeGo INST;

        static  {
            INST = new NativeGo();
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native void createThread();

}
