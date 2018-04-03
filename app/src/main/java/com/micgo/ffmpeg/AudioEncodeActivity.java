package com.micgo.ffmpeg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.micgo.R;
import com.micgo.studio.NativeGo;

/**
 * Created by liuhongtian on 17/5/19.
 */

public class AudioEncodeActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_encode);

        setTitle("Audio Encode");

        textView = (TextView) findViewById(R.id.tips_tx);
        textView.setText(NativeGo.getInstance().stringFromJNI());
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

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, AudioEncodeActivity.class);
        return intent;
    }

}
