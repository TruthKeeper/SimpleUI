package com.tk.simpleui.diyimg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by TK on 2016/11/8.
 */

public class CircleImageView extends ImageView {
    private Path mPath = new Path();

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(mPath);
        super.onDraw(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath.reset();
        int rW = w - getPaddingLeft() - getPaddingRight();
        int rH = h - getPaddingTop() - getPaddingBottom();
        mPath.addCircle(w >> 1, h >> 1, Math.min(rH, rW) >> 1, Path.Direction.CW);
    }
}
