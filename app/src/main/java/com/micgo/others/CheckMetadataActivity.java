package com.micgo.others;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
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
            checkDataSource(file);
        }
    }

    private void checkDataSource(File file) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(file.getAbsolutePath());

        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE); // api level 10, 即从GB2.3.3开始有此功能
        String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
        String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        String bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE); // 从api level 14才有，即从ICS4.0才有此功能
        String date = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);

        durationText.setText(title + '\n' + album + '\n' + mime + '\n' + artist  + '\n' + duration + '\n' + bitrate + '\n' + date);
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, CheckMetadataActivity.class);
        return intent;
    }

}
