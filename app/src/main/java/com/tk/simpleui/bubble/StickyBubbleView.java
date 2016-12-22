package com.tk.simpleui.bubble;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by TK on 2016/10/29.
 */

public class StickyBubbleView extends View {
    private static final int SMALL_R = 8;
    private static final int LARGE_R = 30;
    private static final int SIZE = 8;
    private static final int PADDING = 45;

    private Paint mPaint = new Paint();

    private Path mPath = new Path();

    private ValueAnimator valueAnimator;
    private int mInt;

    public StickyBubbleView(Context context) {
        super(context);
        init();
    }

    public StickyBubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        valueAnimator = ValueAnimator.ofInt(0, SIZE * 4);
        valueAnimator.setDuration(8_000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mInt = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
//        valueAnimator.start();
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath.reset();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }


}
