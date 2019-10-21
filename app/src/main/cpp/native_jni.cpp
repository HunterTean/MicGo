#include <jni.h>
#include <string>

#include "nativelib/thread_rename.h"

extern "C"
jstring Java_com_micgo_studio_NativeGo_stringFromJNI(JNIEnv* env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

struct ListNode{
    int val;
    ListNode* next;
};

void sor(ListNode* header) {
    if (header == NULL) {
        return;
    }
    ListNode* cursor = header;
    ListNode* aPointer = cursor;
    cursor = cursor->next;
    if (cursor == NULL) {
        return;
    }
    ListNode* bPointer = cursor;
    while (true) {
        cursor = cursor->next;
        if (cursor == NULL) {
            break;
        }
        aPointer->next = cursor;
        cursor = cursor->next;
        if (cursor == NULL) {
            break;
        }
        bPointer->next = cursor;
    }
    aPointer->next = bPointer;
}

extern "C"
void Java_com_micgo_studio_NativeGo_createThread(JNIEnv* env, jobject /* this */) {
    LOGI("Java_com_micgo_studio_NativeGo_createThread 1");
//    pthread_t* pthread;
    LOGI("Java_com_micgo_studio_NativeGo_createThread 2");
//    pthread_create(pthread, NULL, thread_rename::thread1, NULL);
    LOGI("Java_com_micgo_studio_NativeGo_createThread 3");
}
