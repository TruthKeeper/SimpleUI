package com.tk.simpleui.recyclerview.layout;

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
import com.tk.simpleui.diyimg.DIYImageViewActivity;
import com.tk.simpleui.drag.DragActivity;
import com.tk.simpleui.flod.FlodActivity;
import com.tk.simpleui.flow.FlowActivity;
import com.tk.simpleui.load.LoadActivity;
import com.tk.simpleui.recyclerview.RecyclerViewActivity;
import com.tk.simpleui.ripple.RippleActivity;
import com.tk.simpleui.scratch.ScratchActivity;
import com.tk.simpleui.pulldetail.PullDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class LayoutActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private List<Item> mList = new ArrayList<>();
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new NoLastItemDecoration(this, 0, 0));

        mList.add(new Item("自定义ImageView", new Intent(this, DIYImageViewActivity.class), true));
        mList.add(new Item("刮奖", new Intent(this, ScratchActivity.class), true));
        mList.add(new Item("流式布局", new Intent(this, FlowActivity.class), true));
        mList.add(new Item("Load加载", new Intent(this, LoadActivity.class), false));
        mList.add(new Item("气泡粘连", new Intent(this, BubbleActivity.class), false));
        mList.add(new Item("上拉查看详情", new Intent(this, PullDetailActivity.class), true));
        mList.add(new Item("拖拽", new Intent(this, DragActivity.class), false));
        mList.add(new Item("涟漪", new Intent(this, RippleActivity.class), false));
        mList.add(new Item("折叠布局", new Intent(this, FlodActivity.class), false));
        mList.add(new Item("One Piece：RecyclerView", new Intent(this, RecyclerViewActivity.class), false));

        adapter = new MainAdapter(this, mList);
        recyclerview.setAdapter(adapter);
    }


}