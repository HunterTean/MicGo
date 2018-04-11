package com.micgo.ffmpeg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.micgo.R;
import com.micgo.studio.ffmpeg.ExtractProcessor;

/**
 * Created by liuhongtian on 18/4/10.
 */

public class ExtractActivity extends AppCompatActivity {

    private TextView textView;

    private final String audioInputPath = "/sdcard/.mg/tongyang.mp3";
    private final String videoInputPath = "/sdcard/.mg/yanyuan.mp4";
    private final String outputPath = "/sdcard/.mg/extract/dst.mp3";

    private ExtractProcessor processor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

        setTitle("Extract");

        textView = (TextView) findViewById(R.id.tips_tx);

        processor = new ExtractProcessor();
        processor.init();
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.a_btn:
                processMP3ToMP3();
                textView.setText("a done.");
                break;

            case R.id.b_btn:
                processMP4ToMP3();
                textView.setText("b done");
                break;

            default:

                break;
        }
    }

    private void processMP3ToMP3() {
        processor.extractMP3FromMP3(audioInputPath, outputPath, 30, 60);
    }

    private void processMP4ToMP3() {
        processor.extractMP3FromMP4(audioInputPath, outputPath, 30, 60);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        processor.destroy();
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, ExtractActivity.class);
        return intent;
    }

}