package com.micgo.ffmpeg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.micgo.R;

/**
 * Created by liuhongtian on 17/2/25.
 */

public class PrimaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_primary);

        setTitle("FFmpeg Primary");
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.audio_encode:

                break;
            case R.id.video_encode:

                break;
            default:

                break;
        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, PrimaryActivity.class);
        return intent;
    }

}
