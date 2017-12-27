package com.micgo.others;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.micgo.R;
import com.micgo.others.textview.GLPictureView;

/**
 * Created by liuhongtian on 17/12/21.
 */

public class TextureViewActivity extends AppCompatActivity {

    private static final String TAG = "TextureViewActivity";

    private FrameLayout mParent;
    private GLPictureView mTexture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture);

        mParent = (FrameLayout) findViewById(R.id.activity_main);
//        mTexture = new PictureView(this);
        mTexture = new GLPictureView(this);
        mParent.addView(mTexture, 0);
//        setContentView(mTexture);
//        mTexture.start();

        setTitle("TextureView");
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, TextureViewActivity.class);
        return intent;
    }

}
