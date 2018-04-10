//
// Created by 刘洪天 on 18/4/4.
//

#ifndef MICGO_VIDEO_CONCAT_H
#define MICGO_VIDEO_CONCAT_H

#include <common_tools.h>
extern "C"
{
#include <libavformat/avformat.h>
}


class VideoConcator {

public:
    VideoConcator();
    virtual ~VideoConcator();

    int init(char* path0, char* path1, char* patch2, char* path3, char* dstPath);
    void start();
    void destroy();

private:
    char* videoSrcPath0;
    char* videoSrcPath1;
    char* videoSrcPath2;
    char* videoSrcPath3;

    char* videoDstPath;

    void processor();

};


#endif //MICGO_VIDEO_CONCAT_H
