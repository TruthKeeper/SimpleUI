package com.tk.simpleui.viewremover;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tk.simpleui.Item;
import com.tk.simpleui.MainAdapter;
import com.tk.simpleui.NoLastItemDecoration;
import com.tk.simpleui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TK on 2017/2/17.
 */

public class ViewRemoverActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private List<Item> mList = new ArrayList<>();
    private MainAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_remover);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        recyclerview.addItemDecoration(new NoLastItemDecoration(this, 0, 0));

//        mList.add(new Item("回收", new Intent(this,  .class), true));
//        mList.add(new Item("粒子", new Intent(this,  .class), true));

        adapter = new MainAdapter(this, mList);
        recyclerview.setAdapter(adapter);
    }
}
