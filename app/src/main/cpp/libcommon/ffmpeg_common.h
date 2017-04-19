//
// Created by 刘洪天 on 17/2/27.
//

#ifndef MICGO_FFMPEG_COMMON_H
#define MICGO_FFMPEG_COMMON_H

extern "C" {
#include "../thirdparty/ffmpeg/include/libavcodec/avcodec.h"
#include "../thirdparty/ffmpeg/include/libavformat/avformat.h"
#include "../thirdparty/ffmpeg/include/libavutil/avutil.h"
#include "../thirdparty/ffmpeg/include/libavutil/samplefmt.h"
#include "../thirdparty/ffmpeg/include/libavutil/common.h"
#include "../thirdparty/ffmpeg/include/libavutil/channel_layout.h"
#include "../thirdparty/ffmpeg/include/libavutil/opt.h"
//#include "../thirdparty/ffmpeg/include/libavutil/timer.h"
#include "../thirdparty/ffmpeg/include/libavutil/imgutils.h"
#include "../thirdparty/ffmpeg/include/libavutil/mathematics.h"
#include "../thirdparty/ffmpeg/include/libswscale/swscale.h"
#include "../thirdparty/ffmpeg/include/libswresample/swresample.h"

#include "../thirdparty/ffmpeg/include/libavutil/frame.h"
};

#endif //MICGO_FFMPEG_COMMON_H
