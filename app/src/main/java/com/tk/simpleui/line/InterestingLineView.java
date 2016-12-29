package com.tk.simpleui.line;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TK on 2016/12/28.
 * 创意来自于https://github.com/zhangyuChen1991/MagicLine
 */

public class InterestingLineView extends View {

    private Paint mPaint = new Paint();
    private Path mPath = new Path();

    private ValueAnimator animator;

    private float mRadiusAX = 400;
    private float mRadiusAY = 400;
    private float mRadiusBX = 200;
    private float mRadiusBY = 200;

    private float mSpeedA = 0.05f;
    private float mSpeedB = 0.35f;

    private float mAngleA;
    private float mAngleB;
    private List<Pair<PointF, PointF>> mPointList = new ArrayList<>();

    private Bitmap mBitmap;
    private Canvas mCanvas;

    public InterestingLineView(Context context) {
        super(context);
        init();
    }

    public InterestingLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        setBackgroundColor(Color.BLACK);

        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(16_000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                calculPath();
                invalidate();
            }
        });
    }

    private void calculPath() {
        if (mCanvas == null) {
            return;
        }
        mAngleA += mSpeedA;
        mAngleB += mSpeedB;

        float newAX = (float) ((getWidth() >> 1) + mRadiusAX * Math.sin(mAngleA));
        float newAY = (float) ((getHeight() >> 1) - mRadiusAY * Math.cos(mAngleA));
        float newBX = (float) ((getWidth() >> 1) + mRadiusBX * Math.sin(mAngleB));
        float newBY = (float) ((getHeight() >> 1) - mRadiusBY * Math.cos(mAngleB));
        mPointList.add(new Pair<>(new PointF(newAX, newAY), new PointF(newBX, newBY)));
        mCanvas.drawLine(newAX, newAY, newBX, newBY, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.cancel();
        }
        if (mBitmap != null) {
            mBitmap.recycle();
        }
    }

    public void start(float radiusAX, float radiusAY, float speedA, float radiusBX, float radiusBY, float speedB) {
        if (getWidth() == 0) {
            return;
        }
        this.mRadiusAX = radiusAX;
        this.mRadiusAY = radiusAY;
        this.mRadiusBX = radiusBX;
        this.mRadiusBY = radiusBY;
        this.mSpeedA = speedA;
        this.mSpeedB = speedB;
        animator.cancel();
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            invalidate();
        }
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
