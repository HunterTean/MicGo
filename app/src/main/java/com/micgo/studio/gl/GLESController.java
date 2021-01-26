package com.micgo.studio.gl;

import android.view.Surface;

/**
 * Created by liuhongtian on 18/1/10.
 */

public class GLESController {

    private GLESController() {}

    public static GLESController getInstance() {
        return GLESController.LazyHolder.INST;
    }

    private static class LazyHolder {
        private static final GLESController INST;

        static  {
            INST = new GLESController();
        }
    }

    public native String test();

    public native String test2();

    public native void prepareEGLContext(Surface surface, int width, int height);

    public native void destroyEGLContext();

    public native void showCube();

    public native void prepareWave(Surface surface, int width, int height);

    public native void showWave();

    public native void destroyWave();

}
