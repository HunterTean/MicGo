#include <jni.h>
#include <string>

#include "nativelib/thread_rename.h"

extern "C"
jstring Java_com_micgo_studio_NativeGo_stringFromJNI(JNIEnv* env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
void Java_com_micgo_studio_NativeGo_createThread(JNIEnv* env, jobject /* this */) {
    LOGI("Java_com_micgo_studio_NativeGo_createThread 1");
//    pthread_t* pthread;
    LOGI("Java_com_micgo_studio_NativeGo_createThread 2");
//    pthread_create(pthread, NULL, thread_rename::thread1, NULL);
    LOGI("Java_com_micgo_studio_NativeGo_createThread 3");
}
