package com.tk.simpleui.recyclerview.pull;

/**
 * Created by TK on 2016/11/2.
 */

public interface IEnd {
    /**
     * 上拉加载失败
     */
    void onError();

    /**
     * 初始化显示
     */
    void onInit();

    /**
     * 初始化消失
     */
    void onDismiss();

    /**
     * 到底了
     */
    void inTheEnd();
}
