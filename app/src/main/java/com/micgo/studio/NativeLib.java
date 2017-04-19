package com.micgo.studio;

/**
 * Created by liuhongtian on 17/2/25.
 */

public class NativeLib {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private NativeLib() {}

    public static NativeLib getInstance() {
        return LazyHolder.INST;
    }

    private static class LazyHolder {
        private static final NativeLib INST;

        static  {
            INST = new NativeLib();
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

}
