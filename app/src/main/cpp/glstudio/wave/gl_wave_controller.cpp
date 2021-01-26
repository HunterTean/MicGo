//
// Created by 刘洪天 on 18/1/10.
//

#include <jni.h>
#include <string>
#include "../../libcommon/common_tools.h"
#include "wave_render.h"

#include <android/native_window.h>
#include <android/native_window_jni.h>

static ANativeWindow *window = 0;

#define LOG_TAG "GL_WAVE_Controller_JNI"

WaveRender* waveRender;

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_gl_GLESController_prepareWave(JNIEnv *env, jobject instance,
                                                          jobject surface, jint width,
                                                          jint height) {
    if (surface != NULL) {
        window = ANativeWindow_fromSurface(env, surface);
        LOGI("Tian waveRender Got window %p", window);
        waveRender = new WaveRender();
        waveRender->prepareEGL(window, width, height);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_gl_GLESController_showWave(JNIEnv *env, jobject instance) {

}

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_gl_GLESController_destroyWave(JNIEnv *env, jobject instance) {

    if (waveRender) {
        waveRender->destroyEGL();
        waveRender = NULL;
    }
    if (window) {
        ANativeWindow_release(window);
        window = NULL;
    }

}