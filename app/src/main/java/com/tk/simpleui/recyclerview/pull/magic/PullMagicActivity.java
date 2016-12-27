package com.tk.simpleui.recyclerview.pull.magic;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.tk.simpleui.R;
import com.tk.simpleui.common.DensityUtil;
import com.tk.simpleui.recyclerview.SimpleAdapter;
import com.tk.simpleui.recyclerview.pull.EmptyLayout;
import com.tk.simpleui.recyclerview.pull.EndLayout;
import com.tk.simpleui.recyclerview.pull.PullableRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PullMagicActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, PullableRecyclerView.OnLoadListener {

    private SwipeRefreshLayout swipeLayout;
    private PullableRecyclerView pullView;

    private SimpleAdapter adapter;
    private List<String> mList = new ArrayList<>();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_normal);
        initView();
    }

    private void initView() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        pullView = (PullableRecyclerView) findViewById(R.id.pull_view);
        swipeLayout.setProgressViewOffset(false, 0, DensityUtil.dp2px(this, 24));
        swipeLayout.setOnRefreshListener(this);

        adapter = new SimpleAdapter(this, mList);
        pullView.setAdapter(adapter);
        pullView.setLayoutManager(new LinearLayoutManager(this));
        pullView.setEmptyView(new EmptyLayout(this));
        pullView.setEndView(new EndLayout(this));
        pullView.refreshComplete();

        pullView.setOnLoadListener(this);
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
    protected void onDestroy() {
        super.onDestroy();
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
