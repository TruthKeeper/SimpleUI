package com.tk.simpleui.expandtext;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tk.simpleui.R;
import com.tk.simpleui.expandtext.list.ListActivity;

/**
 * Created by TK on 2017/1/10.
 */

public class ExpandTextActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandtext);
    }

    public void sample(View v) {
        startActivity(new Intent(this, SampleActivity.class));
    }

    public void list(View v) {
        startActivity(new Intent(this, ListActivity.class));
    }
}
