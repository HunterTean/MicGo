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
    av_register_all();

    const char *srcPath = inputPath;

    AVFormatContext *in_ctx = NULL;
    AVFormatContext *out_ctx = NULL;

    int ret = avformat_open_input(&in_ctx, srcPath, 0, 0);

    if (ret != 0){
        return ret;
    }

    if ((ret = avformat_find_stream_info(in_ctx, NULL)) < 0) {
        return ret;
    }

    ret = avformat_alloc_output_context2(&out_ctx, NULL, NULL, outputPath);
    if (!out_ctx) {
        ret = avformat_alloc_output_context2(&out_ctx, NULL, "mpeg", outputPath);
        if (!out_ctx) {
            return ret;
        }
    }

    int audio_index = 0;
    for (int i = 0; i < in_ctx->nb_streams; i++) {
        AVStream* inAVStream = in_ctx->streams[i];
        if (inAVStream->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audio_index = i;
        }
        LOGI("Tian codec_name = %d.", inAVStream->codecpar->codec_type);
        AVCodec* avCodec = avcodec_find_decoder(inAVStream->codecpar->codec_id);
        AVStream* outAVStream = avformat_new_stream(out_ctx, avCodec);
        avcodec_parameters_copy(outAVStream->codecpar, inAVStream->codecpar);
        if(ret < 0){
            return -41;
        }
        outAVStream->codecpar->codec_tag = 0;
    }

    avio_open(&out_ctx->pb, outputPath, AVIO_FLAG_READ_WRITE);
    if (out_ctx->pb == NULL) {
        return -1;
    }

    ret = avformat_write_header(out_ctx, NULL);
    if (ret < 0) {
        return 1;
    }

    AVPacket pkt;
    av_init_packet(&pkt);

    int64_t startTimeStap = av_rescale_q(startSecond * 1000000, AV_TIME_BASE_Q, in_ctx->streams[audio_index]->time_base);
    int64_t endTimeUs = endSecond * 1000000;
    av_seek_frame(in_ctx, audio_index, startTimeStap, AVSEEK_FLAG_BACKWARD);

    while(1){
        ret = av_read_frame(in_ctx, &pkt);
        if(ret < 0){
            break;
        }

        int64_t curTimeUs = av_rescale_q(pkt.pts, in_ctx->streams[audio_index]->time_base, AV_TIME_BASE_Q);
//        LOGI("pkt.pts = %" PRId64 " | endframe = %" PRId64 " | time = %" PRId64 "", pkt.pts, endTimeUs, curTimeUs);

        ret = av_interleaved_write_frame(out_ctx, &pkt);
        if(ret < 0){
            return -7;
        }

        av_packet_unref(&pkt);

        if (curTimeUs >= endTimeUs) {
            break;
        }
    }

    av_write_trailer(out_ctx);

    avformat_close_input(&in_ctx);

    avformat_free_context(in_ctx);
    avformat_free_context(out_ctx);

    return ret;
}

