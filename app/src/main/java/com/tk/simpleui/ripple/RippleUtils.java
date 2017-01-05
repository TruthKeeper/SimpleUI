package com.tk.simpleui.ripple;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by TK on 2017/1/5.
 */

public class RippleUtils {
    public static final String REVEAL = "reveal";

    public static void revealStart(Activity activity, Class<?> cls, View view) {
        int[] location = new int[2];
        Intent intent = new Intent(activity, cls);
        view.getLocationInWindow(location);
        intent.putExtra(REVEAL, new int[]{location[0],
                location[1],
                location[0] + view.getMeasuredWidth(),
                location[1] + view.getMeasuredHeight()});
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public static void revealShow(Activity activity) {
        int[] locations = activity.getIntent().getIntArrayExtra(REVEAL);

    }
}
