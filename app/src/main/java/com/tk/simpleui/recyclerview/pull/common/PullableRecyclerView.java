package com.tk.simpleui.recyclerview.pull.common;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by TK on 2016/10/20.
 * 上拉加载RecyclerView
 */

public class PullableRecyclerView extends RecyclerView {
    private OnScrollListener mOnScrollListener;
    private OnLoadListener mOnLoadListener;

    private PullAdapter pullAdapter;
    private Adapter sourceAdapter;

    private View emptyView;
    private View endView;

    private boolean loadError;
    private boolean isLoading;
    private boolean isInTheEnd;

    public PullableRecyclerView(Context context) {
        this(context, null);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Init
     */
    private void init() {
        setHasFixedSize(true);
        mOnScrollListener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager m = (LinearLayoutManager) getLayoutManager();
                    if (newState == SCROLL_STATE_IDLE
                            //有数据
                            && (sourceAdapter != null && sourceAdapter.getItemCount() != 0)
                            && (!isLoading)
                            && (!loadError)
                            && (!isInTheEnd)
                            && (m.findLastVisibleItemPosition() == m.getItemCount() - 1)
                            && m.findFirstCompletelyVisibleItemPosition() != 0) {
                        //到达底部，开始执行加载
                        isLoading = true;
                        ((IEnd) endView).onInit();
                        endView.setVisibility(VISIBLE);
                        if (mOnLoadListener != null) {
                            mOnLoadListener.onLoad(PullableRecyclerView.this);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        };
        addOnScrollListener(mOnScrollListener);
    }

    /**
     * 嵌套的计算方案
     *
     * @param state
     */
    public void applyNestScrollState(int state) {
        if (getLayoutManager() instanceof LinearLayoutManager) {
            //LinearLayoutManager会失效的Plan B
            LinearLayoutManager m = (LinearLayoutManager) getLayoutManager();
            if (state == SCROLL_STATE_IDLE
                    //有数据
                    && (sourceAdapter != null && sourceAdapter.getItemCount() != 0)
                    && (!isLoading)
                    && (!loadError)
                    && (!isInTheEnd)) {
                View view = m.findViewByPosition(m.getItemCount() - 1);
                int[] location = new int[2];
                view.getLocationInWindow(location);
                if (location[1] <= getResources().getDisplayMetrics().heightPixels) {
                    //可见了
                    isLoading = true;
                    ((IEnd) endView).onInit();
                    endView.setVisibility(VISIBLE);
                    if (mOnLoadListener != null) {
                        mOnLoadListener.onLoad(PullableRecyclerView.this);
                    }
                }

            }
        }

    }

    /**
     * 下拉刷新数据调用进行初始化
     */
    public void refreshComplete() {
        endView.setVisibility(GONE);
        this.loadError = false;
        this.isLoading = false;
        this.isInTheEnd = false;
    }

    /**
     * 加载完回调
     *
     * @param success
     * @param inTheEnd
     */
    public void setLoadResult(boolean success, boolean inTheEnd) {
        this.isInTheEnd = inTheEnd;
        if (inTheEnd) {
            this.isInTheEnd = true;
            ((IEnd) endView).inTheEnd();
            return;
        }
        loadError = !success;
        isLoading = false;
        if (success) {
            ((IEnd) endView).onDismiss();
        } else {
            ((IEnd) endView).onError();
        }
    }

    /**
     * 设置底部View IEnd实现类
     *
     * @param endView
     */
    public void setEndView(View endView) {
        if (this.endView != null) {
            this.endView.setOnClickListener(null);
        }
        this.endView = endView;
        this.endView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadError && (!isLoading)) {
                    isLoading = true;
                    ((IEnd) v).onInit();
                    if (mOnLoadListener != null) {
                        mOnLoadListener.onReLoad(PullableRecyclerView.this);
                    }

                }
            }
        });
        if (pullAdapter != null) {
            pullAdapter.setEndView(endView);
        }
    }

    /**
     * 设置空数据View
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        if (pullAdapter != null) {
            pullAdapter.setEmptyView(emptyView);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        sourceAdapter = adapter;
        pullAdapter = new PullAdapter(sourceAdapter, emptyView, endView);
        super.setAdapter(pullAdapter);
        sourceAdapter.registerAdapterDataObserver(mDataObserver);
    }

    @Override
    public PullAdapter getAdapter() {
        return (PullAdapter) super.getAdapter();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mOnScrollListener != null) {
            removeOnScrollListener(mOnScrollListener);
            mOnScrollListener = null;
        }
        if (sourceAdapter != null && mDataObserver != null) {
            sourceAdapter.unregisterAdapterDataObserver(mDataObserver);
        }
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            pullAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            pullAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            pullAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            pullAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            pullAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            pullAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

    public interface OnLoadListener {
        void onLoad(PullableRecyclerView pullView);

        void onReLoad(PullableRecyclerView pullView);
    }
}
