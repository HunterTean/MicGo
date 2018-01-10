package com.micgo.others.snow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.micgo.R;
import com.micgo.utils.KTVLog;
import com.micgo.utils.KTVUtility;

/**
 * Created by liuhongtian on 18/1/2.
 */

public class SnowView extends View {

    private static final int NUM_SNOWFLAKES = 5; // 雪花数量
    private static final int DELAY = 5; // 延迟
    private QuaverFlake[] mQuaverFlakes; // 雪花
    private Bitmap[] mBitmaps = new Bitmap[2];

    public SnowView(Context context) {
        super(context);
    }

    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            initSnow(w, h);
        }
    }

    private void initSnow(int width, int height) {
        KTVLog.d("Tian", "density = " + getContext().getResources().getDisplayMetrics().density);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // 抗锯齿
        paint.setColor(Color.WHITE); // 白色雪花
        paint.setStyle(Paint.Style.FILL); // 填充;
        mQuaverFlakes = new QuaverFlake[NUM_SNOWFLAKES];
        for (int i = 0; i < NUM_SNOWFLAKES; ++i) {
            mQuaverFlakes[i] = QuaverFlake.create(width, height, paint);
        }

        loadBitmaps();
    }

    private void loadBitmaps() {
//        mBitmaps[0] = decodeSampledBitmapFromStream(getResources(), R.drawable.record_download_quaver_1, KTVUtility.dpToPixel(getContext(), 20));
//        mBitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.record_download_quaver_0);
//        mBitmaps[1] = decodeSampledBitmapFromStream(getResources(), R.drawable.record_download_quaver_2, KTVUtility.dpToPixel(getContext(), 20));
//        mBitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.record_download_quaver_1);
    }

    public static synchronized Bitmap decodeSampledBitmapFromStream(Resources resources, int in, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, in, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, in, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        while (height / inSampleSize > reqHeight) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (QuaverFlake s : mQuaverFlakes) {
            s.draw(canvas, mBitmaps[s.getmBitmapID()]);
        }
        // 隔一段时间重绘一次, 动画效果
        getHandler().postDelayed(runnable, DELAY);
    }

    // 重绘线程
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };
}
