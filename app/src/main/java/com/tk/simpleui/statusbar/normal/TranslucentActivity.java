package com.tk.simpleui.statusbar.normal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.tk.simpleui.R;
import com.tk.simpleui.statusbar.StatusBarHelper;

/**
 * Created by TK on 2017/1/1.
 */

public class TranslucentActivity extends AppCompatActivity {
    private SeekBar seekbar;
    private int color = 0xdddddd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_translucent);
        seekbar = (SeekBar) findViewById(R.id.seekbar);

        StatusBarHelper.setTranslucent(this, color);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                StatusBarHelper.setTranslucent(TranslucentActivity.this, color, (int) (progress / 100f * 255));
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
