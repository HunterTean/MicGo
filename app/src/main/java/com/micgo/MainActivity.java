package com.micgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import com.micgo.ffmpeg.FFmpegActivity;
import com.micgo.mediacodec.MediaCodecActivity;
import com.micgo.others.OthersActivity;
import com.micgo.exoplayer.ExoPlayerActivity;
import com.micgo.studio.NativeLib;
import com.micgo.utils.KTVLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NativeLib.getInstance();

        initScreenParams(getWindowManager().getDefaultDisplay());
    }

    public void initScreenParams(Display display) {
        DisplayMetrics metric = new DisplayMetrics();
        display.getMetrics(metric);

        KTVLog.d("MainActivity", "densityDpi = " + metric.densityDpi + ", density = " + metric.density + ", scaleDensity : " + metric.scaledDensity);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素） metric.heightPixels
        KTVLog.d("MainActivity", "width = " + width + " | height = " + height);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.ffmpeg:
                Intent priaryIntent = FFmpegActivity.buildIntent(this);
                startActivity(priaryIntent);
                break;

            case R.id.media_codec:
                Intent mediaCodecIntent = MediaCodecActivity.buildIntent(this);
                startActivity(mediaCodecIntent);
                break;

            case R.id.opensl:
                break;

            case R.id.exoplayer:
                Intent exoIntent = ExoPlayerActivity.buildIntent(this);
                startActivity(exoIntent);
                break;

            case R.id.others:
                Intent demosIntent = OthersActivity.buildIntent(this);
                startActivity(demosIntent);
                break;
        }
    }
}
