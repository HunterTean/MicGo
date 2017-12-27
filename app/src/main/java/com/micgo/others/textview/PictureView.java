package com.micgo.others.textview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.TextureView;

import com.micgo.R;
import com.micgo.utils.KTVLog;

import java.io.IOException;

/**
 * Created by liuhongtian on 17/12/21.
 */

public class PictureView extends TextureView implements TextureView.SurfaceTextureListener {

    private Paint mPaint;//画笔
    private Rect mSrcRect;
    private Rect mDstRect;

    public PictureView(Context context) {
        this(context, null);
    }

    public PictureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PictureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOpaque(false);//设置背景透明，记住这里是[是否不透明]
        setSurfaceTextureListener(this);//设置监听

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);//创建画笔
        mSrcRect = new Rect();
        mDstRect = new Rect();
    }

    //从本地读取图片，这里的path必须是绝对地址
    private Bitmap readBitmap(int resId) throws IOException {
        return BitmapFactory.decodeResource(getResources(), resId);
    }
    //从Assets读取图片
    private Bitmap readBitmapStream(String path) throws IOException {
        return BitmapFactory.decodeStream(getResources().getAssets().open(path));
    }

    //将图片画到画布上，图片将被以宽度为比例画上去
    private void drawBitmap(Bitmap bitmap) {
        Canvas canvas = lockCanvas(new Rect(0, 0, getWidth(), getHeight()));//锁定画布
        KTVLog.d("Tian", "canvas : " + canvas.isHardwareAccelerated());
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);// 清空画布
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect dst = new Rect(0, 0, getWidth(), bitmap.getHeight() * getWidth() / bitmap.getWidth());
        canvas.drawBitmap(bitmap, src, dst, mPaint);//将bitmap画到画布上
        unlockCanvasAndPost(canvas);//解锁画布同时提交
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //当TextureView初始化时调用，事实上当你的程序退到后台它会被销毁，你再次打开程序的时候它会被重新初始化
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        //当TextureView的大小改变时调用
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        //当TextureView被销毁时调用
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //当TextureView更新时调用，也就是当我们调用unlockCanvasAndPost方法时
    }

    private static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    PlayThread mPlayThread;
    //开始播放
    public void start() {
        //开启线程
        mPlayThread = new PlayThread();
        mPlayThread.start();
    }

    private class PlayThread extends Thread {
        int mDelayTime = 10000;
        boolean isStop = false;
        int mPlayFrame = 0;
        @Override
        public void run() {
            try {
                while (!isStop) {//如果还没有播放完所有帧
                    Bitmap bitmap = readBitmap(R.drawable.record_song_bg_1);
                    drawBitmap(bitmap);
                    recycleBitmap(bitmap);
                    mPlayFrame++;
                    SystemClock.sleep(mDelayTime);//暂停间隔时间
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
