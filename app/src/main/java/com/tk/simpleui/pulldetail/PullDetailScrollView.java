package com.tk.simpleui.pulldetail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by TK on 2016/11/8.
 */

public class PullDetailScrollView extends ScrollView {

    public PullDetailScrollView(Context context) {
        super(context);
    }

    public PullDetailScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullDetailScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        getParent().requestDisallowInterceptTouchEvent(true);
//        return super.dispatchTouchEvent(ev);
//    }
}