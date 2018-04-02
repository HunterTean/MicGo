package com.micgo.opengl.trans.view;//package com.changba.record.recording.view.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.TextureView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by liuhongtian on 17/12/22.
 */

public class TransBgView extends TextureView implements TextureView.SurfaceTextureListener {

    private GLRenderThread mProducerThread = null;
    private GLRendererImpl mRenderer;

    public TransBgView(Context context) {
        this(context, null);
    }

    public TransBgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TransBgView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    public void init() {
        setSurfaceTextureListener(this);

        mRenderer = new GLRendererImpl(getContext());
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mRenderer.setViewport(width, height);
        mProducerThread = new GLRenderThread(surface, mRenderer, new AtomicBoolean(true));
        mProducerThread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        mRenderer.resize(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mProducerThread.setShouldRender(false);
        mProducerThread = null;
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void setShouldPause(boolean shouldPause) {
        if (mProducerThread != null) {
            mProducerThread.setShouldPause(shouldPause);
        }
    }

}
