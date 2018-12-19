package com.micgo.opengl.wave;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.TextView;

import com.micgo.MicGoApplication;
import com.micgo.R;
import com.micgo.studio.gl.GLESController;
import com.micgo.utils.KTVUtility;

/**
 * Created by liuhongtian on 2018/11/30.
 */

public class GLWaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl_wave);

        setTitle("Waves");

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(GLESController.getInstance().test());

        SurfaceView textureView = (SurfaceView) findViewById(R.id.texture_view);
        textureView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        textureView.setZOrderOnTop(true);

        textureView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                GLESController.getInstance().prepareWave(holder.getSurface(),
                        KTVUtility.getInstance().getWidth(), KTVUtility.dpToPixel(MicGoApplication.getInstance(), 100));
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                GLESController.getInstance().destroyWave();
            }
        });
//        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//            @Override
//            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
//                Surface surface = new Surface(surfaceTexture);
//                GLESController.getInstance().prepareWave(surface, width, height);
//            }
//
//            @Override
//            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
//
//            }
//
//            @Override
//            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//                GLESController.getInstance().destroyWave();
//                return false;
//            }
//
//            @Override
//            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//
//            }
//        });
    }



    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, GLWaveActivity.class);
        return intent;
    }

}