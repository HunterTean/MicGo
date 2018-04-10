//
// Created by 刘洪天 on 18/4/4.
//

#include <jni.h>
#include "libextract/extractor.h"

Extractor* extractor;

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_ffmpeg_ExtractProcessor_init(JNIEnv *env, jobject instance) {
    if (NULL == extractor) {
        extractor = new Extractor();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_ffmpeg_ExtractProcessor_extractMP3FromMP3(JNIEnv *env, jobject instance,
                                                                jstring inputPath_,
                                                                jstring outputPath_,
                                                                jint startSecond, jint endSecond) {
    if (extractor) {
        const char *inputPath = env->GetStringUTFChars(inputPath_, 0);
        const char *outputPath = env->GetStringUTFChars(outputPath_, 0);

        extractor->processMP3ToMP3(inputPath, outputPath, startSecond, endSecond);

        env->ReleaseStringUTFChars(inputPath_, inputPath);
        env->ReleaseStringUTFChars(outputPath_, outputPath);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_ffmpeg_ExtractProcessor_extractMP3FromMP4(JNIEnv *env, jobject instance,
                                                                jstring inputPath_,
                                                                jstring outputPath_,
                                                                jint startSecond, jint endSecond) {
    if (extractor) {
        const char *inputPath = env->GetStringUTFChars(inputPath_, 0);
        const char *outputPath = env->GetStringUTFChars(outputPath_, 0);

        extractor->processMP4ToMP3(inputPath, outputPath, startSecond, endSecond);

        env->ReleaseStringUTFChars(inputPath_, inputPath);
        env->ReleaseStringUTFChars(outputPath_, outputPath);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_ffmpeg_ExtractProcessor_destroy(JNIEnv *env, jobject instance) {
    if (NULL != extractor) {
        delete extractor;
        extractor = NULL;
    }
}