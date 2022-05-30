package com.example.musicplayer.common;

import android.content.res.Resources;

public class AppUtils {
    private static float density;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        if (density == 0)
            density = Resources.getSystem().getDisplayMetrics().density;
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

}
