package com.tk.simpleui.bar.normal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.tk.simpleui.R;
import com.tk.simpleui.bar.BarUtils;

/**
 * Created by TK on 2017/1/1.
 */

public class TranslucentActivity extends AppCompatActivity {
    private SeekBar seekbar;
    private int color = 0xff1e85d4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_translucent);
        seekbar = (SeekBar) findViewById(R.id.seekbar);

        BarUtils.setTranslucent(this);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BarUtils.setTranslucent(TranslucentActivity.this, color, (int) (progress / 100f * 255));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
