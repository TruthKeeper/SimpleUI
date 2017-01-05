package com.tk.simpleui.ripple.reveal;

import android.animation.Animator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by TK on 2017/1/5.
 */

public class RevealHelper {
    private static final String TAG = "RevealHelper";
    public static final int DURING = 500;

    /**
     * 开启圆形揭示动画
     *
     * @param target
     * @param centerX
     * @param centerY
     * @param startRadius
     * @param endRadius
     */
    public static void startCircleReveal(final View target,
                                         final int centerX, final int centerY,
                                         final float startRadius, final float endRadius) {
        startCircleReveal(target, centerX, centerY, startRadius, endRadius, DURING);
    }

    /**
     * 开启圆形揭示动画
     *
     * @param target
     * @param centerX
     * @param centerY
     * @param startRadius
     * @param endRadius
     * @param during
     * @return
     */
    public static Animator startCircleReveal(final View target,
                                             final int centerX, final int centerY,
                                             final float startRadius, final float endRadius,
                                             final int during) {
        if (target == null) {
            Log.e(TAG, "startCircleReveal: target is null !");
            return null;
        }
        ViewParent parent = target.getParent();
        if (parent == null) {
            Log.e(TAG, "startCircleReveal: target's parent is null !");
            return null;
        }
        if (startRadius >= endRadius || startRadius < 0 || during < 0) {
            Log.e(TAG, "startCircleReveal: params is error!");
            return null;
        }
        CircleRevealLayout parentLayout;
        if (parent instanceof CircleRevealLayout) {
            //已经偷天换日
            parentLayout = (CircleRevealLayout) parent;
            return new CircleRevealLayout.Builder()
                    .setTarget(target)
                    .setCenterX(centerX)
                    .setCenterY(centerY)
                    .setStartRadius(startRadius)
                    .setEndRadius(endRadius)
                    .setDuring(during)
                    .build(parentLayout);
        } else {
            //偷天换日
            parentLayout = new CircleRevealLayout(target.getContext());

            ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
            int index = ((ViewGroup) parent).indexOfChild(target);
            ((ViewGroup) parent).removeView(target);
            parentLayout.addView(target, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ((ViewGroup) parent).addView(parentLayout, index, layoutParams);

            return new CircleRevealLayout.Builder()
                    .setTarget(target)
                    .setCenterX(centerX)
                    .setCenterY(centerY)
                    .setStartRadius(startRadius)
                    .setEndRadius(endRadius)
                    .setDuring(during)
                    .build(parentLayout);
        }


    }
}
