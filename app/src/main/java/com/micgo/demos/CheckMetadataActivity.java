package com.micgo.demos;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.micgo.R;
import com.micgo.utils.KTVUtility;

import java.io.File;

/**
 * Created by liuhongtian on 17/4/19.
 */

public class CheckMetadataActivity extends AppCompatActivity {

    private TextView pathText;
    private TextView durationText;

    private String musicFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_metadata);

        setTitle("Check Metadata");

        pathText = (TextView) findViewById(R.id.path_tv);
        durationText = (TextView) findViewById(R.id.duration_tv);

        musicFilePath = KTVUtility.getMGFileDir() + File.separator + "song.aac";
        File file = new File(musicFilePath);
        pathText.setText(musicFilePath + " | exist = " + file.exists());
        if (file != null && file.exists()) {
            checkDuration(file);
        }
    }

    private void checkDuration(File file) {
        Uri uri = Uri.fromFile(file);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, uri);
//        MediaFormat mediaFormat = new MediaFormat();
//        mediaFormat.
        if (mediaPlayer != null) {
            durationText.setText(String.valueOf(mediaPlayer.getDuration()));
        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, CheckMetadataActivity.class);
        return intent;
    }

}
