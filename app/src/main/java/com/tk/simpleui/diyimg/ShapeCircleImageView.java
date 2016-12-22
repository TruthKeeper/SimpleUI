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
        int r = Math.min(getWidth() - getPaddingLeft() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom()) >> 1;
        path.addCircle((getWidth() - getPaddingLeft() - getPaddingRight()) >> 1,
                (getHeight() - getPaddingTop() - getPaddingBottom()) >> 1, r, Path.Direction.CW);
    }
}
