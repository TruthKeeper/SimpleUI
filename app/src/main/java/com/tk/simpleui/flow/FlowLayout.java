package com.tk.simpleui.flow;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by TK on 2016/12/20.
 * 流式自动换行布局
 */

public class FlowLayout extends ViewGroup {

    private FlowAdapter mAdapter;
    private AdapterDataObserver mDataObserver = new AdapterDataObserver();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        setLayoutTransition(new LayoutTransition());
    }

    public void setAdapter(FlowAdapter adapter) {
        if (adapter == null) {
            return;
        }

        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }
        mAdapter = adapter;
        adapter.registerDataSetObserver(mDataObserver);
        refresh(adapter.getItemCount());
    }

    private void refresh(int newCount) {
        int oldCount = getChildCount();
        if (oldCount > newCount) {
            removeViews(newCount, oldCount - newCount);
            requestLayout();
        } else if (oldCount < newCount) {
            for (int i = oldCount; i < newCount; i++) {
                View child = mAdapter.getView(this, i);
                addView(child);
            }
        }
    }

    public FlowAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);

        int resultW = 0;
        int resultH = 0;
        //行最大宽度
        int lineWidth = 0;
        //行最大高度
        int lineHeight = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            //测量子View
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            if (child.getVisibility() == GONE) {
                continue;
            }
            ViewGroup.MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //获取真实占据的空间
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > sizeW - getPaddingLeft() - getPaddingRight()) {
                //需要换行
                resultW = Math.max(lineWidth, childWidth);
                resultH += lineHeight;
                lineWidth = childWidth;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == count - 1) {
                resultW = Math.max(resultW, lineWidth);
                resultH += lineHeight;
            }

        }
        setMeasuredDimension(modeW == MeasureSpec.EXACTLY ? sizeW : resultW + getPaddingLeft() + getPaddingRight(),
                modeH == MeasureSpec.EXACTLY ? sizeH : resultH + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lineWidth = 0;
        int lineHeight = 0;

        int sumHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //获取真实占据的空间
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (childWidth + lineWidth > r - l - getPaddingLeft() - getPaddingRight()) {
                //需要换行
                //上一行的最大行高
                sumHeight += lineHeight;
                lineHeight = childHeight;
                lineWidth = 0;
                child.layout(getPaddingLeft() + lp.leftMargin,
                        getPaddingTop() + sumHeight + lp.topMargin,
                        getPaddingLeft() + lp.leftMargin + child.getMeasuredWidth(),
                        getPaddingTop() + sumHeight + lp.topMargin + child.getMeasuredHeight());
                lineWidth += childWidth;

            } else {
                child.layout(getPaddingLeft() + lineWidth + lp.leftMargin,
                        getPaddingTop() + sumHeight + lp.topMargin,
                        getPaddingLeft() + lineWidth + lp.leftMargin + child.getMeasuredWidth(),
                        getPaddingTop() + sumHeight + lp.topMargin + child.getMeasuredHeight());
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
        }
    }

    public class AdapterDataObserver {
        public void onChanged() {
            refresh(mAdapter.getItemCount());
        }

    }
}

