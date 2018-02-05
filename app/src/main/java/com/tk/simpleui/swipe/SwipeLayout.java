package com.tk.simpleui.swipe;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tk.simpleui.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/10
 *      desc :
 * </pre>
 */

public class SwipeLayout extends FrameLayout {
    @IntDef({Direction.LEFT, Direction.TOP, Direction.RIGHT, Direction.BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
        int LEFT = 0x01;
        int TOP = 0x02;
        int RIGHT = 0x03;
        int BOTTOM = 0x04;
    }

    /**
     * 支持2种模式
     */
    public static final int MODE_PULL = 0x00;
    public static final int MODE_LAYER = 0x01;

    private ViewDragHelper mDragHelper;
    private View mContentView;

    private View mLeftMenuView;
    private int mLeftMode;
    private View mTopMenuView;
    private int mTopMode;
    private View mRightMenuView;
    private int mRightMode;
    private View mBottomMenuView;
    private int mBottomMode;

    /**
     * 坐标(0,0)为基准，不允许同时不为0
     */
    private int mCurrentLeft;
    private int mCurrentTop;
    /**
     * 记录上一次的
     */
    private int mLastLeft;
    private int mLastTop;
    private OnSwipeListener onSwipeListener;

    public SwipeLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            /**
             * 拖拽改变位置回调
             * @param changedView
             * @param left
             * @param top
             * @param dx
             * @param dy
             */
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (changedView == mContentView) {
                    mCurrentLeft = left;
                    mCurrentTop = top;
                } else if (changedView == mLeftMenuView && allowPositionChangeHorizontal()) {
                    mCurrentLeft += dx;
                    mCurrentLeft = Math.max(mCurrentLeft, 0);
                    mCurrentLeft = Math.min(mCurrentLeft, mLeftMenuView.getMeasuredWidth());
                    mCurrentTop = top;
                } else if (changedView == mTopMenuView && allowPositionChangeVertical()) {
                    mCurrentLeft = left;
                    mCurrentTop += dy;
                    mCurrentTop = Math.max(mCurrentTop, 0);
                    mCurrentTop = Math.min(mCurrentTop, mTopMenuView.getMeasuredHeight());
                } else if (changedView == mRightMenuView && allowPositionChangeHorizontal()) {
                    mCurrentLeft += dx;
                    mCurrentLeft = Math.max(mCurrentLeft, -mRightMenuView.getMeasuredWidth());
                    mCurrentLeft = Math.min(mCurrentLeft, 0);
                    mCurrentTop = top;
                } else if (changedView == mBottomMenuView && allowPositionChangeVertical()) {
                    mCurrentLeft = left;
                    mCurrentTop += dy;
                    mCurrentTop = Math.max(mCurrentTop, -mBottomMenuView.getMeasuredHeight());
                    mCurrentTop = Math.min(mCurrentTop, 0);
                }
                layoutSelf();
                invalidate();
            }

            /**
             * 松开
             * @param releasedChild
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                final float minVelocity = mDragHelper.getMinVelocity();
                final int leftMenuW = mLeftMenuView == null ? 0 : mLeftMenuView.getMeasuredWidth();
                final int rightMenuW = mRightMenuView == null ? 0 : mRightMenuView.getMeasuredWidth();
                final int topMenuH = mTopMenuView == null ? 0 : mTopMenuView.getMeasuredHeight();
                final int bottomMenuH = mBottomMenuView == null ? 0 : mBottomMenuView.getMeasuredHeight();
                if (mCurrentLeft != 0) {
                    //横向
                    if (Math.abs(xvel) < minVelocity) {
                        //判断是否过半
                        if (mCurrentLeft > (leftMenuW >> 1)) {
                            openLeft(true);
                        } else if (mCurrentLeft > 0) {
                            closeLeft(true);
                        } else if (mCurrentLeft > -(rightMenuW >> 1)) {
                            closeRight(true);
                        } else {
                            openRight(true);
                        }
                    } else {
                        //根据滑动方向
                        if (xvel > 0) {
                            if (mCurrentLeft > 0) {
                                openLeft(true);
                            } else {
                                closeRight(true);
                            }
                        } else {
                            if (mCurrentLeft > 0) {
                                closeLeft(true);
                            } else {
                                openRight(true);
                            }
                        }
                    }
                } else if (mCurrentTop != 0) {
                    //纵向
                    if (Math.abs(yvel) < minVelocity) {
                        //判断是否过半
                        if (mCurrentTop > (topMenuH >> 1)) {
                            openTop(true);
                        } else if (mCurrentTop > 0) {
                            closeTop(true);
                        } else if (mCurrentTop > -(bottomMenuH >> 1)) {
                            closeBottom(true);
                        } else {
                            openBottom(true);
                        }
                    } else {
                        //根据滑动方向
                        if (yvel > 0) {
                            if (mCurrentTop > 0) {
                                openTop(true);
                            } else {
                                closeBottom(true);
                            }
                        } else {
                            if (mCurrentTop > 0) {
                                closeTop(true);
                            } else {
                                openBottom(true);
                            }
                        }
                    }
                } else {
                    layoutSelf();
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 1;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return 1;
            }

            /**
             * 指定横向滑动范围
             * @param child
             * @param left
             * @param dx
             * @return
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //横向时禁止纵向
                if (mCurrentTop != 0 || (!allowPositionChangeHorizontal())) {
                    return 0;
                }
                final int contentW = mContentView.getMeasuredWidth();
                final int leftMenuW = mLeftMenuView == null ? 0 : mLeftMenuView.getMeasuredWidth();
                final int rightMenuW = mRightMenuView == null ? 0 : mRightMenuView.getMeasuredWidth();
                if (child == mContentView) {
                    if (left > 0) {
                        return Math.min(left, leftMenuW);
                    } else {
                        return Math.max(left, -rightMenuW);
                    }
                } else if (child == mLeftMenuView) {
                    if (left > 0) {
                        return mLeftMode == MODE_LAYER ? Math.min(left, leftMenuW) : 0;
                    } else {
                        return Math.max(left, -leftMenuW);
                    }
                } else if (child == mRightMenuView) {
                    if (left > contentW) {
                        return contentW;
                    } else {
                        return mRightMode == MODE_LAYER ? Math.max(left, contentW - (rightMenuW << 1))
                                : Math.max(left, contentW - rightMenuW);
                    }
                }
                return 0;
            }

            /**
             *  指定纵向滑动范围
             * @param child
             * @param top
             * @param dy
             * @return
             */
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                //纵向时禁止横向
                if (mCurrentLeft != 0 || (!allowPositionChangeVertical())) {
                    return 0;
                }

