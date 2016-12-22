package com.tk.simpleui;

import android.content.Intent;

/**
 * Created by TK on 2016/12/20.
 */

public class Item {
    private String content;
    private Intent intent;
    private boolean done;

    public Item(String content, Intent intent, boolean done) {
        this.content = content;
        this.intent = intent;
        this.done = done;
    }

    public String getContent() {
        return content;
    }

    public Intent getIntent() {
        return intent;
    }


    public boolean isDone() {
        return done;
    }
}
