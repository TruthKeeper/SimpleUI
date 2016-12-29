package com.tk.simpleui.line;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tk.simpleui.R;

public class InterestingLineActivity extends AppCompatActivity {
    private EditText aX;
    private EditText aY;
    private EditText aSpeed;
    private EditText bX;
    private EditText bY;
    private EditText bSpeed;
    private Button btnStart;
    private InterestingLineView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesting_line);

        aX = (EditText) findViewById(R.id.a_x);
        aY = (EditText) findViewById(R.id.a_y);
        aSpeed = (EditText) findViewById(R.id.a_speed);
        bX = (EditText) findViewById(R.id.b_x);
        bY = (EditText) findViewById(R.id.b_y);
        bSpeed = (EditText) findViewById(R.id.b_speed);
        btnStart = (Button) findViewById(R.id.btn_start);
        view = (InterestingLineView) findViewById(R.id.view);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ax = Float.parseFloat(aX.getText().toString().trim());
                float ay = Float.parseFloat(aY.getText().toString().trim());
                float aS = Float.parseFloat(aSpeed.getText().toString().trim());
                float bx = Float.parseFloat(bX.getText().toString().trim());
                float by = Float.parseFloat(bY.getText().toString().trim());
                float bS = Float.parseFloat(bSpeed.getText().toString().trim());
                view.start(ax, ay, aS, bx, by, bS);
            }
        });
    }

}