                final int contentH = mContentView.getMeasuredHeight();
                final int topMenuH = mTopMenuView == null ? 0 : mTopMenuView.getMeasuredHeight();
                final int bottomMenuH = mBottomMenuView == null ? 0 : mBottomMenuView.getMeasuredHeight();

                if (child == mContentView) {
                    if (top > 0) {
                        return Math.min(top, topMenuH);
                    } else {
                        return Math.max(top, -bottomMenuH);
                    }
                } else if (child == mTopMenuView) {
                    if (top > 0) {
                        return mTopMode == MODE_LAYER ? Math.min(top, topMenuH) : 0;
                    } else {
                        return Math.max(top, -topMenuH);
                    }
                } else if (child == mBottomMenuView) {
                    if (top > contentH) {
                        return contentH;
                    } else {
                        return mBottomMode == MODE_LAYER ? Math.max(top, contentH - (bottomMenuH << 1))
                                : Math.max(top, contentH - bottomMenuH);
                    }
                }
                return 0;
            }

            /**
             * 适配click场景
             * @return
             */
            private boolean allowPositionChangeHorizontal() {
                if (mTopMenuView != null && mCurrentTop == mTopMenuView.getMeasuredHeight()) {
                    return false;
                }
                if (mBottomMenuView != null && mCurrentTop == -mBottomMenuView.getMeasuredHeight()) {
                    return false;
                }
                return true;
            }

            /**
             * 适配click场景
             * @return
             */
            private boolean allowPositionChangeVertical() {
                if (mLeftMenuView != null && mCurrentLeft == mLeftMenuView.getMeasuredWidth()) {
                    return false;
                }
                if (mRightMenuView != null && mCurrentLeft == -mRightMenuView.getMeasuredWidth()) {
                    return false;
                }
                return true;
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount == 0) {
            throw new NullPointerException("no child");
        }
        for (int i = 0; i < childCount; i++) {
            if (i == childCount - 1) {
                //最后一个是主体
                mContentView = getChildAt(i);
            } else {
                //解析
                generateChildViews(getChildAt(i));
            }
        }
    }

    /**
     * 生成子孩子
     *
     * @param child
     */
    private void generateChildViews(View child) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        switch (layoutParams.position) {
            case 0:
                //left
                mLeftMenuView = child;
                mLeftMode = layoutParams.mode;
                break;
            case 1:
                //top
                mTopMenuView = child;
                mTopMode = layoutParams.mode;
                break;
            case 2:
                //right
                mRightMenuView = child;
                mRightMode = layoutParams.mode;
                break;
            case 3:
                //bottom
                mBottomMenuView = child;
                mBottomMode = layoutParams.mode;
                break;
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new LayoutParams(lp);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mDragHelper.shouldInterceptTouchEvent(event)) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        mDragHelper.continueSettling(true);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutSelf();
    }

    /**
     * 布局
     */
    private void layoutSelf() {
        if (mCurrentLeft * mCurrentTop != 0) {
            //不允许同时不为0
            mCurrentTop = 0;
            mCurrentLeft = 0;
        }
        final int contentW = mContentView.getMeasuredWidth();
        final int contentH = mContentView.getMeasuredHeight();
        mContentView.layout(mCurrentLeft,
                mCurrentTop,
                mCurrentLeft + contentW,
                mCurrentTop + contentH);
        if (mLeftMenuView != null && mLeftMenuView.getVisibility() != GONE) {
            if (mLeftMode == MODE_PULL) {
                mLeftMenuView.layout(mCurrentLeft - mLeftMenuView.getMeasuredWidth(),
                        mCurrentTop,
                        mCurrentLeft,
                        mCurrentTop + mLeftMenuView.getMeasuredHeight());
            } else if (mLeftMode == MODE_LAYER) {
                mLeftMenuView.layout(0,
                        0,
                        mLeftMenuView.getMeasuredWidth(),
                        mLeftMenuView.getMeasuredHeight());
            }
        }
        if (mTopMenuView != null && mTopMenuView.getVisibility() != GONE) {
            if (mTopMode == MODE_PULL) {
                mTopMenuView.layout(mCurrentLeft,
                        mCurrentTop - mTopMenuView.getMeasuredHeight(),
                        mCurrentLeft + mTopMenuView.getMeasuredWidth(),
                        mCurrentTop);
            } else if (mTopMode == MODE_LAYER) {
                mTopMenuView.layout(0,
                        0,
                        mTopMenuView.getMeasuredWidth(),
                        mTopMenuView.getMeasuredHeight());
            }
        }
        if (mRightMenuView != null && mRightMenuView.getVisibility() != GONE) {
            if (mRightMode == MODE_PULL) {
                mRightMenuView.layout(mCurrentLeft + contentW,
                        mCurrentTop,
                        mCurrentLeft + contentW + mRightMenuView.getMeasuredWidth(),
                        mCurrentTop + mRightMenuView.getMeasuredHeight());
            } else if (mRightMode == MODE_LAYER) {
                mRightMenuView.layout(contentW - mRightMenuView.getMeasuredWidth(),
                        0,
                        contentW,
                        mRightMenuView.getMeasuredHeight());
            }
        }
        if (mBottomMenuView != null && mBottomMenuView.getVisibility() != GONE) {
            if (mBottomMode == MODE_PULL) {
                mBottomMenuView.layout(mCurrentLeft,
                        mCurrentTop + contentH,
                        mCurrentLeft + mBottomMenuView.getMeasuredWidth(),
                        mCurrentTop + contentH + mBottomMenuView.getMeasuredHeight());
            } else if (mBottomMode == MODE_LAYER) {
                mBottomMenuView.layout(0,
                        contentH - mBottomMenuView.getMeasuredHeight(),
                        mBottomMenuView.getMeasuredWidth(),
                        contentH);
            }
        }

        if (onSwipeListener != null) {
            onSwipeListener.onPositionChange(SwipeLayout.this, mCurrentLeft, mCurrentTop);
            int dx = mCurrentLeft - mLastLeft;
            int dy = mCurrentTop - mLastTop;

            if (mCurrentLeft == 0 && mCurrentTop == 0) {
                //close
                if (dx > 0) {
                    //right close
                    onSwipeListener.onSwipeClose(this, Direction.RIGHT);
                    Log.e(getClass().getSimpleName(), "right close");
                } else if (dx < 0) {
                    //left close
                    onSwipeListener.onSwipeClose(this, Direction.LEFT);
                    Log.e(getClass().getSimpleName(), "left close");
                } else if (dy > 0) {
                    //bottom close
                    onSwipeListener.onSwipeClose(this, Direction.BOTTOM);
                    Log.e(getClass().getSimpleName(), "bottom close");
                } else if (dy < 0) {
                    //top close
                    onSwipeListener.onSwipeClose(this, Direction.TOP);
                    Log.e(getClass().getSimpleName(), "top close");
                }
            } else if (mLeftMenuView != null && mCurrentLeft == mLeftMenuView.getMeasuredWidth() && dx != 0) {
                onSwipeListener.onSwipeOpen(this, Direction.LEFT);
                Log.e(getClass().getSimpleName(), "left open");
            } else if (mTopMenuView != null && mCurrentTop == mTopMenuView.getMeasuredHeight() && dy != 0) {
                onSwipeListener.onSwipeOpen(this, Direction.TOP);
                Log.e(getClass().getSimpleName(), "top open");
            } else if (mRightMenuView != null && mCurrentLeft == -mRightMenuView.getMeasuredWidth() && dx != 0) {
                onSwipeListener.onSwipeOpen(this, Direction.RIGHT);
                Log.e(getClass().getSimpleName(), "right open");
            } else if (mBottomMenuView != null && mCurrentTop == -mBottomMenuView.getMeasuredHeight() && dy != 0) {
                onSwipeListener.onSwipeOpen(this, Direction.BOTTOM);
                Log.e(getClass().getSimpleName(), "bottom open");
            }
        }

        mLastLeft = mCurrentLeft;
        mLastTop = mCurrentTop;

    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    /**
     * 打开左侧视图
     *
     * @param smooth 是否平滑过渡
     */
    public void openLeft(boolean smooth) {
        if (mLeftMenuView != null && mCurrentTop == 0) {
            final int leftMenuW = mLeftMenuView.getMeasuredWidth();
            mCurrentTop = 0;
            if (smooth) {
                mDragHelper.smoothSlideViewTo(mContentView, leftMenuW, mCurrentTop);
                invalidate();
            } else {
                mCurrentLeft = leftMenuW;
                layoutSelf();
            }
        }
    }

    /**
     * 打开上方视图
     *
     * @param smooth 是否平滑过渡
     */
    public void openTop(boolean smooth) {
        if (mTopMenuView != null && mCurrentLeft == 0) {
            final int topMenuH = mTopMenuView.getMeasuredHeight();
            mCurrentLeft = 0;
            if (smooth) {
                mDragHelper.smoothSlideViewTo(mContentView, mCurrentLeft, topMenuH);
                invalidate();
            } else {
                mCurrentTop = topMenuH;
                layoutSelf();
            }
        }
    }

    /**
     * 打开右侧视图
     *
     * @param smooth 是否平滑过渡
     */
    public void openRight(boolean smooth) {
        if (mRightMenuView != null && mCurrentTop == 0) {
            final int rightMenuW = mRightMenuView.getMeasuredWidth();
            mCurrentTop = 0;
            if (smooth) {
                mDragHelper.smoothSlideViewTo(mContentView, -rightMenuW, mCurrentTop);
                invalidate();
            } else {
                mCurrentLeft = -rightMenuW;
                layoutSelf();
            }
        }
    }

    /**
     * 打开下方视图
     *
     * @param smooth 是否平滑过渡
     */
    public void openBottom(boolean smooth) {
        if (mBottomMenuView != null && mCurrentLeft == 0) {
            final int bottomMenuH = mBottomMenuView.getMeasuredHeight();
            mCurrentLeft = 0;
            if (smooth) {
                mDragHelper.smoothSlideViewTo(mContentView, mCurrentLeft, -bottomMenuH);
                invalidate();
            } else {
                mCurrentTop = -bottomMenuH;
                layoutSelf();
            }
        }
    }

    /**
     * 关闭左侧视图
     *
     * @param smooth 是否平滑过渡
     */
    public void closeLeft(boolean smooth) {
        if (mLeftMenuView != null && mCurrentLeft != 0) {
            resetPosition(smooth);
        }
    }

    /**
     * 关闭上方视图
     *
     * @param smooth 是否平滑过渡
     */
    public void closeTop(boolean smooth) {
        if (mTopMenuView != null && mCurrentTop != 0) {
            resetPosition(smooth);
        }
    }

    /**
     * 关闭右侧视图
     *
     * @param smooth 是否平滑过渡
     */
    public void closeRight(boolean smooth) {
        if (mRightMenuView != null && mCurrentLeft != 0) {
            resetPosition(smooth);
        }
    }

    /**
     * 关闭下方视图
     *
     * @param smooth 是否平滑过渡
     */
    public void closeBottom(boolean smooth) {
        if (mBottomMenuView != null && mCurrentTop != 0) {
            resetPosition(smooth);
        }
    }

    /**
     * 还原位置
     *
     * @param smooth 是否平滑过渡
     */
    public void resetPosition(boolean smooth) {
        if (smooth) {
            mDragHelper.smoothSlideViewTo(mContentView, 0, 0);
            invalidate();
        } else {
            mCurrentTop = 0;
            mCurrentLeft = 0;
            layoutSelf();
        }
    }

    /**
     * 扩展
     */
    public static class LayoutParams extends FrameLayout.LayoutParams {
        public int position;
        public int mode;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.SwipeLayout);
            position = array.getInt(R.styleable.SwipeLayout_sl_position, -1);
            mode = array.getInt(R.styleable.SwipeLayout_sl_mode, MODE_PULL);
            array.recycle();
        }

        public LayoutParams(@Px int width, @Px int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    /**
     * 监听
     */
    public interface OnSwipeListener {
        void onSwipeOpen(@NonNull SwipeLayout view, @Direction int direction);

        void onSwipeClose(@NonNull SwipeLayout view, @Direction int direction);

        void onPositionChange(@NonNull SwipeLayout view, @IntRange(from = 0) int currentX, @IntRange(from = 0) int currentY);
    }
}
