package com.tk.simpleui.recyclerview.Item;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tk.simpleui.Item;
import com.tk.simpleui.MainAdapter;
import com.tk.simpleui.NoLastItemDecoration;
import com.tk.simpleui.R;
import com.tk.simpleui.bubble.BubbleActivity;
import com.tk.simpleui.drag.DragActivity;
import com.tk.simpleui.load.LoadActivity;
import com.tk.simpleui.pulldetail.PullDetailActivity;
import com.tk.simpleui.recyclerview.pull.elegance.PullEleganceActivity;
import com.tk.simpleui.recyclerview.pull.magic.PullMagicActivity;
import com.tk.simpleui.recyclerview.pull.normal.PullNormalActivity;

import java.util.ArrayList;
import java.util.List;


public class ItemDecorationActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private List<Item> mList = new ArrayList<>();
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        initView();
    }

    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new NoLastItemDecoration(this, 0, 0));

        mList.add(new Item("下拉刷新上拉加载_普通青年", new Intent(this, PullNormalActivity.class), true));
        mList.add(new Item("下拉刷新上拉加载_文艺青年", new Intent(this, PullEleganceActivity.class), true));
        mList.add(new Item("下拉刷新上拉加载_中二青年", new Intent(this, PullMagicActivity.class), true));
        mList.add(new Item("ItemDecoration", new Intent(this, LoadActivity.class), false));
        mList.add(new Item("ItemAnimator", new Intent(this, BubbleActivity.class), false));
        mList.add(new Item("LayoutManager", new Intent(this, PullDetailActivity.class), false));
        mList.add(new Item("条目点击", new Intent(this, DragActivity.class), false));

        adapter = new MainAdapter(this, mList);
        recyclerview.setAdapter(adapter);
    }


}
