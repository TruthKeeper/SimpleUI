package com.tk.simpleui.gesturelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by TK on 2017/1/16.
 * 单个点
 */

public class TouchView extends View {
    public static final int STATE_INIT = 0;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_FAILURE = 2;
    public static final int STATE_SUCCESS = 3;

    public static final float PERCENT_R_BIG = 0.6f;
    public static final float PERCENT_R_SMALL = 0.2f;

    public static final int STROKE_THIN = 2;
    public static final int STROKE_STRONG = 4;

    private Paint mPaint = new Paint();
    private Path mPath = new Path();

    private int mThemeColor;
    private int mFailureColor;
    private int mState = STATE_INIT;

    private int mBigRadius;
    private int mSmallRadius;
    //内部绘制三角箭头
    private boolean mDragGuide;
    private boolean mFailureGuide;
    //箭头角度
    private float mGuideDegree = 0;


    public TouchView(Context context) {
        super(context);
        init();
    }

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mState) {
            case STATE_INIT:
                mPaint.setColor(mThemeColor);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(STROKE_THIN);
                canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mBigRadius, mPaint);
                break;
            case STATE_DRAGGING:
                mPaint.setColor(mThemeColor);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(STROKE_STRONG);
                canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mBigRadius, mPaint);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mSmallRadius, mPaint);
                if (mDragGuide) {
                    canvas.rotate(mGuideDegree, getWidth() >> 1, getHeight() >> 1);
                    canvas.drawPath(mPath, mPaint);
                }
                break;
            case STATE_FAILURE:
                mPaint.setColor(mFailureColor);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(STROKE_STRONG);
                canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mBigRadius, mPaint);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, mSmallRadius, mPaint);
                if (mFailureGuide) {
                    canvas.rotate(mGuideDegree, getWidth() >> 1, getHeight() >> 1);
                    canvas.drawPath(mPath, mPaint);
                }
                break;
            case STATE_SUCCESS:
                break;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //外界约束宽高相等
        mBigRadius = (int) (w * PERCENT_R_BIG) >> 1;
        mSmallRadius = (int) (w * PERCENT_R_SMALL) >> 1;
        mPath.reset();
        //正三角形
        int height = mBigRadius - mSmallRadius >> 1;
        mPath.moveTo(w >> 1, (float) ((h >> 1) - mSmallRadius * 2.5));
        mPath.rLineTo((float) (height / Math.sqrt(3)), height);
        mPath.rLineTo(-(float) (2 * height / Math.sqrt(3)), 0);
        mPath.close();
    }

    public void initColors(int themeColor, int failureColor) {
        this.mThemeColor = themeColor;
        this.mFailureColor = failureColor;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public int getBigRadius() {
        return mBigRadius;
    }

    public void setDragGuide(boolean dragGuide) {
        this.mDragGuide = dragGuide;
    }

    public void setFailureGuide(boolean failureGuide) {
        this.mFailureGuide = failureGuide;
    }

    public void setGuideDegree(float guideDegree) {
        this.mGuideDegree = guideDegree;
    }
}
