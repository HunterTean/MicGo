package com.micgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import com.micgo.ffmpeg.FFmpegActivity;
import com.micgo.mediacodec.MediaCodecActivity;
import com.micgo.opengl.OpenGLActivity;
import com.micgo.others.OthersActivity;
import com.micgo.exoplayer.ExoPlayerActivity;
import com.micgo.studio.NativeGo;
import com.micgo.utils.KTVLog;
import com.micgo.utils.KTVUtility;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NativeGo.getInstance();

        KTVUtility.getInstance().initScreenParams(getWindowManager().getDefaultDisplay());
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

            case R.id.opengl:
                Intent glIntent = OpenGLActivity.buildIntent(this);
                startActivity(glIntent);
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
