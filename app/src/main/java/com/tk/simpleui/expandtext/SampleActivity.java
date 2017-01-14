package com.tk.simpleui.expandtext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tk.simpleui.R;

/**
 * Created by TK on 2017/1/10.
 */

public class SampleActivity extends AppCompatActivity {
    private ExpandableTextView expand1;
    private ExpandableTextView expand2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandtext_sample);
        expand1 = (ExpandableTextView) findViewById(R.id.expand_1);
        expand2 = (ExpandableTextView) findViewById(R.id.expand_2);
        expand1.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onStatusChange(int status) {
                if (status == ExpandableTextView.STATUS_EXPAND) {
                    Toast.makeText(SampleActivity.this, "1打开了", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SampleActivity.this, "1关闭了", Toast.LENGTH_SHORT).show();
                }
            }
        });
        expand2.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onStatusChange(int status) {
                if (status == ExpandableTextView.STATUS_EXPAND) {
                    Toast.makeText(SampleActivity.this, "2打开了", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SampleActivity.this, "2关闭了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
