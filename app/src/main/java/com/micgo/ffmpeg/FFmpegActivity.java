package com.micgo.ffmpeg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.micgo.R;
import com.micgo.studio.NativeGo;

/**
 * Created by liuhongtian on 17/7/25.
 */

public class FFmpegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg);

        setTitle("FFmpeg");
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.audio_encode:
                Intent audioEncodeIntent = AudioEncodeActivity.buildIntent(this);
                startActivity(audioEncodeIntent);
                break;

            case R.id.video_encode:

                break;

            case R.id.video_concat:
                Intent videoConcatIntent = VideoConcatActivity.buildIntent(this);
                startActivity(videoConcatIntent);
                break;

            case R.id.lab:
                NativeGo.getInstance().createThread();
                break;

            default:

                break;
        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, FFmpegActivity.class);
        return intent;
    }

}