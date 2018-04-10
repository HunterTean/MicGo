package com.micgo.ffmpeg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.micgo.R;
import com.micgo.studio.ffmpeg.VideoConcatProcessor;

/**
 * Created by liuhongtian on 18/4/3.
 */

public class VideoConcatActivity extends AppCompatActivity {

    private TextView textView;

    private final String video0 = "/sdcard/.mg/concat/video0.mp4";
    private final String video1 = "/sdcard/.mg/concat/video1.mp4";
    private final String video2 = "/sdcard/.mg/concat/video2.mp4";
    private final String video3 = "/sdcard/.mg/concat/video3.mp4";
    private final String videoDst = "/sdcard/.mg/concat/dstConcat.mp4";

    private VideoConcatProcessor processor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_concat);

        setTitle("Video Concat");

        textView = (TextView) findViewById(R.id.tips_tx);

        processor = new VideoConcatProcessor();
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.a_btn:
                processor.init(video0, video1, video2, video3, videoDst);
                processor.start();
                break;

            case R.id.b_btn:
                processor.destroy();
                break;

            default:

                break;
        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, VideoConcatActivity.class);
        return intent;
    }

}