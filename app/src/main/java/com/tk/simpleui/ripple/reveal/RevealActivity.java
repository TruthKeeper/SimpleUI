package com.tk.simpleui.ripple.reveal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tk.simpleui.R;

public class RevealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal);
        RevealHelper.startCircleReveal(this, 1000);
    }


}
