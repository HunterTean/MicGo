//
// Created by 刘洪天 on 18/4/10.
//

#include "extractor.h"

#define LOG_TAG "Tian"

Extractor::Extractor() {

}

Extractor::~Extractor() {

}

int Extractor::processMP3ToMP3(const char *inputPath, const char *outputPath, int startSecond, int endSecond) {
    LOGI("Extractor::processMP3ToMP3 | inputPath = %s | outputPath = %s.", inputPath, outputPath);
    av_register_all();

    const char *srcPath = inputPath;

    AVFormatContext *in_ctx = NULL;
    AVFormatContext *out_ctx = NULL;

    int ret = avformat_open_input(&in_ctx, srcPath, 0, 0);

    if (ret != 0){
        return ret;
    }

    if ((ret = avformat_find_stream_info(in_ctx, NULL)) < 0) {
        LOGI("Extractor::processMP3ToMP3 Cannot find stream information\n");
        return ret;
    }

    ret = avformat_alloc_output_context2(&out_ctx, NULL, NULL, outputPath);
    if (!out_ctx) {
        LOGI("Could not deduce output format from file extension: using MPEG.\n");
        ret = avformat_alloc_output_context2(&out_ctx, NULL, "mpeg", outputPath);
        if (!out_ctx) {
            return ret;
        }
    }

    int audio_index = 0;
    for (int i = 0; i < in_ctx->nb_streams; i++) {
        AVStream* inAVStream = in_ctx->streams[i];
        if (inAVStream->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            LOGI("Extractor inAVStream->codec AVMEDIA_TYPE_AUDIO = %d.", audio_index);
            audio_index = i;
        }
        AVCodec* avCodec = avcodec_find_decoder(inAVStream->codecpar->codec_id);
        AVStream* outAVStream = avformat_new_stream(out_ctx, avCodec);
        avcodec_parameters_copy(outAVStream->codecpar, inAVStream->codecpar);
        if(ret < 0){
            printf("Extractor copy context failed\n");
            return -41;
        }
        outAVStream->codecpar->codec_tag = 0;
    }

    avio_open(&out_ctx->pb, outputPath, AVIO_FLAG_READ_WRITE);
    if (out_ctx->pb == NULL) {
        LOGI("Extractor Could not open for writing");
        return -1;
    }

    LOGI("Extractor will avformat_write_header");
    ret = avformat_write_header(out_ctx, NULL);
    if (ret < 0) {
        LOGI("Extractor Error occurred when opening output file: %s\n", av_err2str(ret));
        return 1;
    }

    AVPacket pkt;
    av_init_packet(&pkt);

    int64_t startTimeStap = av_rescale_q(startSecond * 1000000, AV_TIME_BASE_Q, in_ctx->streams[audio_index]->time_base);
    int64_t endTimeUs = endSecond * 1000000;
    LOGI("av_rescale_q | startframe = %" PRId64 " | endframe = %" PRId64 ".", startTimeStap, endTimeUs);
    av_seek_frame(in_ctx, audio_index, startTimeStap, AVSEEK_FLAG_BACKWARD);

    int64_t currentFrame = startTimeStap;
    while(1){
        ret = av_read_frame(in_ctx, &pkt);
        if(ret < 0){
            break;
        }

        int64_t curTimeUs = av_rescale_q(pkt.pts, in_ctx->streams[audio_index]->time_base, AV_TIME_BASE_Q);
//        LOGI("pkt.pts = %" PRId64 " | endframe = %" PRId64 " | time = %" PRId64 "", pkt.pts, endTimeUs, curTimeUs);

        ret = av_interleaved_write_frame(out_ctx, &pkt);
        if(ret < 0){
            printf("Extractor write data faild\n");
            return -7;
        }

        av_packet_unref(&pkt);

        if (curTimeUs >= endTimeUs) {
            printf("Extractor write data end & break\n");
            break;
        } else {
            currentFrame++;
        }
    }

    av_write_trailer(out_ctx);

    avformat_close_input(&in_ctx);

    avformat_free_context(in_ctx);
    avformat_free_context(out_ctx);

    return ret;
}

int Extractor::processMP4ToMP3(const char *inputPath, const char *outputPath, int startSecond, int endSecond) {
    LOGI("Extractor::processMP4ToMP3");
    av_register_all();

}
