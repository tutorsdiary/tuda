package com.tuda.teacher.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by crazyluv on 2015. 11. 27..
 */
public class DPManager {
    public static float px2dp(Context context, float px)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static int dp2px(Context context, float dp)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
