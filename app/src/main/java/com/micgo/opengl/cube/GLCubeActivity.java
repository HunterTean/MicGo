package com.micgo.opengl.cube;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.widget.TextView;

import com.micgo.R;
import com.micgo.studio.gl.GLESController;

/**
 * Created by liuhongtian on 18/1/10.
 */

public class GLCubeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl_cube);

        setTitle("Cube");

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(GLESController.getInstance().test());

        TextureView textureView = (TextureView) findViewById(R.id.texture_view);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                Surface surface = new Surface(surfaceTexture);

                GLESController.getInstance().prepareEGLContext(surface, width, height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
    }

    public static Intent buildIntent(Context context) {
        Intent intent = new Intent(context, GLCubeActivity.class);
        return intent;
    }

}