int Extractor::processMP4ToMP3(const char *inputPath, const char *outputPath, int startSecond, int endSecond) {
    LOGI("Extractor::processMP4ToMP3 | inputPath = %s | outputPath = %s.", inputPath, outputPath);
    av_register_all();

    const char *srcPath = inputPath;

    AVFormatContext *in_ctx = NULL;
    AVFormatContext *out_ctx = NULL;

    int ret = avformat_open_input(&in_ctx, srcPath, 0, 0);

    if (ret != 0){
        return ret;
    }

    if ((ret = avformat_find_stream_info(in_ctx, NULL)) < 0) {
        LOGI("Extractor::processMP4ToMP3 Cannot find stream information\n");
        return ret;
    }

//    ret = avformat_alloc_output_context2(&out_ctx, NULL, NULL, outputPath);
//    if (!out_ctx) {
//        LOGI("Could not deduce output format from file extension: using MPEG.\n");
//        ret = avformat_alloc_output_context2(&out_ctx, NULL, "mpeg", outputPath);
//        if (!out_ctx) {
//            return ret;
//        }
//    }

    int audio_index = 0;
//    for (int i = 0; i < in_ctx->nb_streams; i++) {
//        AVStream* inAVStream = in_ctx->streams[i];
//        if (inAVStream->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
//            LOGI("Extractor audio_index = %d.", audio_index);
//            audio_index = i;
//            AVCodec* avCodec = avcodec_find_decoder(inAVStream->codecpar->codec_id);
//            AVStream* outAVStream = avformat_new_stream(out_ctx, avCodec);
//            avcodec_parameters_copy(outAVStream->codecpar, inAVStream->codecpar);
//            if(ret < 0){
//                printf("Extractor copy context failed\n");
//                return -41;
//            }
//            outAVStream->codecpar->codec_tag = 0;
//            break;
//        }
//    }
//
//    avio_open(&out_ctx->pb, outputPath, AVIO_FLAG_READ_WRITE);
//    if (out_ctx->pb == NULL) {
//        LOGI("Extractor Could not open for writing");
//        return -1;
//    }
//
//    LOGI("Extractor will avformat_write_header");
//    ret = avformat_write_header(out_ctx, NULL);
//    if (ret < 0) {
//        LOGI("Extractor Error occurred when opening output file: %s\n", av_err2str(ret));
//        return 1;
//    }

    openOutputMP3File(outputPath, in_ctx, out_ctx);

    AVPacket pkt;
    av_init_packet(&pkt);

    int64_t startTimeStap = av_rescale_q(startSecond * 1000000, AV_TIME_BASE_Q, in_ctx->streams[audio_index]->time_base);
    int64_t endTimeUs = endSecond * 1000000;
    LOGI("av_rescale_q | startframe = %" PRId64 " | endframe = %" PRId64 ".", startTimeStap, endTimeUs);
    av_seek_frame(in_ctx, audio_index, startTimeStap, AVSEEK_FLAG_BACKWARD);
    while(1){
        AVStream *in_stream, *out_stream;

        ret = av_read_frame(in_ctx, &pkt);
        if(ret < 0){
            break;
        }

        if (pkt.stream_index != audio_index) {
            continue;
        }

        in_stream = in_ctx->streams[pkt.stream_index];
        out_stream = out_ctx->streams[0];

        //Convert PTS/DTS
        pkt.pts = av_rescale_q_rnd(pkt.pts, in_stream->time_base, out_stream->time_base, (AVRounding) (AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX));
        pkt.dts = av_rescale_q_rnd(pkt.dts, in_stream->time_base, out_stream->time_base, (AVRounding) (AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX));
        pkt.duration = av_rescale_q(pkt.duration, in_stream->time_base, out_stream->time_base);
        pkt.pos = -1;
        pkt.stream_index = 0;

        int64_t curTimeUs = av_rescale_q(pkt.pts, in_ctx->streams[audio_index]->time_base, AV_TIME_BASE_Q);
        LOGI("pkt.pts = %" PRId64 " | endframe = %" PRId64 " | time = %" PRId64 "", pkt.pts, endTimeUs, curTimeUs);

        ret = av_interleaved_write_frame(out_ctx, &pkt);
        if(ret < 0){
            LOGI("Extractor write data faild\n");
            return -7;
        }

        av_packet_unref(&pkt);

        if (curTimeUs >= endTimeUs) {
            LOGI("Extractor write data end & break\n");
            break;
        }
    }
    LOGI("av_write_trailer");
    av_write_trailer(out_ctx);

    avformat_close_input(&in_ctx);

    avformat_free_context(in_ctx);
    avformat_free_context(out_ctx);

    return ret;
}

