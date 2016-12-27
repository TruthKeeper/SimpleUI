package com.tk.simpleui.recyclerview.pull.magic;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by TK on 2016/12/23.
 */

public class MagicNestedScrollView extends NestedScrollView {
    private static final int CHECK_WHAT = 233;
    private static final int DELAY = 20;
    private boolean isTouch;
    private int mLastY;
    private OnScrollListener onScrollListener;
    private Handler checkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CHECK_WHAT) {
                if (mLastY == getScrollY()) {
                    if (onScrollListener != null) {
                        onScrollListener.onScrollStateChanged(MagicNestedScrollView.this, OnScrollListener.SCROLL_STATE_IDLE);
                    }
                }
            }
        }
    };

    public MagicNestedScrollView(Context context) {
        this(context, null);
    }

    public MagicNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isTouch = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isTouch = false;
                checkHandler.removeMessages(CHECK_WHAT);
                checkHandler.sendEmptyMessageDelayed(CHECK_WHAT, DELAY);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener == null) {
            return;
        }
        mLastY = t;
        if (isTouch) {
            if (t != oldt) {
                // 有手指触摸，并且位置有滚动
                onScrollListener.onScrollStateChanged(this, OnScrollListener.SCROLL_STATE_DRAGGING);
            }
        } else {
            if (t != oldt) {
                // 没有手指触摸，并且位置有滚动，fling
                onScrollListener.onScrollStateChanged(this, OnScrollListener.SCROLL_STATE_FLING);
                // 记住滑动的位置
                checkHandler.removeMessages(CHECK_WHAT);
                checkHandler.sendEmptyMessageDelayed(CHECK_WHAT, DELAY);
            }
        }
        onScrollListener.onScrollChanged(l, t, oldl, oldt);

    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 滚动扩展监听
     */
    public interface OnScrollListener {
        /**
         * 静止
         */
        public static int SCROLL_STATE_IDLE = 0;
        /**
         * 拖拽
         */
        public static int SCROLL_STATE_DRAGGING = 1;
        /**
         * 自由滑动
         */
        public static int SCROLL_STATE_FLING = 2;


        /**
         * 滑动状态回调
         *
         * @param view
         * @param scrollState 滚动的状态
         */
        public void onScrollStateChanged(MagicNestedScrollView view, int scrollState);

        /**
         * 滑动位置回调
         *
         * @param l
         * @param t
         * @param oldl
         * @param oldt
         */
        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
