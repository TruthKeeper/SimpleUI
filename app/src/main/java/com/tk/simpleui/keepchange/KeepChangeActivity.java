package com.tk.simpleui.keepchange;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tk.simpleui.R;

/**
 * Created by TK on 2016/12/30.
 */

public class KeepChangeActivity extends AppCompatActivity {
    private KeepChangeLayout changeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep_change);

        changeLayout = (KeepChangeLayout) findViewById(R.id.change_layout);

    }

    public void confirm(View view) {
        Toast.makeText(this, changeLayout.getNowSum() + "", Toast.LENGTH_SHORT).show();
    }
}
