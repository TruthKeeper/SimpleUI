package com.tk.simpleui.ripple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tk.simpleui.R;
import com.tk.simpleui.ripple.reveal.RevealHelper;

public class RippleActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView reveal1;
    private TextView reveal2;
    private TextView reveal3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);

        reveal1 = (TextView) findViewById(R.id.reveal_1);
        reveal2 = (TextView) findViewById(R.id.reveal_2);
        reveal3 = (TextView) findViewById(R.id.reveal_3);

        reveal1.setOnClickListener(this);
        reveal2.setOnClickListener(this);
        reveal3.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reveal_1:
                RevealHelper.startCircleReveal(reveal1,
                        0, 0,
                        0, (float) (reveal1.getWidth() * Math.sqrt(2)));
                break;
            case R.id.reveal_2:
                RevealHelper.startCircleReveal(reveal2,
                        reveal2.getMeasuredWidth() >> 1, reveal2.getMeasuredHeight() >> 1,
                        0, (float) Math.hypot(reveal2.getMeasuredWidth(), reveal2.getMeasuredHeight()) / 2,
                        1000);
                break;
            case R.id.reveal_3:

                break;
        }
    }
}
