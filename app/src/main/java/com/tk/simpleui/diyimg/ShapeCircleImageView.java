package com.tk.simpleui.diyimg;

import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;

/**
 * Created by TK on 2016/11/9.
 */

public class ShapeCircleImageView extends ShapeImageview {
    public ShapeCircleImageView(Context context) {
        super(context);
    }

    public ShapeCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void generatePath(Path path) {
        int realW = getWidth() - getPaddingLeft() - getPaddingRight();
        int realH = getHeight() - getPaddingTop() - getPaddingBottom();
        int r = Math.min(realW, realH) >> 1;
        if (realW >= realH) {
            path.addCircle(getPaddingLeft() + r + (realW - realH >> 1),
                    getPaddingTop() + r,
                    r,
                    Path.Direction.CW);
        } else {
            path.addCircle(getPaddingLeft() + r,
                    getPaddingTop() + r + (realH - realW >> 1),
                    r,
                    Path.Direction.CW);
        }

    }
}