int Extractor::openOutputMP3File(const char *filename, AVFormatContext *iFmtCtx,
                                 AVFormatContext *oFmtCtx) {
    AVStream *out_stream;
    AVStream *in_stream;
    AVCodecContext *dec_ctx, *enc_ctx;
    AVCodec *encoder;
    int ret;
    unsigned int i;
    oFmtCtx = NULL;
    avformat_alloc_output_context2(&oFmtCtx, NULL, NULL, filename);
    if (!oFmtCtx) {
        LOGI("Could notcreate output context\n");
        return AVERROR_UNKNOWN;
    }
    for (i = 0; i < iFmtCtx->nb_streams; i++) {

        in_stream = iFmtCtx->streams[i];
        dec_ctx = in_stream->codec;

        if (dec_ctx->codec_type == AVMEDIA_TYPE_AUDIO) {

            out_stream = avformat_new_stream(oFmtCtx, NULL);
            if (!out_stream) {
                av_log(NULL, AV_LOG_ERROR, "Failedallocating output stream\n");
                return AVERROR_UNKNOWN;
            }
            enc_ctx = out_stream->codec;

            /* in this example, we choose transcoding to same codec */
            LOGI("avcodec_find_encoder dec_ctx->codec_id = %d | enc_ctx->codec_id = %d | AV_CODEC_ID_MP3 = %d.", dec_ctx->codec_id, enc_ctx->codec_id, AV_CODEC_ID_MP3);
//            AVCodec* encoder0 = avcodec_find_encoder(dec_ctx->codec_id);
//            LOGI("Tian avcodec_open2 0 | sample_fmts = %d.", *encoder0->sample_fmts);
            encoder = avcodec_find_encoder(AV_CODEC_ID_MP3);
//            LOGI("Tian avcodec_open2 1");
//            /* In this example, we transcode to same properties(picture size,
//            * sample rate etc.). These properties can be changed for output
//            * streams easily using filters */
//            enc_ctx->sample_rate = dec_ctx->sample_rate;
//            enc_ctx->channel_layout = dec_ctx->channel_layout;
//            LOGI("Tian avcodec_open2 2");
//            enc_ctx->channels = av_get_channel_layout_nb_channels(enc_ctx->channel_layout);
//            LOGI("Tian avcodec_open2 3");
//            /* take first format from list of supported formats */
//            enc_ctx->sample_fmt = encoder0->sample_fmts[0];
//            LOGI("Tian avcodec_open2 4");
//            AVRational time_base = {1, enc_ctx->sample_rate};
//            LOGI("Tian avcodec_open2 5");
//            enc_ctx->time_base = time_base;
//            LOGI("Tian avcodec_open2 6");
//            enc_ctx->codec_type = AVMEDIA_TYPE_AUDIO;
//            LOGI("Tian avcodec_open2 7");
//            enc_ctx->bit_rate = 128000;
//            enc_ctx->sample_fmt = AV_SAMPLE_FMT_S16;
//            LOGI("Tian avcodec_open2 8");
//            if (oFmtCtx->oformat->flags &AVFMT_GLOBALHEADER) {
//                enc_ctx->flags |= CODEC_FLAG_GLOBAL_HEADER;
//            }
//            LOGI("Tian avcodec_open2 9");
//            enc_ctx->codec_tag = 0;

            /* Third parameter can be used to pass settings to encoder*/
            ret = avcodec_open2(enc_ctx, encoder, NULL);
            if (ret < 0) {
                LOGI("Cannot openvideo encoder for stream %d\n", ret);
                return ret;
            }
        }

    }
    av_dump_format(oFmtCtx, 0, filename, 1);

    if (!(oFmtCtx->oformat->flags &AVFMT_NOFILE)) {
        ret = avio_open(&oFmtCtx->pb, filename, AVIO_FLAG_WRITE);
        if (ret < 0) {
            LOGI("Could notopen output file '%s'", filename);
            return ret;
        }
    }
    /* init muxer, write output file header */
    ret = avformat_write_header(oFmtCtx, NULL);
    if (ret < 0) {
        LOGI("Error occurred when openingoutput file\n");
        return ret;
    }
    LOGI("openOutputMP3File done");
    return 0;
}