package com.tk.simpleui.indicator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tk.simpleui.R;
import com.tk.simpleui.common.DensityUtil;

/**
 * Created by TK on 2017/2/6.
 */

public class IndicatorActivity extends AppCompatActivity {
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private ImageView iv5;
    private Button btnGet;
    private Button btnRead;

    private int msg = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);
        iv1 = (ImageView) findViewById(R.id.iv_1);
        iv2 = (ImageView) findViewById(R.id.iv_2);
        iv3 = (ImageView) findViewById(R.id.iv_3);
        iv4 = (ImageView) findViewById(R.id.iv_4);
        iv5 = (ImageView) findViewById(R.id.iv_5);
        btnGet = (Button) findViewById(R.id.btn_get);
        btnRead = (Button) findViewById(R.id.btn_read);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg += 9;
                refresh();
            }
        });
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = 0;
                refresh();
            }
        });

    }

    private void refresh() {
        IndicatorFactory.with(this)
                .gravity(Gravity.END | Gravity.TOP)
                .size(DensityUtil.dp2px(this, 12))
                .shape(IndicatorView.Shape.POINT)
                .text(msg)
                .build()
                .bind(iv1);
        IndicatorFactory.with(this)
                .gravity(Gravity.END | Gravity.TOP)
                .size(DensityUtil.dp2px(this, 18))
                .shape(IndicatorView.Shape.CIRCLE)
                .backgroundColor(0xff1e85d4)
                .text(msg)
                .build()
                .bind(iv2);
        IndicatorFactory.with(this)
                .gravity(Gravity.END | Gravity.TOP)
                .size(DensityUtil.dp2px(this, 18))
                .textSize(14)
                .shape(IndicatorView.Shape.RECT)
                .textColor(Color.BLACK)
                .text(msg)
                .needPlus(false)
                .build()
                .bind(iv3);
        IndicatorFactory.with(this)
                .gravity(Gravity.END | Gravity.TOP)
                .size(DensityUtil.dp2px(this, 18))
                .shape(IndicatorView.Shape.ROUND_RECT)
                .text(msg)
                .needPlus(false)
                .build()
                .bind(iv4);
        IndicatorFactory.with(this)
                .gravity(Gravity.START | Gravity.BOTTOM)
                .size(DensityUtil.dp2px(this, 18))
                .bottomMargin(DensityUtil.dp2px(this, 6))
                .shape(IndicatorView.Shape.CIRCLE)
                .text(msg)
                .needPlus(false)
                .build()
                .bind(iv5);
    }
}
