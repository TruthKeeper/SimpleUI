package com.tk.simpleui.triangledialog.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * <pre>
 *      author : TK
 *      time : 2018/3/30
 *      desc :
 * </pre>
 */

public class TriangleContainer extends FrameLayout {
    private Paint mPaint = new Paint();
    private Path mPath = new Path();

    private int radius;
    @TriangleDialog.Direction
    private int direction;

    private int triangleWidth;
    private int triangleHeight;
    private float containerOffsetPercent;

    private int shadowRadius;

    public TriangleContainer(@NonNull Context context) {
        this(context, null);
    }

    public TriangleContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

    }

    public void prepare(int paddingLeft,
                        int paddingTop,
                        int paddingRight,
                        int paddingBottom,
                        int radius,
                        @TriangleDialog.Direction int direction,
                        int triangleWidth, int triangleHeight,
                        @FloatRange(from = 0, to = 1) float containerOffsetPercent, int containerColor,
                        int shadowRadius, int shadowColor) {

        this.radius = radius;
        this.direction = direction;
        this.triangleWidth = triangleWidth;
        this.triangleHeight = triangleHeight;
        this.containerOffsetPercent = containerOffsetPercent;
        this.shadowRadius = shadowRadius;

        mPaint.setColor(containerColor);
        if (shadowRadius > 0) {
            mPaint.setShadowLayer(shadowRadius, 0, 0, shadowColor);
        }
        switch (direction) {
            case TriangleDialog.Direction.BOTTOM:
                setPadding(shadowRadius + paddingLeft,
                        shadowRadius + paddingTop,
                        shadowRadius + paddingRight,
                        shadowRadius + paddingBottom + triangleHeight);
                break;
            case TriangleDialog.Direction.LEFT:
                setPadding(shadowRadius + paddingLeft + triangleHeight,
                        shadowRadius + paddingTop,
                        shadowRadius + paddingRight,
                        shadowRadius + paddingBottom);
                break;
            case TriangleDialog.Direction.RIGHT:
                setPadding(shadowRadius + paddingLeft,
                        shadowRadius + paddingTop,
                        shadowRadius + paddingRight + triangleHeight,
                        shadowRadius + paddingBottom);
                break;
            case TriangleDialog.Direction.TOP:
                setPadding(shadowRadius + paddingLeft,
                        shadowRadius + paddingTop + triangleHeight,
                        shadowRadius + paddingRight,
                        shadowRadius + paddingBottom);
                break;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
        canvas.clipPath(mPath);
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int containerOffset;
        mPath.reset();
        //将主体区域和三角形映射到Path上
        RectF rectF = new RectF(shadowRadius, shadowRadius, w - shadowRadius, h - shadowRadius);
        switch (direction) {
            case TriangleDialog.Direction.BOTTOM:
                containerOffset = (int) (Math.max(0, w - shadowRadius * 2 - radius * 2 - triangleWidth) * containerOffsetPercent);
                rectF.bottom -= triangleHeight;
                mPath.moveTo(radius + shadowRadius + containerOffset, rectF.bottom);
                mPath.rLineTo(triangleWidth, 0);
                mPath.rLineTo(-triangleWidth >> 1, triangleHeight);
                mPath.close();
                break;
            case TriangleDialog.Direction.LEFT:
                containerOffset = (int) (Math.max(0, h - shadowRadius * 2 - radius * 2 - triangleWidth) * containerOffsetPercent);
                rectF.left += triangleHeight;
                mPath.moveTo(rectF.left, radius + shadowRadius + containerOffset);
                mPath.rLineTo(0, triangleWidth);
                mPath.rLineTo(-triangleHeight, -triangleWidth >> 1);
                mPath.close();
                break;
            case TriangleDialog.Direction.RIGHT:
                containerOffset = (int) (Math.max(0, h - shadowRadius * 2 - radius * 2 - triangleWidth) * containerOffsetPercent);
                rectF.right -= triangleHeight;
                mPath.moveTo(rectF.right, radius + shadowRadius + containerOffset);
                mPath.rLineTo(0, triangleWidth);
                mPath.rLineTo(triangleHeight, -triangleWidth >> 1);
                mPath.close();
                break;
            case TriangleDialog.Direction.TOP:
                containerOffset = (int) (Math.max(0, w - shadowRadius * 2 - radius * 2 - triangleWidth) * containerOffsetPercent);
                rectF.top += triangleHeight;
                mPath.moveTo(radius + shadowRadius + containerOffset, rectF.top);
                mPath.rLineTo(triangleWidth, 0);
                mPath.rLineTo(-triangleWidth >> 1, -triangleHeight);
                mPath.close();
                break;
        }
        mPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);
    }
}
