package com.micgo.exoplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.micgo.R;
import com.micgo.demos.CheckMetadataActivity;
import com.micgo.demos.NIOActivity;
import com.micgo.demos.OthersActivity;

/**
 * Created by liuhongtian on 17/5/17.
 */

public class ExoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);

        setTitle("ExoPlayer");
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.sniff_files:
                Intent checkIntent = SniffActivity.buildIntent(this);
                startActivity(checkIntent);
                break;
            case R.id.play_list:
                Intent listIntent = PlayListAvtivity.buildIntent(this);
                startActivity(listIntent);
                break;
            default:

                break;
        }
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, ExoPlayerActivity.class);
        return intent;
    }

}
