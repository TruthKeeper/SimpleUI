package com.tk.simpleui;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by TK on 2016/12/3.
 * 无最后一条
 */
public class NoLastItemDecoration extends PaddingItemDecoration {

    public NoLastItemDecoration(Context mContext, int paddingLeft, int paddingRight) {
        super(mContext, paddingLeft, paddingRight);
    }

    public NoLastItemDecoration(Context mContext, int paddingLeft, int paddingRight, int color, int divier) {
        super(mContext, paddingLeft, paddingRight, color, divier);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin +
                        Math.round(ViewCompat.getTranslationY(child));
                final int bottom = top + divider;
                mDrawable.setBounds(left + paddingLeft, top, right - paddingRight, bottom);
                mDrawable.draw(c);
            }
        } else if (parent.getLayoutManager() instanceof GridLayoutManager) {
            int span = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();

            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            int l = childCount / span;
            int off = childCount % span;
            for (int i = 0; i < (off == 0 ? l - 1 : l) * span; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin +
                        Math.round(ViewCompat.getTranslationY(child));
                final int bottom = top + divider;
                mDrawable.setBounds(left + paddingLeft, top, right - paddingRight, bottom);
                mDrawable.draw(c);
            }

        }
    }
}
