package com.micgo.others.snow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by liuhongtian on 18/1/2.
 */

public class QuaverFlake {

    // 移动速度
    private static final float INCREMENT_LOWER = 8f;
    private static final float INCREMENT_UPPER = 16f;

    private final RandomGenerator mRandom; // 随机控制器
    private final Point mPosition; // 位置
    private int mAngle; // 角度
    private float mIncrement; // 速度
    private final float mFlakeSize; // 大小
    private final Paint mPaint; // 画笔
    private int mOffsetY;
    private int mBitmapID;

    private QuaverFlake(RandomGenerator random, Point position, int angle, float increment, float flakeSize, int offsetY, Paint paint) {
        mRandom = random;
        mPosition = position;
        mIncrement = increment;
        mFlakeSize = flakeSize;
        mPaint = paint;
        mAngle = angle;
        mOffsetY = offsetY;
        mBitmapID = mRandom.getRandom(2);
    }

    public static QuaverFlake create(int width, int height, Paint paint) {
        RandomGenerator random = new RandomGenerator();
        int x = width;
        int y = random.getRandom(height);
        Point position = new Point(x, y);
        float increment = random.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);
        float flakeSize = 14;
        int angleA = y / height / 2 * 360;
        int angleB = 360 - angleA;
        int angle = angleA > (angleB - 180) ? angleA : angleB;

        int offsetY = random.getRandom(height / 2);

        return new QuaverFlake(random, position, angle, increment, flakeSize, offsetY, paint);
    }

    private double getRadian(int angle) {
        return Math.PI / 180 * angle;
    }

    // 绘制
    public void draw(Canvas canvas, Bitmap bitmap) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        move(width, height);
        canvas.drawBitmap(bitmap, mPosition.x, mPosition.y, mPaint);
    }

    // 移动
    private void move(int width, int height) {
        float x = mPosition.x - mIncrement;
        double y = height / 2 * (Math.cos(getRadian(mAngle)) / 2 + 0.5) + mOffsetY;
        mAngle++;
        mPosition.set((int) x, (int) y);

        // 移除屏幕, 重新开始
        if (!isInside(width, height)) {
            reset(width, height);
        }
    }

    // 判断是否在其中
    private boolean isInside(int width, int height) {
        int x = mPosition.x;
        int y = mPosition.y;
        boolean a = x >= -mFlakeSize - 1;
        boolean b = x <= width;
        boolean c = y >= -mFlakeSize - 1;
        boolean d = y - mFlakeSize < height;
        return a && b && c && d;
    }

    // 重置
    private void reset(int width, int height) {
        mPosition.x = width;
        double y = mRandom.getRandom(height - mFlakeSize) + mFlakeSize;

        mIncrement = mRandom.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);

        mAngle = (int) (Math.random() * 360);
        float angleA = (float) (y / height / 2f * 360);
        float angleB = 360 - angleA;
        mAngle = (int) (angleA > (angleB - 180) ? angleA : angleB);

        mOffsetY = (int) mRandom.getRandom((height - mFlakeSize) / 2);

        mPosition.y = (int) (height / 2 * (Math.cos(getRadian(mAngle)) / 2 + 0.5)) + mOffsetY;


    }

    public int getmBitmapID() {
        return mBitmapID;
    }

}
