package com.tk.simpleui.diyimg;

import android.content.Context;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by TK on 2016/11/9.
 */

public class ShapeOtherImageView extends ShapeImageview {
    public ShapeOtherImageView(Context context) {
        super(context);
    }

    public ShapeOtherImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void generatePath(Path path) {
        RectF rectF = new RectF(getPaddingLeft(), getPaddingTop() + 30,
                getWidth() - getPaddingRight() - getPaddingLeft(), getBottom() - getPaddingTop() - getPaddingBottom() - 30);
        path.addOval(rectF, Path.Direction.CW);
    }
}
