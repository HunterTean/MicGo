#include <jni.h>
#include <string>

#include "thread_rename.h"

extern "C"
jstring Java_com_micgo_studio_NativeLib_stringFromJNI(JNIEnv* env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
void Java_com_micgo_studio_NativeLib_createThread(JNIEnv* env, jobject /* this */) {
    LOGI("Tian Java_com_micgo_studio_NativeLib_createThread 1");
    pthread_t* pthread;
    LOGI("Tian Java_com_micgo_studio_NativeLib_createThread 2");
    pthread_create(pthread, NULL, thread_rename::thread1, NULL);
    LOGI("Tian Java_com_micgo_studio_NativeLib_createThread 3");
}
