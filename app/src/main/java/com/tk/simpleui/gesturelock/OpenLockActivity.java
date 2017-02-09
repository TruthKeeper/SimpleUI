package com.tk.simpleui.gesturelock;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tk.simpleui.R;

public class OpenLockActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvPsd;
    private Button btnNormal;
    private Button btnHard;
    private Button btnHentail;
    private Button btnSet;
    private GesureLockView lockView;

    private int mode = 0;
    private String secret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_lock);
        tvPsd = (TextView) findViewById(R.id.tv_psd);
        btnNormal = (Button) findViewById(R.id.btn_normal);
        btnHard = (Button) findViewById(R.id.btn_hard);
        btnHentail = (Button) findViewById(R.id.btn_hentail);
        btnSet = (Button) findViewById(R.id.btn_set);
        lockView = (GesureLockView) findViewById(R.id.lock_view);
        btnNormal.setOnClickListener(this);
        btnHard.setOnClickListener(this);
        btnHentail.setOnClickListener(this);
        btnSet.setOnClickListener(this);
        initSecret();
        lockView.setOnGestureListener(new GesureLockView.OnGestureListener() {
            @Override
            public void onDragOver(GesureLockView view, String source) {
                if (secret.equals(source)) {
                    Toast.makeText(OpenLockActivity.this, "成功解锁", Toast.LENGTH_SHORT).show();
                    view.delayClearMark();
                } else {
                    Toast.makeText(OpenLockActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    view.setFailureGesture();
                }
            }

            @Override
            public void onFailure(GesureLockView view) {

            }
        });
    }

    private void initSecret() {
        secret = SecretUtils.initSecret(mode);
        tvPsd.setText("密码："+secret);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_normal:
                mode = 0;
                initSecret();
                lockView.build(new GesureLockView.Builder()
                        .setColumn(3)
                        .setRow(3)
                        .setDraggingGuide(false)
                        .setFailureColor(0xffff0000)
                        .setThemeColor(0xff1e85d4));
                break;
            case R.id.btn_hard:
                mode = 1;
                initSecret();
                lockView.build(new GesureLockView.Builder()
                        .setColumn(4)
                        .setRow(4)
                        .setDraggingGuide(false)
                        .setFailureColor(0xffff0000)
                        .setThemeColor(Color.YELLOW));
                break;
            case R.id.btn_hentail:
                mode = 2;
                initSecret();
                lockView.build(new GesureLockView.Builder()
                        .setColumn(5)
                        .setRow(5)
                        .setDraggingGuide(false)
                        .setFailureColor(0xffbbbbbb)
                        .setThemeColor(Color.GREEN));
                break;
            case R.id.btn_set:
                startActivity(new Intent(this, SetLockActivity.class));
                break;
        }
    }
}
