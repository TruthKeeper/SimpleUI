package com.tk.simpleui.statusbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by TK on 2016/12/30.
 * 沉浸式的辅助
 */

public class StatusBarHelper {
    public static final int TAG_OFFSET_VIEW = -233;

    /**
     * 设置状态栏颜色，默认纯色
     *
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(@NonNull Activity activity, @ColorInt int color) {
        setStatusBarColor(activity, color, 255);
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param color
     * @param statusBarAlpha
     */
    public static void setStatusBarColor(@NonNull Activity activity,
                                         @ColorInt int color,
                                         @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0+
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateNewColor(color, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4.+
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            fitFakeView(activity, color, statusBarAlpha);
            setRootViewFit(activity, true);
        }
    }

    /**
     * <pre>
     *     适用于根布局为DrawerLayout
     *     设置状态栏颜色，默认纯色
     * <pre/>
     * @param activity
     * @param drawerLayout
     * @param color
     */
    public static void setStatusBarColorInDrawer(@NonNull Activity activity,
                                                 @NonNull DrawerLayout drawerLayout,
                                                 @ColorInt int color) {
        setStatusBarColorInDrawer(activity, drawerLayout, color, 255);
    }

    /**
     * <pre>
     *     适用于根布局为DrawerLayout
     *     设置状态栏颜色，默认纯色
     * <pre/>
     * @param activity
     * @param drawerLayout
     * @param color
     * @param statusBarAlpha
     */
    public static void setStatusBarColorInDrawer(@NonNull Activity activity,
                                                 @NonNull DrawerLayout drawerLayout,
                                                 @ColorInt int color,
                                                 @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0+
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateNewColor(color, statusBarAlpha));
            setDrawerFit(drawerLayout);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4.+
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            fitFakeView(activity, color, statusBarAlpha);
            ((ViewGroup.MarginLayoutParams) drawerLayout.getLayoutParams()).topMargin = getStatusBarHeight(activity);
            setDrawerFit(drawerLayout);
        }
    }


    /**
     * 使状态栏半透明，默认完全透明
     *
     * @param activity
     * @param color
     */
    public static void setTranslucent(@NonNull Activity activity, @ColorInt int color) {
        setTranslucent(activity, color, 0);
    }

    /**
     * 使状态栏半透明
     *
     * @param activity
     * @param color
     * @param statusBarAlpha
     */
    public static void setTranslucent(@NonNull Activity activity,
                                      @ColorInt int color,
                                      @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0+
            activity.getWindow().setStatusBarColor(calculateNewColor(color, statusBarAlpha));
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            setRootViewFit(activity, false);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4+
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            fitFakeView(activity, color, statusBarAlpha);
            setRootViewFit(activity, false);
        }
    }

    /**
     * <pre>
     *     适用于根布局为DrawerLayout
     *     使状态栏半透明，默认完全透明
     * <pre/>
     * @param activity
     * @param drawerLayout
     * @param color
     */
    public static void setTranslucentInDrawer(@NonNull Activity activity,
                                              @NonNull DrawerLayout drawerLayout,
                                              @ColorInt int color) {
        setTranslucentInDrawer(activity, drawerLayout, color, 0);
    }

    /**
     * <pre>
     *     适用于根布局为DrawerLayout
     *     使状态栏半透明，默认完全透明
     * <pre/>
     * @param activity
     * @param drawerLayout
     * @param color
     * @param statusBarAlpha
     */
    public static void setTranslucentInDrawer(@NonNull Activity activity,
                                              @NonNull DrawerLayout drawerLayout,
                                              @ColorInt int color,
                                              @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0+
            activity.getWindow().setStatusBarColor(calculateNewColor(color, statusBarAlpha));
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            setRootViewFit(activity, false);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4+
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            fitFakeView(activity, color, statusBarAlpha);
            setRootViewFit(activity, false);
            setDrawerFit(drawerLayout);
        }
    }

    /**
     * 使状态栏半透明，标题View向下偏移
     *
     * @param activity
     * @param statusBarAlpha
     */
    public static void setTranslucentOffset(@NonNull final Activity activity,
                                            @NonNull final View offsetView,
                                            @ColorInt final int color,
                                            @IntRange(from = 0, to = 255) final int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTranslucent(activity, color, statusBarAlpha);
        Object tag = offsetView.getTag(TAG_OFFSET_VIEW);
        if (tag != null && tag instanceof Boolean && (Boolean) tag) {
            return;
        }
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) offsetView.getLayoutParams();
        params.setMargins(params.leftMargin, params.topMargin + getStatusBarHeight(activity),
                params.rightMargin, params.bottomMargin);
        offsetView.setLayoutParams(params);
        offsetView.setTag(TAG_OFFSET_VIEW, true);
    }

    /**
     * Fragment中设置状态栏颜色，默认纯色
     *
     * @param activity
     * @param fragment
     * @param color
     */
    public static void setStatusBarColorInFragment(@NonNull Activity activity, @NonNull Fragment fragment, @ColorInt int color) {
        setStatusBarColorInFragment(activity, fragment, color, 255);
    }

    /**
     * Fragment中设置状态栏颜色
     *
     * @param activity
     * @param fragment
     * @param color
     * @param statusBarAlpha
     */
    public static void setStatusBarColorInFragment(@NonNull final Activity activity,
                                                   @NonNull final Fragment fragment,
                                                   @ColorInt final int color,
                                                   @IntRange(from = 0, to = 255) final int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setStatusBarColor(activity, color, statusBarAlpha);
        ViewGroup contentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        contentView.setPadding(0, 0, 0, 0);
        //设置头部偏移
        if (null != fragment.getView()) {
            fragment.getView().setPadding(0, getStatusBarHeight(activity), 0, 0);
        } else {
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    if (fragment.getView() != null) {
                        fragment.getView().setPadding(0, getStatusBarHeight(activity), 0, 0);
                    }
                }
            });
        }
    }


    /**
     * 添加一个自适应FakeView
     *
     * @param activity
     * @param color
     * @param statusBarAlpha
     */
    private static void fitFakeView(Activity activity, @ColorInt int color, int statusBarAlpha) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        if (null != decorView) {
            int count = decorView.getChildCount();
            View fakeView;
            for (int i = 0; i < count; i++) {
                if (decorView.getChildAt(i) instanceof FakeView) {
                    //refresh
                    fakeView = decorView.getChildAt(i);
                    if (fakeView.getVisibility() == View.GONE) {
                        fakeView.setVisibility(View.VISIBLE);
                    }
                    fakeView.setBackgroundColor(calculateNewColor(color, statusBarAlpha));
                    return;
                }
            }
            //add
            fakeView = generateFakeView(activity, color, statusBarAlpha);
            decorView.addView(fakeView);
        }
    }

    /**
     * 适配DrawerLayout
     *
     * @param drawerLayout
     */
    private static void setDrawerFit(DrawerLayout drawerLayout) {
        drawerLayout.setFitsSystemWindows(false);
        drawerLayout.getChildAt(0).setFitsSystemWindows(false);
        drawerLayout.getChildAt(1).setFitsSystemWindows(false);
    }

    /**
     * 设置根布局相关参数
     *
     * @param activity
     */
    private static void setRootViewFit(Activity activity, boolean fitsSystemWindow) {
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT)).getChildAt(0);
        ViewCompat.setFitsSystemWindows(rootView, fitsSystemWindow);
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
    public static int getStatusBarHeight(@NonNull Context context) {
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
    public static int calculateNewColor(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
}