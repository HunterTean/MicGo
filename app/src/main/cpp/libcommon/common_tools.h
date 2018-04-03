//
// Created by 刘洪天 on 17/2/27.
//

#ifndef MICGO_COMMON_TOOLS_H
#define MICGO_COMMON_TOOLS_H

#include <Android/log.h>

#define LOG_TAG  "native_go"

#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE,  LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG ,  LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO  ,  LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN  ,  LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  ,  LOG_TAG, __VA_ARGS__)

#endif //MICGO_COMMON_TOOLS_H
