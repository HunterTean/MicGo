package com.micgo.studio.gl;

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

}
