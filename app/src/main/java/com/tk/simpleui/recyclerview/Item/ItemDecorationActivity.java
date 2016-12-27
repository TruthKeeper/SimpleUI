package com.tk.simpleui.recyclerview.Item;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tk.simpleui.NoLastItemDecoration;
import com.tk.simpleui.R;
import com.tk.simpleui.recyclerview.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;


public class ItemDecorationActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerview;


    private SimpleAdapter adapter;
    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_decoration);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(this);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new NoLastItemDecoration(this, 0, 0));
        for (int i = 0; i < 20; i++) {
            mList.add("第" + i + "条数据");
        }
        adapter = new SimpleAdapter(this, mList);
        recyclerview.setAdapter(adapter);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.linear:
                recyclerview.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.grid:
                recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case R.id.staggered:
                recyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                break;
        }
        return false;
    }
}
