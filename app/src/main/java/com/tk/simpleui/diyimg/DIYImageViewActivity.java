package com.tk.simpleui.diyimg;

import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.tk.simpleui.R;
import com.tk.simpleui.common.DensityUtil;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class DIYImageViewActivity extends AppCompatActivity {


    private ImageView roundL;
    private ImageView circleL;
    private RoundRectImageView roundFit;
    private CircleImageView roundCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy_imageview);
        roundL = (ImageView) findViewById(R.id.round_l);
        circleL = (ImageView) findViewById(R.id.circle_l);
        roundFit = (RoundRectImageView) findViewById(R.id.round_fit);
        roundCircle = (CircleImageView) findViewById(R.id.round_circle);

        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            roundL.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int round = DensityUtil.dp2px(DIYImageViewActivity.this, 6);
                    if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                        outline.setRoundRect(0, 0, roundL.getMeasuredWidth(), roundL.getMeasuredHeight(), round);
                    }
                }
            });
            roundL.setClipToOutline(true);
            circleL.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                        outline.setOval(0, 0, circleL.getMeasuredWidth(), circleL.getMeasuredHeight());
                    }
                }
            });
            circleL.setClipToOutline(true);
        }
    }
}
