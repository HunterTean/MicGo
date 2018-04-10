//
// Created by 刘洪天 on 18/4/4.
// 视频拼接功能类，方式：分别读取两个文件再写入同一个文件
//

#include "video_concator.h"

VideoConcator::VideoConcator() {

}

VideoConcator::~VideoConcator() {

}

int VideoConcator::init(char *path0, char *path1, char *path2, char *path3, char *dstPath) {
    int ret;

//    char *videoSrcPath0 = "/sdcard/.mg/concat/video0.mp4";
//    char *videoSrcPath1 = "/sdcard/.mg/concat/video1.mp4";
//    char *videoSrcPath2 = "/sdcard/.mg/concat/video2.mp4";
//    char *videoSrcPath3 = "/sdcard/.mg/concat/video3.mp4";
//    char *videoSrcPath4 = "/sdcard/.mg/concat/video4.mp4";

    char *videoSrcPath0 = path0;
    char *videoSrcPath1 = path1;
    char *videoSrcPath2 = path2;
    char *videoSrcPath3 = path3;
    char *videoSrcPath4 = path0;

//    char *videoDstPath = "/sdcard/.mg/concat/dstVideo.mp4";
    char *videoDstPath = dstPath;

    av_register_all();

    AVFormatContext *src_ctx_0 = NULL;
    AVFormatContext *src_ctx_1 = NULL;
    AVFormatContext *src_ctx_2 = NULL;
    AVFormatContext *src_ctx_3 = NULL;
    AVFormatContext *src_ctx_4 = NULL;

    LOGI("VideoConcat VideoConcator::init | path0 = %s | dstPath = %s.", videoSrcPath0, videoDstPath);

    ret = avformat_open_input(&src_ctx_0, videoSrcPath0, 0, 0);
    if (ret != 0){
        LOGI("VideoConcat VideoConcator::init 打开文件失败 0");
        return ret;
    }
    if ((ret = avformat_find_stream_info(src_ctx_0, NULL)) < 0) {
        LOGI("VideoConcat Cannot find stream information\n");
        return ret;
    }

    ret = avformat_open_input(&src_ctx_1, videoSrcPath1, 0, 0);
    if (ret != 0){
        LOGI("VideoConcat VideoConcator::init 打开文件失败 1");
        return ret;
    }
    if ((ret = avformat_find_stream_info(src_ctx_1, NULL)) < 0) {
        LOGI("VideoConcat Cannot find stream information\n");
        return ret;
    }

    ret = avformat_open_input(&src_ctx_2, videoSrcPath2, 0, 0);
    if (ret != 0){
        LOGI("VideoConcat VideoConcator::init 打开文件失败 0");
        return ret;
    }
    if ((ret = avformat_find_stream_info(src_ctx_2, NULL)) < 0) {
        LOGI("VideoConcat Cannot find stream information\n");
        return ret;
    }

    ret = avformat_open_input(&src_ctx_3, videoSrcPath3, 0, 0);
    if (ret != 0){
        LOGI("VideoConcat VideoConcator::init 打开文件失败 1");
        return ret;
    }
    if ((ret = avformat_find_stream_info(src_ctx_3, NULL)) < 0) {
        LOGI("VideoConcat Cannot find stream information\n");
        return ret;
    }

    ret = avformat_open_input(&src_ctx_4, videoSrcPath4, 0, 0);
    if (ret != 0){
        LOGI("VideoConcat VideoConcator::init 打开文件失败 0");
        return ret;
    }
    if ((ret = avformat_find_stream_info(src_ctx_4, NULL)) < 0) {
        LOGI("VideoConcat Cannot find stream information\n");
        return ret;
    }

    AVFormatContext *oc;
    /* allocate the output media context */
    ret = avformat_alloc_output_context2(&oc, NULL, NULL, videoDstPath);
    if (!oc) {
        LOGI("Could not deduce output format from file extension: using MPEG.\n");
        ret = avformat_alloc_output_context2(&oc, NULL, "mpeg", videoDstPath);
    }
    if (!oc) {
        return ret;
    }

    for (int i = 0; i < src_ctx_0->nb_streams; i++) {
        AVStream* inAVStream = src_ctx_0->streams[i];
        LOGI("VideoConcat inAVStream->codec = %s.", inAVStream->codec->codec_name);
        AVStream* outAVStream = avformat_new_stream(oc, inAVStream->codec->codec);
        if (avcodec_copy_context(outAVStream->codec, inAVStream->codec) < 0) {
            LOGI("VideoConcat Failed to copy codec context");
            return -1;
        }

        outAVStream->codec->codec_tag = 0;
        if (oc->oformat->flags & AVFMT_GLOBALHEADER) {
            outAVStream->codec->flags |= CODEC_FLAG_GLOBAL_HEADER; //AV_CODEC_FLAG_GLOBAL_HEADER
        }
    }

    avio_open(&oc->pb, videoDstPath, AVIO_FLAG_READ_WRITE);
    if (oc->pb == NULL) {
        LOGI("VideoConcat Could not open for writing");
        return -1;
    }

    LOGI("VideoConcat will avformat_write_header");
    /* Write the stream header, if any. */
    ret = avformat_write_header(oc, NULL);
    if (ret < 0) {
        LOGI("VideoConcat Error occurred when opening output file: %s\n", av_err2str(ret));
        return 1;
    }
    LOGI("VideoConcat avformat_write_header success");

    AVPacket pkt;
    av_init_packet(&pkt);

    AVStream * in_stream, * out_stream;

    int64_t timeLen = 0;
    int64_t pts_us, dts_us;

    for(int i = 0; i < 5; i++){
        AVFormatContext *src_ctx;
        if (i == 0) {
            src_ctx = src_ctx_0;
        } else if (i == 1) {
            src_ctx = src_ctx_1;
        } else if (i == 2) {
            src_ctx = src_ctx_2;
        } else if (i == 3) {
            src_ctx = src_ctx_3;
        } else if (i == 4) {
            src_ctx = src_ctx_4;
        }
        while(1){
            ret = av_read_frame(src_ctx, &pkt);
            if(ret < 0){
                break;
            }

            in_stream = src_ctx->streams[pkt.stream_index];
            out_stream = oc->streams[pkt.stream_index];
            pts_us = av_rescale_q(pkt.pts, in_stream->time_base, AV_TIME_BASE_Q);
            dts_us = av_rescale_q(pkt.dts, in_stream->time_base, AV_TIME_BASE_Q);
            pts_us += timeLen;
            dts_us += timeLen;
            pkt.pts = av_rescale_q(pts_us, AV_TIME_BASE_Q, out_stream->time_base);
            pkt.dts = av_rescale_q(dts_us, AV_TIME_BASE_Q, out_stream->time_base);
            pkt.duration = av_rescale_q(pkt.duration, in_stream->time_base, out_stream->time_base);
            pkt.pos = -1;

            ret = av_interleaved_write_frame(oc, &pkt);
            if(ret < 0){
                LOGI("VideoConcat av_interleaved_write_frame end %d | ret = %d.", i, ret);
                return -22;
            }
            av_packet_unref(&pkt);
        }
        av_packet_unref(&pkt);
        timeLen += src_ctx->duration;
    }


    av_write_trailer(oc);

    avformat_close_input(&src_ctx_0);
    avformat_close_input(&src_ctx_1);
    avformat_close_input(&src_ctx_2);
    avformat_close_input(&src_ctx_3);
    avformat_close_input(&src_ctx_4);

    avformat_free_context(oc);
    LOGI("VideoConcat done.");
    return ret;
}

void VideoConcator::start() {

}

void VideoConcator::destroy() {

}