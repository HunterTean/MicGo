package com.micgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.micgo.demos.OthersActivity;
import com.micgo.ffmpeg.PrimaryActivity;
import com.micgo.studio.NativeLib;
import com.micgo.utils.KTVLog;
import com.micgo.utils.KTVUtility;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NativeLib.getInstance();

    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.ffmpeg_primary:
                Intent priaryIntent = PrimaryActivity.buildIntent(this);
                startActivity(priaryIntent);
                break;
            case R.id.others:
                Intent demosIntent = OthersActivity.buildIntent(this);
                startActivity(demosIntent);
                break;
        }
    }
}
