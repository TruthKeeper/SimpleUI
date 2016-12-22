package com.tk.simpleui.diyimg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tk.simpleui.common.DensityUtil;

/**
 * Created by TK on 2016/11/8.
 */

public class RoundRectImageView extends ImageView {
    private Path mPath = new Path();

    public RoundRectImageView(Context context) {
        super(context);
    }

    public RoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundRectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        RectF r = new RectF(getPaddingLeft(),
                getPaddingTop(),
                w - getPaddingLeft() - getPaddingRight(),
                h - getPaddingTop() - getPaddingBottom());
        int round = DensityUtil.dp2px(getContext(), 6);
        mPath.addRoundRect(r, new float[]{round, round, round, round, round, round, round, round}, Path.Direction.CW);
    }
}
