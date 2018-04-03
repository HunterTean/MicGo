//
// Created by 刘洪天 on 17/5/19.
//

#include "thread_rename.h"

void * thread_rename::thread1(void* arg) {
    LOGI("thread_rename 1");
    int i = 0;
    while (1) {
        LOGI("thread_rename 2");
        usleep(100000);
        LOGI("thread_rename i'am alive.");
        if (i == 50) {
            break;
        }
        i++;
    }
    LOGI("thread_rename 3");
}