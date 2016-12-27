package com.tk.simpleui.recyclerview.pull.elegance;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tk.simpleui.R;
import com.tk.simpleui.common.DensityUtil;
import com.tk.simpleui.recyclerview.SimpleAdapter;
import com.tk.simpleui.recyclerview.pull.common.EmptyLayout;
import com.tk.simpleui.recyclerview.pull.common.EndLayout;
import com.tk.simpleui.recyclerview.pull.common.PullableRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by TK on 2016/12/25.
 */

public class TextFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener, PullableRecyclerView.OnLoadListener {
    private SwipeRefreshLayout swipeLayout;
    private PullableRecyclerView pullView;

    private SimpleAdapter adapter;
    private List<String> mList = new ArrayList<>();
    private Handler handler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pull, null);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        pullView = (PullableRecyclerView) view.findViewById(R.id.pull_view);
        initView();
        return view;
    }

    private void initView() {
        swipeLayout.setProgressViewOffset(false, 0, DensityUtil.dp2px(getContext(), 24));
        swipeLayout.setOnRefreshListener(this);

        adapter = new SimpleAdapter(getContext(), mList);
        pullView.setAdapter(adapter);
        pullView.setLayoutManager(new LinearLayoutManager(getContext()));
        pullView.setEmptyView(new EmptyLayout(getContext()));
        pullView.setEndView(new EndLayout(getContext()));
        pullView.refreshComplete();

        pullView.setOnLoadListener(this);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        swipeLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                for (int i = 0; i < 20; i++) {
                    mList.add("第" + i + "条数据");
                }
                swipeLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
                pullView.setLoadResult(true, false);
            }
        }, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onLoad(PullableRecyclerView pullView) {
        getData();
    }

    @Override
    public void onReLoad(PullableRecyclerView pullView) {
        getData();
    }

    private void getData() {
        if (mList.size() < 25) {
            final int r = new Random().nextInt(2);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (r > 0) {
                        mList.add("新数据");
                        adapter.notifyItemRangeInserted(adapter.getItemCount() - 1, 1);
                        pullView.setLoadResult(true, false);
                    } else {
                        pullView.setLoadResult(false, false);
                    }
                }
            }, 1000);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullView.setLoadResult(true, true);
                }
            }, 1000);
        }
    }
}
