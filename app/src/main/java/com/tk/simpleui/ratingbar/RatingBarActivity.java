package com.tk.simpleui.ratingbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tk.simpleui.R;

public class RatingBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratingbar);

        ((TRatingbar) findViewById(R.id.ratingbar)).setOnRatingListener(new TRatingbar.OnRatingChangeListener() {
            @Override
            public void onRating(int ratingScore) {
                Toast.makeText(RatingBarActivity.this, ratingScore + "星", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
