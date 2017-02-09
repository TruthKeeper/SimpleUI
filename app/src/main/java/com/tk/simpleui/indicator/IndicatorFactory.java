package com.tk.simpleui.indicator;

import android.content.Context;

/**
 * Created by TK on 2017/2/6.
 */

public class IndicatorFactory {

    public static IndicatorView.Builder with(Context context) {
        return new IndicatorView.Builder(context);
    }
}
