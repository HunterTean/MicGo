//
// Created by 刘洪天 on 18/4/4.
//

#include <jni.h>
#include "libvideo_concat/video_concator.h"

#define LOG_TAG "VideoConcatProcessor_jni"

VideoConcator* videoConcator;

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_ffmpeg_VideoConcatProcessor_init(JNIEnv *env, jobject instance,
                                                       jstring videoPath0_, jstring videoPath1_,
                                                       jstring videoPath2_, jstring videoPath3_,
                                                       jstring desVideoPath_) {
    char *videoPath0 = (char *) env->GetStringUTFChars(videoPath0_, 0);
    char *videoPath1 = (char *) env->GetStringUTFChars(videoPath1_, 0);
    char *videoPath2 = (char *) env->GetStringUTFChars(videoPath2_, 0);
    char *videoPath3 = (char *) env->GetStringUTFChars(videoPath3_, 0);
    char *dstVideoPath = (char *) env->GetStringUTFChars(desVideoPath_, 0);

    videoConcator = new VideoConcator();
    videoConcator->init(videoPath0, videoPath1, videoPath2, videoPath3, dstVideoPath);

    env->ReleaseStringUTFChars(videoPath0_, videoPath0);
    env->ReleaseStringUTFChars(videoPath1_, videoPath1);
    env->ReleaseStringUTFChars(videoPath2_, videoPath2);
    env->ReleaseStringUTFChars(videoPath3_, videoPath3);
    env->ReleaseStringUTFChars(desVideoPath_, dstVideoPath);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_ffmpeg_VideoConcatProcessor_start(JNIEnv *env, jobject instance) {

    // TODO

}

extern "C"
JNIEXPORT void JNICALL
Java_com_micgo_studio_ffmpeg_VideoConcatProcessor_destroy(JNIEnv *env, jobject instance) {

    // TODO

}