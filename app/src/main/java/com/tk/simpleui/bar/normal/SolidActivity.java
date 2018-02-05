package com.tk.simpleui.bar.normal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.tk.simpleui.R;
import com.tk.simpleui.bar.BarUtils;

/**
 * Created by TK on 2016/12/30.
 */

public class SolidActivity extends AppCompatActivity {

    private SeekBar seekbar;
    private int color = 0xffff6262;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_solid);
        seekbar = (SeekBar) findViewById(R.id.seekbar);

        BarUtils.setStatusBarColor(this, color);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BarUtils.setStatusBarColor(SolidActivity.this, color, (int) (progress / 100f * 255));
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
