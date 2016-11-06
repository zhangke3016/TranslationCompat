package com.mrzk.transitioncontroller.controller.utils;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by zhangke on 2016-11-3.
 */
public class BitmapUtil {
    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    public static Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }
}
