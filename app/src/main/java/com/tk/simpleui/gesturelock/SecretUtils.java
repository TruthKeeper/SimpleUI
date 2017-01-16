package com.tk.simpleui.gesturelock;

/**
 * Created by TK on 2017/1/16.
 * 图案加密工具
 */

public final class SecretUtils {


    public static String initSecret(int mode) {
        switch (mode) {
            case 0:
                return "0124678";
            case 1:
                return "01698";
            case 2:
                return "05107894";
        }
        return null;
    }
}
