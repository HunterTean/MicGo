//
// Created by 刘洪天 on 18/1/10.
//

#include <jni.h>
#include <string>

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