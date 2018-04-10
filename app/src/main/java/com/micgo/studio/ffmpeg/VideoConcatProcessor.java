package com.micgo.studio.ffmpeg;

/**
 * Created by liuhongtian on 18/4/3.
 */

public class VideoConcatProcessor {

    public native void init(String videoPath0, String videoPath1, String videoPath2, String videoPath3, String dstVideoPath);

    public native void start();

    public native void destroy();

}
