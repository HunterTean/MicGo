package com.micgo.studio.ffmpeg;

/**
 * Created by liuhongtian on 18/4/10.
 */

public class ExtractProcessor {

    public native void init();

    public native void extractMP3FromMP3(String inputPath, String outputPath, int startSecond, int endSecond);

    public native void extractMP3FromMP4(String inputPath, String outputPath, int startSecond, int endSecond);

    public native void destroy();

}
