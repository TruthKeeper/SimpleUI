package com.tk.simpleui.statusbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by TK on 2016/12/30.
 * 沉浸式的辅助
 */

public class StatusBarHelper {

    /**
     * 设置状态栏颜色，默认纯色
     *
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(Activity activity, @ColorInt int color) {
        setStatusBarColor(activity, color, 255);
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param color
     * @param statusBarAlpha
     */

    public static void setStatusBarColor(Activity activity, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0+
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateNewColor(color, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4.+
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            fitFakeView(activity, color, statusBarAlpha);
            setRootView(activity);
        }
    }


    /**
     * 使状态栏半透明，默认完全透明
     *
     * @param activity
     */
    public static void setTranslucent(Activity activity, @ColorInt int color) {
        setTranslucent(activity, color, 0);
    }

    /**
     * 使状态栏半透明
     *
     * @param activity
     * @param statusBarAlpha
     */
    public static void setTranslucent(Activity activity, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0+
            activity.getWindow().setStatusBarColor(calculateNewColor(color, statusBarAlpha));
            activity.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4+
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            fitFakeView(activity, color, statusBarAlpha);
        }
    }

    /**
     * 使状态栏半透明，标题View向下偏移
     * 与setStatusBarColorInFragment配合食用
     *
     * @param activity
     * @param statusBarAlpha
     */
    public static void setTranslucentInFragment(final Activity activity, final View needOffsetView, final @ColorInt int color, final int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        //Post来无缝切换
        activity.findViewById(android.R.id.content).post(new Runnable() {
            @Override
            public void run() {
                setTranslucent(activity, color, statusBarAlpha);
                if (needOffsetView != null) {
                    ((ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams()).topMargin = getStatusBarHeight(activity);
                }
            }
        });

    }

    /**
     * Fragment中设置状态栏颜色，默认纯色
     * 与setTranslucentInFragment配合食用
     *
     * @param activity
     * @param fragment
     * @param color
     */
    public static void setStatusBarColorInFragment(Activity activity, Fragment fragment, @ColorInt int color) {
        setStatusBarColorInFragment(activity, fragment, color, 255);
    }

    /**
     * Fragment中设置状态栏颜色
     * 与setTranslucentInFragment配合食用
     *
     * @param activity
     * @param fragment
     * @param color
     * @param statusBarAlpha
     */
    public static void setStatusBarColorInFragment(final Activity activity, final Fragment fragment, final @ColorInt int color, final int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setStatusBarColor(activity, color, statusBarAlpha);
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        contentView.setPadding(0, 0, 0, 0);
        //设置头部偏移
        if (fragment.getView() != null) {
            fragment.getView().setPadding(0, getStatusBarHeight(activity), 0, 0);
        } else {
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    fragment.getView().setPadding(0, getStatusBarHeight(activity), 0, 0);
                }
            });
        }
    }


    /**
     * 自适应FakeView
     *
     * @param activity
     * @param color
     * @param statusBarAlpha
     */
    private static void fitFakeView(Activity activity, @ColorInt int color, int statusBarAlpha) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        if (decorView != null) {
            int count = decorView.getChildCount();
            for (int i = 0; i < count; i++) {
                if (decorView.getChildAt(i) instanceof FakeView) {
                    //refresh
                    decorView.getChildAt(i).setBackgroundColor(calculateNewColor(color, statusBarAlpha));
                    return;
                }
            }
            //add
            FakeView fakeView = generateFakeView(activity, color, statusBarAlpha);
            decorView.addView(fakeView);
        }
    }


    /**
     * 设置根布局相关参数
     *
     * @param activity
     */
    private static void setRootView(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    /**
     * 生成一个和状态栏大小相同的矩形
     *
     * @param activity
     * @param color
     * @param alpha
     * @return
     */
    private static FakeView generateFakeView(Activity activity, @ColorInt int color, int alpha) {
        FakeView fakeView = new FakeView(activity);
        fakeView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity)));
        fakeView.setBackgroundColor(calculateNewColor(color, alpha));
        return fakeView;
    }


    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 计算状态栏颜色
     *
     * @param color
     * @param alpha
     * @return
     */
    public static int calculateNewColor(@ColorInt int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
}