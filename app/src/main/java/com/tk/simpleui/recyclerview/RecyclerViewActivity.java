package com.tk.simpleui.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tk.simpleui.R;
import com.tk.simpleui.recyclerview.pull.PullActivity;


public class RecyclerViewActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPull;
    private Button btnItemDecoration;
    private Button btnItemAnimtor;
    private Button btnLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        initView();
    }

    private void initView() {
        btnPull = (Button) findViewById(R.id.btn_pull);
        btnItemDecoration = (Button) findViewById(R.id.btn_item_decoration);
        btnItemAnimtor = (Button) findViewById(R.id.btn_item_animtor);
        btnLayoutManager = (Button) findViewById(R.id.btn_layout_manager);

        btnPull.setOnClickListener(this);
        btnItemDecoration.setOnClickListener(this);
        btnItemAnimtor.setOnClickListener(this);
        btnLayoutManager.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pull:
                startActivity(new Intent(this, PullActivity.class));
                break;
            case R.id.btn_item_decoration:
                break;
            case R.id.btn_item_animtor:
                break;
            case R.id.btn_layout_manager:
                break;
        }
    }
}
