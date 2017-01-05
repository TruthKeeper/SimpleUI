package com.tk.simpleui.ripple.reveal;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by TK on 2017/1/5.
 * 5.0揭示动画兼容辅助类
 */

public class RevealHelper {
    private static final String TAG = "RevealHelper";
    private static final String REVEAL = "reveal";
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

    /**
     * 揭示动画之跳转Activity
     *
     * @param anchor
     * @param activity
     * @param cls
     */
    public static void preCircleReveal(final View anchor, Activity activity, Class<?> cls) {
        if (anchor == null) {
            Log.e(TAG, "startCircleReveal: anchor is null !");
            return;
        }
        int[] location = new int[2];
        Intent intent = new Intent(activity, cls);
        anchor.getLocationInWindow(location);
        intent.putExtra(REVEAL, new int[]{location[0],
                location[1],
                location[0] + anchor.getMeasuredWidth(),
                location[1] + anchor.getMeasuredHeight()});
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);

    }

    /**
     * 揭示动画之Activity显示
     * 注意：设置windowBackground背景会变黑色，需对style适配
     *
     * @param activity
     */
    public static void startCircleReveal(final Activity activity) {
        startCircleReveal(activity, DURING);
    }

    /**
     * 揭示动画之Activity显示
     * 注意：设置windowBackground背景会变黑色，需对style适配
     *
     * @param activity
     * @param during
     */
    public static void startCircleReveal(final Activity activity, final int during) {
        int[] locations = activity.getIntent().getIntArrayExtra(REVEAL);
        if (locations == null) {
            Log.e(TAG, "startCircleReveal: extra is null !");
            return;
        }
        //锚点中心坐标
        final int centerX = locations[2] + locations[0] >> 1;
        final int centerY = (locations[3] + locations[1] >> 1) - getStatusBarHeight(activity);
        final View view = activity.findViewById(android.R.id.content);
        view.post(new Runnable() {
            @Override
            public void run() {
                float radius = (float) Math.max(
                        Math.max(Math.hypot(centerX, centerY),
                                Math.hypot(view.getMeasuredWidth() - centerX, centerY)),
                        Math.max(Math.hypot(centerX, view.getMeasuredHeight() - centerY),
                                Math.hypot(view.getMeasuredWidth() - centerX, view.getMeasuredHeight() - centerY)));

                startCircleReveal(view, centerX, centerY, 0, radius, during);
            }
        });

    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
