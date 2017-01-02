package com.tk.simpleui.statusbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tk.simpleui.Item;
import com.tk.simpleui.MainAdapter;
import com.tk.simpleui.NoLastItemDecoration;
import com.tk.simpleui.R;
import com.tk.simpleui.statusbar.more.one.OneActivity;
import com.tk.simpleui.statusbar.normal.SolidActivity;
import com.tk.simpleui.statusbar.normal.TranslucentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TK on 2016/12/30.
 */

public class StatusBarActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private List<Item> mList = new ArrayList<>();
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new NoLastItemDecoration(this, 0, 0));

        mList.add(new Item("纯色沉浸式+透明度", new Intent(this, SolidActivity.class), true));
        mList.add(new Item("Translucent半透明模式", new Intent(this, TranslucentActivity.class), true));
        mList.add(new Item("Fragment_复杂场景1", new Intent(this, OneActivity.class), true));
//        mList.add(new Item("Fragment_复杂场景2", new Intent(this,. class),true));

        adapter = new MainAdapter(this, mList);
        recyclerview.setAdapter(adapter);
    }

}
