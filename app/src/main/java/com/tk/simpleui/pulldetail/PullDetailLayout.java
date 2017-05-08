package com.tk.simpleui.pulldetail;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by TK on 2016/11/8.
 */

public class PullDetailLayout extends ViewGroup {
    public static final int SPEED = 400;

    private ViewGroup[] scrollViews = new ViewGroup[2];
    private float mLastY;
    private int mCurrentItem = 0;
    private Scroller mScroller;
    private VelocityTracker mTracker;

    public PullDetailLayout(Context context) {
        this(context, null);
    }

    public PullDetailLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullDetailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context, new AccelerateDecelerateInterpolator());
        mTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int height = t;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.layout(l + getPaddingLeft(),
                    height,
                    r - getPaddingRight(),
                    height + child.getMeasuredHeight());
            height += child.getMeasuredHeight();
        }
        scrollViews[0] = (ViewGroup) getChildAt(0);
        scrollViews[1] = (ViewGroup) getChildAt(1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!ViewCompat.canScrollVertically(scrollViews[0], 1)) {
                    //Top滑到底了
                    if (event.getY() - mLastY < 0) {
                        //上拉手势
                        if (!ViewCompat.canScrollVertically(scrollViews[1], -1)) {
                            //Bottom滑到顶了
                            if (getScrollY() >= scrollViews[0].getMeasuredHeight()) {
                                //Bottom完全可见
                                return false;
                            } else {
                                return true;
                            }
                        } else {
                            return false;
                        }
                    } else if (event.getY() - mLastY > 0) {
                        //下拉手势
                        if (getScrollY() <= 0) {
                            return false;
                        } else if (getScrollY() >= scrollViews[0].getMeasuredHeight()) {
                            if (!ViewCompat.canScrollVertically(scrollViews[1], -1)) {
                                //Bottom滑到顶了
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onInterceptTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) -(event.getY() - mLastY);
                if (getScrollY() + dy <= 0) {
                    dy = -getScrollY();
                }
                if (getScrollY() + dy >= scrollViews[0].getMeasuredHeight()) {
                    dy = scrollViews[0].getMeasuredHeight() - getScrollY();
                }
                scrollBy(0, dy);
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (mScroller.computeScrollOffset()) {
                    break;
                }
                mTracker.computeCurrentVelocity(500);
                float speed = Math.abs(mTracker.getYVelocity());
                if (mCurrentItem == 0) {
                    if (getScrollY() >= scrollViews[0].getMeasuredHeight() / 4 || speed >= SPEED) {
                        //大于1/4上拉
                        mScroller.startScroll(0, getScrollY(), 0, scrollViews[0].getMeasuredHeight() - getScrollY());
                        mCurrentItem = 1;
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                    }
                } else {
                    if (getScrollY() <= scrollViews[0].getMeasuredHeight() / 4 * 3 || speed >= SPEED) {
                        //大于1/4下拉
                        mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                        mCurrentItem = 0;
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, scrollViews[0].getMeasuredHeight() - getScrollY());
                    }
                }
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTracker.recycle();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }
}