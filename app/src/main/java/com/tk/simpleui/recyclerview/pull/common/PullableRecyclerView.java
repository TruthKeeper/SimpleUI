package com.tk.simpleui.recyclerview.pull.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by TK on 2016/10/20.
 * RecyclerView
 * 上拉加载
 * TODO 瀑布流
 */

public class PullableRecyclerView extends RecyclerView {
    /**
     * 待命，加载失败，加载完毕，无新数据，加载中
     */
    public enum Status {
        LOAD_STANDBY,
        LOAD_ERROR,
        LOAD_END,
        LOAD_ING,
    }

    private OnScrollListener onScrollListener;
    private OnLoadListener onLoadListener;
    /**
     * 装饰者Adapter
     */
    private PullAdapter pullAdapter;
    /**
     * 真实的Adapter
     */
    private Adapter sourceAdapter;
    /**
     * 空数据时的展示View
     */
    private View emptyView;
    /**
     * 上拉加载View
     */
    private View endView;

    private Status status = Status.LOAD_STANDBY;

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


    private void init() {
        setHasFixedSize(true);
        onScrollListener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
                    /**
                     * 悬停状态
                     * Adapter有数据
                     * 待命状态
                     * 滚动到底
                     */
                    if (newState == SCROLL_STATE_IDLE
                            && (sourceAdapter != null && sourceAdapter.getItemCount() != 0)
                            && (status == Status.LOAD_STANDBY)
                            && (manager.findLastVisibleItemPosition() == sourceAdapter.getItemCount()
                            && manager.findFirstCompletelyVisibleItemPosition() != 0)) {
                        //load ing
                        status = Status.LOAD_ING;
                        endView.setVisibility(VISIBLE);
                        ((IEnd) endView).onShow();
                        if (onLoadListener != null) {
                            onLoadListener.onLoad(PullableRecyclerView.this);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        };
        addOnScrollListener(onScrollListener);
    }

    /**
     * 用于嵌套
     *
     * @param state
     */
    public void applyInNesting(int state) {
        if (getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
            /**
             * 悬停状态
             * Adapter有数据
             * 待命状态
             * 滚动到底
             */
            if (state == SCROLL_STATE_IDLE
                    && (sourceAdapter != null && sourceAdapter.getItemCount() != 0)
                    && (status == Status.LOAD_STANDBY)) {
                View view = manager.findViewByPosition(sourceAdapter.getItemCount());
                int[] location = new int[2];
                view.getLocationInWindow(location);
                if (location[1] <= getResources().getDisplayMetrics().heightPixels) {
                    //可见了
                    status = Status.LOAD_ING;
                    endView.setVisibility(VISIBLE);
                    ((IEnd) endView).onShow();
                    if (onLoadListener != null) {
                        onLoadListener.onLoad(PullableRecyclerView.this);
                    }
                }
            }
        }
    }

    /**
     * 下拉刷新数据调用前进行初始化
     */
    public void prepare() {
        if (endView == null) {
            throw new IllegalArgumentException("EndView is null!");
        }
        endView.setVisibility(GONE);
        status = Status.LOAD_STANDBY;
    }

    /**
     * 加载完回调
     *
     * @param status
     */
    public void setLoadResult(Status status) {
        if (status == Status.LOAD_END) {
            this.status = status;
            ((IEnd) endView).inTheEnd();
        } else if (status == Status.LOAD_ERROR) {
            this.status = status;
            ((IEnd) endView).onError();
        } else if (status == Status.LOAD_STANDBY) {
            this.status = status;
            ((IEnd) endView).onDismiss();
        }
    }

    /**
     * 设置底部View IEnd实现类
     *
     * @param endView
     */
    public void setEndView(@NonNull View endView) {
        if (!(endView instanceof IEnd)) {
            throw new IllegalArgumentException("EndView is error!");
        }
        this.endView = endView;
        this.endView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == Status.LOAD_ERROR) {
                    status = Status.LOAD_ING;
                    ((IEnd) v).onShow();
                    if (onLoadListener != null) {
                        onLoadListener.onReLoad(PullableRecyclerView.this);
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
    public void setEmptyView(@Nullable View emptyView) {
        this.emptyView = emptyView;
        if (pullAdapter != null) {
            pullAdapter.setEmptyView(emptyView);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof PullAdapter) {
            sourceAdapter = ((PullAdapter) adapter).getAdapter();
        } else {
            sourceAdapter = adapter;
        }
        pullAdapter = new PullAdapter(sourceAdapter, emptyView, endView);
        super.setAdapter(pullAdapter);
        sourceAdapter.registerAdapterDataObserver(mDataObserver);
    }

    @Override
    public Adapter getAdapter() {
        return sourceAdapter;
    }

    public PullAdapter getPullAdapter() {
        return pullAdapter;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (onScrollListener != null) {
            removeOnScrollListener(onScrollListener);
            onScrollListener = null;
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
        this.onLoadListener = onLoadListener;
    }

    public interface OnLoadListener {
        /**
         * 触发下拉刷新
         *
         * @param pullView
         */
        void onLoad(PullableRecyclerView pullView);

        /**
         * 网络异常是内置点击重试
         *
         * @param pullView
         */
        void onReLoad(PullableRecyclerView pullView);
    }
}
