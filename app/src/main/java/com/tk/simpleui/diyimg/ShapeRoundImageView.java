package com.tk.simpleui.diyimg;

import android.content.Context;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.tk.simpleui.common.DensityUtil;

/**
 * Created by TK on 2016/11/9.
 */

public class ShapeRoundImageView extends ShapeImageview {
    public ShapeRoundImageView(Context context) {
        super(context);
    }

    public ShapeRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void generatePath(Path path) {
        RectF r = new RectF(getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());
        int round = DensityUtil.dp2px(getContext(), 6);
        path.addRoundRect(r, new float[]{round, round, round, round, round, round, round, round}, Path.Direction.CW);
    }
}
