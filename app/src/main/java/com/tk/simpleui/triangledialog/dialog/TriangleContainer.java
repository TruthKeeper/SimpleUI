package com.tk.simpleui.triangledialog.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import static com.tk.simpleui.triangledialog.dialog.TriangleDialog.Mode.LEFT;
import static com.tk.simpleui.triangledialog.dialog.TriangleDialog.Mode.TOP;
import static com.tk.simpleui.triangledialog.dialog.TriangleDialog.Mode.RIGHT;
import static com.tk.simpleui.triangledialog.dialog.TriangleDialog.Mode.BOTTOM;


/**
 * <pre>
 *      author : TK
 *      time : 2017/8/15
 *      desc :
 * </pre>
 */

public class TriangleContainer extends FrameLayout {
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    @TriangleDialog.Mode
    private int mMode;
    private int round;
    private int triangleWidth;
    private int triangleHeight;
    /**
     * 三角偏移量，>0向右，向下
     */
    private int off;

    public TriangleContainer(@NonNull Context context) {
        this(context, null);
    }

    public TriangleContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleContainer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath.reset();
        RectF rectF = new RectF(0, 0, w, h);
        switch (mMode) {
            case LEFT:
                rectF.left = rectF.left + triangleHeight;
                mPath.moveTo(triangleHeight, round + off);
                mPath.rLineTo(0, triangleWidth);
                mPath.rLineTo(-triangleHeight, -triangleWidth >> 1);
                mPath.close();
                break;
            case TOP:
                rectF.top = rectF.top + triangleHeight;
                mPath.moveTo(round + off, triangleHeight);
                mPath.rLineTo(triangleWidth, 0);
                mPath.rLineTo(-triangleWidth >> 1, -triangleHeight);
                mPath.close();
                break;
            case RIGHT:
                rectF.right = rectF.right - triangleHeight;
                mPath.moveTo(w - triangleHeight, round + off);
                mPath.rLineTo(0, triangleWidth);
                mPath.rLineTo(triangleHeight, -triangleWidth >> 1);
                mPath.close();
                break;
            case BOTTOM:
                rectF.bottom = rectF.bottom - triangleHeight;
                mPath.moveTo(round + off, h - triangleHeight);
                mPath.rLineTo(triangleWidth, 0);
                mPath.rLineTo(-triangleWidth >> 1, triangleHeight);
                mPath.close();
                break;
        }
        mPath.addRoundRect(rectF, round, round, Path.Direction.CW);
    }

    public void init(@TriangleDialog.Mode int mode, int triangleWidth, int triangleHeight, int off, int round, int color) {
        this.triangleWidth = triangleWidth < 0 ? 0 : triangleWidth;
        this.triangleHeight = triangleHeight < 0 ? 0 : triangleHeight;
        this.round = round < 0 ? 0 : round;
        this.off = off < 0 ? 0 : off;

        mMode = mode;
        mPaint.setColor(color);
        switch (mMode) {
            case LEFT:
                setPadding(triangleHeight, 0, 0, 0);
                break;
            case TOP:
                setPadding(0, triangleHeight, 0, 0);
                break;
            case RIGHT:
                setPadding(0, 0, triangleHeight, 0);
                break;
            case BOTTOM:
                setPadding(0, 0, 0, triangleHeight);
                break;
        }
    }
}
