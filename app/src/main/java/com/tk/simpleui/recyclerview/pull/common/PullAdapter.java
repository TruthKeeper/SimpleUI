package com.tk.simpleui.recyclerview.pull.common;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by TK on 2016/10/20.
 */

public class PullAdapter extends RecyclerView.Adapter {
    public static final int TYPE_EMPTY = 23333;
    public static final int TYPE_END = 24444;

    private RecyclerView.Adapter mAdapter;

    private View emptyView;
    private View endView;

    public PullAdapter(RecyclerView.Adapter mAdapter, View emptyView, View endView) {
        this.mAdapter = mAdapter;
        this.emptyView = emptyView;
        this.endView = endView;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAdapter.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup lookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == TYPE_EMPTY) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (getItemViewType(position) == TYPE_END) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (lookup != null) {
                        return lookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        if (mAdapter.getItemCount() == 0) {
            return TYPE_EMPTY;
        } else {
            if (position == mAdapter.getItemCount()) {
                return TYPE_END;
            } else {
                return mAdapter.getItemViewType(position);
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return new UIHolder(emptyView);
        } else if (viewType == TYPE_END) {
            return new UIHolder(endView);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (!(holder instanceof UIHolder)) {
            mAdapter.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //empty
    }

    @Override
    public int getItemCount() {
        if (endView == null) {
            throw new IllegalArgumentException("EndView is null!");
        }
        if (mAdapter.getItemCount() == 0) {
            return emptyView == null ? 0 : 1;
        }
        return mAdapter.getItemCount() + 1;
    }

    /**
     * 获取源dapter
     *
     * @return
     */
    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public static class UIHolder extends RecyclerView.ViewHolder {

        public UIHolder(View itemView) {
            super(itemView);
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void setEndView(View endView) {
        this.endView = endView;
    }
}
