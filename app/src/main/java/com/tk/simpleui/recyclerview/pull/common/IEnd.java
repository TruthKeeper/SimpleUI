package com.tk.simpleui.recyclerview.pull.common;

/**
 * Created by TK on 2016/11/2.
 */

public interface IEnd {
    /**
     * 上拉加载失败
     * 一般用于网络异常
     */
    void onError();

    /**
     * 显示
     * 一般用于开始动画等操作
     */
    void onShow();

    /**
     * 加载完毕，隐藏
     * 一般用于动画资源等的回收
     */
    void onDismiss();

    /**
     * 到底了
     * 一般指上拉加载无数据
     */
    void inTheEnd();
}
