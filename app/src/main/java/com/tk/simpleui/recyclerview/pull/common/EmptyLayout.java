package com.tk.simpleui.recyclerview.pull.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tk.simpleui.R;


/**
 * Created by TK on 2016/11/2.
 */

public class EmptyLayout extends RelativeLayout {
    private TextView contentView;

    public EmptyLayout(Context context) {
        this(context, null);
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.pull_empty_layout, this);
        contentView = (TextView) findViewById(R.id.content);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public EmptyLayout setContent(CharSequence content) {
        contentView.setText(content);
        return this;
    }
}
