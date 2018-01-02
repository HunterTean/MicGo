package com.micgo.others;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.micgo.R;

/**
 * Created by liuhongtian on 18/1/2.
 */

public class SnowActivity extends AppCompatActivity {

    private static final String TAG = "SnowActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snow);

        setTitle("Snow");
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, SnowActivity.class);
        return intent;
    }

}
