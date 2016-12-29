package com.tk.simpleui.recyclerview.layout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tk.simpleui.R;
import com.tk.simpleui.recyclerview.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

public class LayoutActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private RecyclerView recyclerview;
    private Toolbar toolbar;

    private SimpleAdapter adapter;
    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.recyclerview_layout);
        toolbar.setOnMenuItemClickListener(this);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.diamond:
                break;
            case R.id.hexagon:
                break;
        }
        return false;
    }
}
