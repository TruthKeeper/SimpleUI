package com.tk.simpleui.load;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by TK on 2016/10/29.
 */

public class LoadView extends View {
    private static final int SMALL_R = 8;
    private static final int LARGE_R = 30;
    private static final int SIZE = 8;
    private static final int PADDING = 24;

    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private ValueAnimator valueAnimator;
    private int mInt;
    private RectF mRectF = new RectF();
    private float startAngle;
    private float sweepAngle;

    public LoadView(Context context) {
        super(context);
        init();
    }

    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(12);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.WHITE);
        valueAnimator = ValueAnimator.ofInt(0, 120);
        valueAnimator.setDuration(2_000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mInt = (int) valueAnimator.getAnimatedValue();
                if (mInt <= 90) {
                    startAngle = -90 + mInt;
                    sweepAngle = mInt * 2;
                } else {
                    startAngle = (mInt - 90) * 9;
                    sweepAngle = 180 - (mInt - 90) * 6;
                }

                invalidate();
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mRectF, startAngle, sweepAngle, false, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(PADDING, PADDING, w - PADDING * 2, h - PADDING * 2);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }


}
