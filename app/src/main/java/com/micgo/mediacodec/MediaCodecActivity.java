package com.micgo.mediacodec;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.micgo.R;
import com.micgo.ffmpeg.primary.FFmpegPrimaryActivity;

/**
 * Created by liuhongtian on 17/7/25.
 */

public class MediaCodecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediacodec);

        setTitle("MediaCodec");
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.simple_audio:
                Intent simpleAudioIntent = SimpleAudioActivity.buildIntent(this);
                startActivity(simpleAudioIntent);
                break;
            case R.id.simple_video:

                break;
            default:

                break;
        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, MediaCodecActivity.class);
        return intent;
    }

}