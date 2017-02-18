package com.tk.simpleui.recyclerview.pull.magic;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.tk.simpleui.R;
import com.tk.simpleui.recyclerview.SimpleAdapter;
import com.tk.simpleui.recyclerview.pull.common.EmptyLayout;
import com.tk.simpleui.recyclerview.pull.common.EndLayout;
import com.tk.simpleui.recyclerview.pull.common.PullableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PullMagicActivity extends AppCompatActivity {
    /**
     * 注意，如果使用ScrollView进行监听扩展，在6.0+真机上无法显示RecyclerView，需重写ScrollView的OnMeasure
     */
    private MagicNestedScrollView scrollview;
    private PullableRecyclerView pullView;
    private SimpleAdapter adapter;
    private List<String> mList = new ArrayList<>();
    private Handler handler = new Handler();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_magic);
        dialog = new ProgressDialog(this);
        initView();
    }

    private void initView() {
        scrollview = (MagicNestedScrollView) findViewById(R.id.scrollview);
        pullView = (PullableRecyclerView) findViewById(R.id.pull_view);
        scrollview.setOnScrollListener(new MagicNestedScrollView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(MagicNestedScrollView view, int scrollState) {
                pullView.applyInNesting(scrollState);
            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {

            }
        });
        adapter = new SimpleAdapter(this, mList);
        pullView.setAdapter(adapter);
        pullView.setLayoutManager(new LinearLayoutManager(this));
        pullView.setEmptyView(new EmptyLayout(this));
        pullView.setEndView(new EndLayout(this));
        pullView.prepare();
        pullView.setNestedScrollingEnabled(false);
        pullView.setOnLoadListener(new PullableRecyclerView.OnLoadListener() {
            @Override
            public void onLoad(final PullableRecyclerView pullView) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mList.add("新数据");
                        adapter.notifyItemRangeInserted(adapter.getItemCount() - 1, 1);
                        pullView.setLoadResult(PullableRecyclerView.Status.LOAD_STANDBY);
                        pullView.requestLayout();
                    }
                }, 1000);
            }

            @Override
            public void onReLoad(PullableRecyclerView pullView) {

            }
        });
        dialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    mList.add("第" + i + "条数据");
                }
                dialog.dismiss();
                adapter.notifyDataSetChanged();
                pullView.setLoadResult(PullableRecyclerView.Status.LOAD_STANDBY);
            }
        }, 2000);


    }


}
