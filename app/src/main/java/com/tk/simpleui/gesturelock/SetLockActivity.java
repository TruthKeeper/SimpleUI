package com.tk.simpleui.gesturelock;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tk.simpleui.R;

public class SetLockActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvPsd;
    private Button btnNormal;
    private Button btnHard;
    private Button btnHentail;
    private GesureLockView lockView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lock);
        tvPsd = (TextView) findViewById(R.id.tv_psd);
        btnNormal = (Button) findViewById(R.id.btn_normal);
        btnHard = (Button) findViewById(R.id.btn_hard);
        btnHentail = (Button) findViewById(R.id.btn_hentail);
        lockView = (GesureLockView) findViewById(R.id.lock_view);
        btnNormal.setOnClickListener(this);
        btnHard.setOnClickListener(this);
        btnHentail.setOnClickListener(this);

        lockView.setOnGestureListener(new GesureLockView.OnGestureListener() {
            @Override
            public void onDragOver(GesureLockView view, String source) {
                Toast.makeText(SetLockActivity.this, source, Toast.LENGTH_SHORT).show();
                view.delayClearMark();
            }

            @Override
            public void onFailure(GesureLockView view) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_normal:
                lockView.build(new GesureLockView.Builder()
                        .setColumn(3)
                        .setRow(3)
                        .setDraggingGuide(false)
                        .setFailureColor(0xffff0000)
                        .setThemeColor(0xff1e85d4));
                break;
            case R.id.btn_hard:
                lockView.build(new GesureLockView.Builder()
                        .setColumn(4)
                        .setRow(4)
                        .setDraggingGuide(false)
                        .setFailureColor(0xffff0000)
                        .setThemeColor(Color.YELLOW));
                break;
            case R.id.btn_hentail:
                lockView.build(new GesureLockView.Builder()
                        .setColumn(5)
                        .setRow(5)
                        .setDraggingGuide(false)
                        .setFailureColor(0xffbbbbbb)
                        .setThemeColor(Color.GREEN));
                break;
        }
    }

}
