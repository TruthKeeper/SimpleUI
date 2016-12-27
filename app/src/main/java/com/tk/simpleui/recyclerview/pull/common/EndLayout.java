package com.tk.simpleui.recyclerview.pull.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tk.simpleui.R;


/**
 * Created by TK on 2016/11/2.
 */

public class EndLayout extends LinearLayout implements IEnd {

    private ProgressBar pull_progressbar;
    private TextView pull_tip;

    public EndLayout(Context context) {
        this(context, null);
    }

    public EndLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EndLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.pull_end_layout, this);
        pull_progressbar = (ProgressBar) findViewById(R.id.pull_progressbar);
        pull_tip = (TextView) findViewById(R.id.pull_tip);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onInit() {
        pull_progressbar.setVisibility(VISIBLE);
        pull_tip.setText("卖力加载中");
    }

    @Override
    public void onError() {
        pull_progressbar.setVisibility(GONE);
        pull_tip.setText("网络异常，点击重试");
    }


    @Override
    public void onDismiss() {
        setVisibility(GONE);
    }

    @Override
    public void inTheEnd() {
        pull_progressbar.setVisibility(GONE);
        pull_tip.setText("到底了");
    }
}
