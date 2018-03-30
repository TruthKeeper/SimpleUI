package com.tk.simpleui;

import android.app.Application;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/02/05
 *     desc   : xxxx描述
 * </pre>
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
