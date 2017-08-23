package com.micgo.mediacodec;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.micgo.R;
import com.micgo.utils.KTVLog;
import com.micgo.utils.KTVUtility;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by liuhongtian on 17/7/26.
 */

public class SimpleAudioActivity extends AppCompatActivity {

    private static final String TAG = "SimpleAudioActivity";
    private static final int TIMEOUT_US = 1000;

    private final String audioPath = KTVUtility.getMGFile("audio.mp3").toString();

    private TextView textView;

    private MediaExtractor mediaExtractor;
    private MediaFormat mediaFormat;
    private MediaCodec audioDecoder;
    private AudioTrack audioTrack;

    private boolean isPlaying;
    private boolean isStop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_audio);

        setTitle("Simple Audio");

        textView = (TextView) findViewById(R.id.tips_tx);
        textView.setText("playing");

        createMediaCodec(audioPath);

        if (mediaFormat != null) {
            int sampleRate = mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            int channel = mediaFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            int bitRate = mediaFormat.getInteger(MediaFormat.KEY_BIT_RATE);
            KTVLog.d(TAG, "onCreate sampleRate = " + sampleRate + " | channel = " + channel + " | bitRate = " + bitRate + " | KEY_BITRATE_MODE = " + mediaFormat.getInteger("KEY_BITRATE_MODE"));
            KTVLog.d(TAG, "onCreate sampleRate = " + 44100 + " | channel = " + AudioFormat.CHANNEL_OUT_STEREO + " | bitRate = " + AudioFormat.ENCODING_PCM_16BIT);
        }

    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.a_btn:

                break;
            case R.id.b_btn:

                break;
            default:

                break;
        }
    }

    private void createMediaCodec(String path) {
        try {
            mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(path);
            mediaFormat = mediaExtractor.getTrackFormat(0);
            if (mediaFormat == null)
            {
                textView.setText("format is null.");
                return;
            }
            String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {

            } else {
                textView.setText("mime is not audio.");
                return;
            }

            audioDecoder = MediaCodec.createDecoderByType(mime); // TODO aac decoder
            if (audioDecoder == null) {
                textView.setText("can not find decoder");
            }
            audioDecoder.configure(mediaFormat, null, null, 0); // TODO params null null 0 ??????
            audioDecoder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process() {

        initAudioTrack(44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);

        ByteBuffer[] inputBuffers = audioDecoder.getInputBuffers();
        ByteBuffer[] outputBuffers = audioDecoder.getOutputBuffers();

        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

        while (!isStop) {
            int inIndex = audioDecoder.dequeueInputBuffer(TIMEOUT_US);
            if (inIndex >= 0) {
                ByteBuffer buffer = inputBuffers[inIndex];
                //从MediaExtractor中读取一帧待解数据
                int sampleSize = mediaExtractor.readSampleData(buffer, 0);
                if (sampleSize < 0) {
                    audioDecoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                } else {
                    //向MediaDecoder输入一帧待解码数据
                    audioDecoder.queueInputBuffer(inIndex, 0, sampleSize, mediaExtractor.getSampleTime(), 0);
                    mediaExtractor.advance();
                }
                //从MediaDecoder队列取出一帧解码后的数据
                int outIndex = audioDecoder.dequeueOutputBuffer(info, TIMEOUT_US);
                switch (outIndex) {
                    case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                        outputBuffers = audioDecoder.getOutputBuffers();
                        break;

                    case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                        MediaFormat format = audioDecoder.getOutputFormat();
                        audioTrack.setPlaybackRate(format.getInteger(MediaFormat.KEY_SAMPLE_RATE));

                        break;
                    case MediaCodec.INFO_TRY_AGAIN_LATER:
                        break;

                    default:
                        ByteBuffer outBuffer = outputBuffers[outIndex];
                        final byte[] chunk = new byte[info.size];
                        // Read the buffer all at once
                        outBuffer.get(chunk);
                        //清空buffer,否则下一次得到的还会得到同样的buffer
                        outBuffer.clear();
                        // AudioTrack write data
                        audioTrack.write(chunk, info.offset, info.offset + info.size);
                        audioDecoder.releaseOutputBuffer(outIndex, false);
                        break;
                }

                // 所有帧都解码、播放完之后退出循环
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break;
                }
            }
        }

        // release audioDecoder
        audioDecoder.stop();
        audioDecoder.release();
        audioDecoder = null;

        // release mediaExtractor
        mediaExtractor.release();
        mediaExtractor = null;

        // release audioTrack
        audioTrack.stop();
        audioTrack.release();
        audioTrack = null;
    }

    private void initAudioTrack(int sampleRateInHz, int channelConfig, int audioFormat) {
        // 获取frame大小
        int bufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        // 创建audioTrack
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, channelConfig, audioFormat, bufferSize, AudioTrack.MODE_STREAM);
        audioTrack.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStop = true;
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, SimpleAudioActivity.class);
        return intent;
    }

}
