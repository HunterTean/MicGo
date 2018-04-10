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

    videoSrcPath0 = path0;
    videoSrcPath1 = path1;
    videoSrcPath2 = path2;
    videoSrcPath3 = path3;

    videoDstPath = dstPath;

    av_register_all();

    char *path = path0;
    AVFormatContext *src_ctx_0 = NULL;
    AVFormatContext *src_ctx_1 = NULL;

    LOGI("Tian VideoConcator::init | path0 = %s | dstPath = %s.", path0, dstPath);

    ret = avformat_open_input(&src_ctx_0, path, 0, 0);

    if (ret != 0){//打开文件失败
        LOGI("Tian VideoConcator::init 打开文件失败");
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

    // 强制指定 264 编码
    oc->oformat->video_codec = AV_CODEC_ID_H264;
    LOGI("Tian we will avformat_write_header");
    /* Write the stream header, if any. */
    ret = avformat_write_header(oc, NULL);
    if (ret < 0) {
        LOGI("Tian Error occurred when opening output file: %s\n", av_err2str(ret));
        return 1;
    }
    LOGI("Tian avformat_write_header success");

//    int totalSec = ac->duration / AV_TIME_BASE;
    for (;;){
        AVPacket pkt;
        ret = av_read_frame(src_ctx_0, &pkt);
        if (ret != 0){
            LOGI("Tian av_read_frame");
            break;
        }
        ret = av_interleaved_write_frame(oc, &pkt);
        if (ret != 0) {
            LOGI("Tian av_interleaved_write_frame");
		    break;
	    }
        av_packet_unref(&pkt);
    }

    avformat_close_input(&src_ctx_0);

    av_write_trailer(oc);
    avformat_free_context(oc);
}

void VideoConcator::start() {

}

void VideoConcator::destroy() {

}