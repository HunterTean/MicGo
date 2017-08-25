package com.micgo.mediacodec;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.micgo.R;
import com.micgo.utils.KTVUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by liuhongtian on 17/7/25.
 */

public class AudioTrackPcmActivity extends AppCompatActivity {

    private AudioTrack audioTrack;
    private int audioBufSize;
    private byte[] audioData;
    private Player player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_track_pcm);

        setTitle("Audio Track Play PCM");

        initAudioTrack();
        player = new Player();
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.a_btn:
                audioTrack.play();
                player.start();
                break;
            case R.id.b_btn:

                break;
            default:

                break;
        }
    }

    private void initAudioTrack() {
        audioBufSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, audioBufSize, AudioTrack.MODE_STREAM);
    }

    class Player extends Thread {
        byte[] data1 = new byte[audioBufSize*2];
        File file = KTVUtility.getMGFile("audio.pcm");
        FileInputStream fileInputStream;

        @Override
        public void run() {
            super.run();
            if (!file.exists()) {
                return;
            }
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    fileInputStream.read(data1, 0, audioBufSize*2);
                } catch (Exception e) {
                    break;
                }
                audioTrack.write(data1, 0, audioBufSize*2);
            }
            try {
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, AudioTrackPcmActivity.class);
        return intent;
    }

}