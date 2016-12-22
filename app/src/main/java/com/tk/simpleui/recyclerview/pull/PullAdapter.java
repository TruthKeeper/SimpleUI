package com.tk.simpleui.recyclerview.pull;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by TK on 2016/10/20.
 */

public class PullAdapter extends RecyclerView.Adapter {
    public static final int TYPE_EMPTY = 23333;
    public static final int TYPE_END = 23334;

    private RecyclerView.Adapter mAdapter;

    private View emptyView;
    private View endView;

    public PullAdapter(RecyclerView.Adapter mAdapter, View emptyView, View endView) {
        this.mAdapter = mAdapter;
        this.emptyView = emptyView;
        this.endView = endView;
    }

    @Override
    public int getItemViewType(int position) {
        if (emptyView != null && mAdapter.getItemCount() == 0) {
            return TYPE_EMPTY;
        }
        if (position == mAdapter.getItemCount() && endView != null) {
            return TYPE_END;
        } else {
            return mAdapter.getItemViewType(position);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof UIHolder)) {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapter.getItemCount() == 0) {
            if (emptyView != null) {
                return 1;
            }
        }
        return endView == null ? mAdapter.getItemCount() : mAdapter.getItemCount() + 1;
    }

    class UIHolder extends RecyclerView.ViewHolder {

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
