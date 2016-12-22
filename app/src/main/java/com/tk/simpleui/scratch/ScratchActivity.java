package com.tk.simpleui.scratch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tk.simpleui.R;

public class ScratchActivity extends AppCompatActivity {
    private ScratchView scratchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        scratchView = (ScratchView) findViewById(R.id.scratch_view);
        scratchView.setOnScratchListener(new ScratchView.OnScratchListener() {
            @Override
            public void onComplete() {
                Toast.makeText(ScratchActivity.this, "恭喜，中奖了!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void again(View v) {
        scratchView.scratchAgain();
    }

}
