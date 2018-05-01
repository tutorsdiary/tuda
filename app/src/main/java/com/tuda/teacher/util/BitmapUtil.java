package com.tuda.teacher.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import com.tuda.teacher.ui.activity.MainActivity;

public class BitmapUtil {
    /**
     * Bitmap을 ratio에 맞춰서 max값 만큼 resize한다.
     *
     * @param src 원본
     * @param max 원하는 크기의 값
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap src, int max) {
        if(src == null)
            return null;

        int width = src.getWidth();
        int height = src.getHeight();
        float rate = 0.0f;

        if (width > height) {
            rate = max / (float) width;
            height = (int) (height * rate);
            width = max;
        } else {
            rate = max / (float) height;
            width = (int) (width * rate);
            height = max;
        }

        return Bitmap.createScaledBitmap(src, width, height, true);
    }

    /**
     * Bitmap을 ratio에 맞춰서 max값 만큼 resize한다.
     *
     * @param src
     * @param max
     * @param isKeep 작은 크기인 경우 유지할건지 체크..
     * @return
     */
    public static Bitmap resize(Bitmap src, int max, boolean isKeep) {
        if(!isKeep)
            return resizeBitmap(src, max);

        int width = src.getWidth();
        int height = src.getHeight();
        float rate = 0.0f;

        if (width > height) {
            if (max > width) {
                rate = max / (float) width;
                height = (int) (height * rate);
                width = max;
            }
        } else {
            if (max > height) {
                rate = max / (float) height;
                width = (int) (width * rate);
                height = max;
            }
        }

        return Bitmap.createScaledBitmap(src, width, height, true);
    }

    /**
     * Bitmap 이미지를 정사각형으로 만든다.
     *
     * @param src 원본
     * @param max 사이즈
     * @return
     */
    public static Bitmap resizeSquare(Bitmap src, int max) {
        if(src == null)
            return null;

        return Bitmap.createScaledBitmap(src, max, max, true);
    }


    /**
     * Bitmap 이미지를 가운데를 기준으로 w, h 크기 만큼 crop한다.
     *
     * @param src 원본
     * @param w 넓이
     * @param h 높이
     * @return
     */
    public static Bitmap cropCenterBitmap(Bitmap src, int w, int h) {
        if(src == null)
            return null;

        int width = src.getWidth();
        int height = src.getHeight();

        if(width < w && height < h)
            return src;

        int x = 0;
        int y = 0;

        if(width > w)
            x = (width - w) / 2;

        if(height > h)
            y = (height - h) / 2;

        int cw = w; // crop width
        int ch = h; // crop height

        if(w > width)
            cw = width;

        if(h > height)
            ch = height;

        return Bitmap.createBitmap(src, x, y, cw, ch);
    }

    /**
     * Bitmap 이미지의 가로폭을 레이아웃의 가로폭에 맞춰 비율대로 resize 한 후
     *          이미지를 가운데를 기준으로 h 크기 만큼 crop한다.
     *
     * @param view 원본의 부모 레이아웃
     * @param src 원본
     * @param h 높이
     * @return
     */
    public static Bitmap cropCenterBitmapMaxWidth(View view, Bitmap src, int h) {
        if(view == null || src == null)
            return src;
        if (view.getWidth() == 0)
            return src;

        int width = src.getWidth();
        int height = src.getHeight();

        float rate = view.getWidth() / (float) width;
        height = (int) (height * rate);

        Bitmap resizeBmp = Bitmap.createScaledBitmap(src, view.getWidth(), height, true);

        return resizeBmp;

        /*int y = 0;

        if(height > h)
            y = (height - h) / 2;

        int ch = h; // crop height

        if(h > height)
            ch = height;

        return Bitmap.createBitmap(resizeBmp, 0, y, view.getWidth(), ch);*/
    }

    public static Bitmap screenCapture(View view) {
        if (view == null)
            return null;

        //view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }

    public static Bitmap blur(Context context, Bitmap sentBitmap, int radius) {
        if (context == null || sentBitmap == null)
            return null;

        // 젤리빈 이상일 경우 blur 처리
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN)
            return null;

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius); //0.0f ~ 25.0f
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);

        return bitmap;
    }
}
