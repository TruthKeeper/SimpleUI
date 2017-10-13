package com.tk.simpleui.swipe;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tk.simpleui.R;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/10
 *      desc :
 * </pre>
 */
public class SwipeSampleActivity extends AppCompatActivity implements SwipeLayout.OnSwipeListener, View.OnClickListener {

    private TextView position;
    private SwipeLayout swipe;
    private LinearLayout btnLeft;
    private LinearLayout btnTop;
    private LinearLayout btnRight;
    private LinearLayout btnBottom;
    private LinearLayout btnContent;


    private boolean smooth = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_sample);

        position = (TextView) findViewById(R.id.position);
        swipe = (SwipeLayout) findViewById(R.id.swipe);
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.btn_top).setOnClickListener(this);
        findViewById(R.id.btn_right).setOnClickListener(this);
        findViewById(R.id.btn_bottom).setOnClickListener(this);
        findViewById(R.id.btn_content).setOnClickListener(this);
        swipe.setOnSwipeListener(this);
    }

    public void openLeft(View v) {
        swipe.openLeft(smooth);
    }

    public void closeLeft(View v) {
        swipe.closeLeft(smooth);
    }

    public void openRight(View v) {
        swipe.openRight(smooth);
    }

    public void closeRight(View v) {
        swipe.closeRight(smooth);
    }

    public void openTop(View v) {
        swipe.openTop(smooth);
    }

    public void closeTop(View v) {
        swipe.closeTop(smooth);
    }

    public void openBottom(View v) {
        swipe.openBottom(smooth);
    }

    public void closeBottom(View v) {
        swipe.closeBottom(smooth);
    }


    @Override
    public void onSwipeOpen(@NonNull SwipeLayout view, @SwipeLayout.Direction int direction) {
        switch (direction) {
            case SwipeLayout.Direction.LEFT:
                Toast.makeText(this, "左侧打开了", Toast.LENGTH_SHORT).show();
                break;
            case SwipeLayout.Direction.TOP:
                Toast.makeText(this, "上方打开了", Toast.LENGTH_SHORT).show();
                break;
            case SwipeLayout.Direction.RIGHT:
                Toast.makeText(this, "右侧打开了", Toast.LENGTH_SHORT).show();
                break;
            case SwipeLayout.Direction.BOTTOM:
                Toast.makeText(this, "下方打开了", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onSwipeClose(@NonNull SwipeLayout view, @SwipeLayout.Direction int direction) {
        switch (direction) {
            case SwipeLayout.Direction.LEFT:
                Toast.makeText(this, "左侧关闭了", Toast.LENGTH_SHORT).show();
                break;
            case SwipeLayout.Direction.TOP:
                Toast.makeText(this, "上方关闭了", Toast.LENGTH_SHORT).show();
                break;
            case SwipeLayout.Direction.RIGHT:
                Toast.makeText(this, "右侧关闭了", Toast.LENGTH_SHORT).show();
                break;
            case SwipeLayout.Direction.BOTTOM:
                Toast.makeText(this, "下方关闭了", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPositionChange(@NonNull SwipeLayout view, @IntRange(from = 0) int currentX, @IntRange(from = 0) int currentY) {
        this.position.setText("内容布局坐标：(" + currentX + " , " + currentY + ")");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                Toast.makeText(this, "点击了左侧", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_top:
                Toast.makeText(this, "点击了上方", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_right:
                Toast.makeText(this, "点击了右侧", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_bottom:
                Toast.makeText(this, "点击了下方", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_content:
                Toast.makeText(this, "点击了主体", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
