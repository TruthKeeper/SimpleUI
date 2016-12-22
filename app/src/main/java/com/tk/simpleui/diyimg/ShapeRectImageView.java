package com.tk.simpleui.diyimg;

import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;

/**
 * Created by TK on 2016/11/9.
 */

public class ShapeRectImageView extends ShapeImageview {
    public ShapeRectImageView(Context context) {
        super(context);
    }

    public ShapeRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void generatePath(Path path) {
        path.moveTo(getWidth() >> 1, 0);
        path.lineTo(getWidth(), getHeight() >> 1);
        path.lineTo(getWidth() >> 1, getHeight());
        path.lineTo(0, getHeight() >> 1);
        path.close();
    }
}
