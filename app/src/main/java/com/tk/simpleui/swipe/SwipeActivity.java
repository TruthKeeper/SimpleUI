package com.tk.simpleui.swipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tk.simpleui.R;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/11
 *      desc :
 * </pre>
 */
public class SwipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
    }

    public void sample(View v) {
        startActivity(new Intent(this, SwipeSampleActivity.class));
    }

    public void list(View v) {
        startActivity(new Intent(this, SwipeListActivity.class));
    }

    public void viewpager(View v) {
        startActivity(new Intent(this, SwipeViewPagerActivity.class));
    }
}
