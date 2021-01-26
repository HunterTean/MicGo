//
// Created by 刘洪天 on 18/1/10.
//

#include <jni.h>
#include <string>
#include "../libcommon/common_tools.h"
#include "demo/cube_controller.h"

#include <android/native_window.h>
#include <android/native_window_jni.h>

static ANativeWindow *window = 0;
CubeController* cubeController;

#define LOG_TAG "CubeController_JNI"

extern "C"
JNIEXPORT jstring JNICALL Java_com_micgo_studio_gl_GLESController_test(JNIEnv *env, jobject instance) {
    std::string hello = "Hello from gl_controller";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_micgo_studio_gl_GLESController_test2(JNIEnv *env, jobject instance) {
    std::string hello = "Hello from gl_controller_2";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_gl_GLESController_prepareEGLContext(JNIEnv *env, jobject instance,
                                                          jobject surface, jint width,
                                                          jint height) {
    if (surface != NULL) {
        window = ANativeWindow_fromSurface(env, surface);
        LOGI("Got window %p", window);

        cubeController = new CubeController();
        cubeController->prepareEGLContext(window, width, height);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_gl_GLESController_showCube(JNIEnv *env, jobject instance) {



}

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_gl_GLESController_destroyEGLContext(JNIEnv *env, jobject instance) {

    if(window){
        ANativeWindow_release(window);
        window = NULL;
    }

}