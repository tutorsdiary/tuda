package com.tuda.teacher.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.tuda.teacher.R;

import java.io.File;

public class ImageManager {
    public static void setImageResource(Context context, String path, String fileName, ImageView imageView) {
        try {
            // 이미지 가져오기
            if (fileName == null || fileName.length() <= 4) {
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.common_student_default));
            } else {
                Bitmap bmp = getBitmapResource(path, fileName);
                if (bmp!= null)
                    imageView.setImageBitmap(bmp);
                else
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.common_student_default));
            }
        } catch (Exception e) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.common_student_default));
        }
    }

    public static void deleteImageResource(Context context, String path, String fileName) {
        try {
            File files = new File(path, fileName);
            if (files.exists())
                files.delete();
        } catch (Exception e) {}
    }

    private static Bitmap getBitmapResource(String path, String fileName) {
        try {
            if (new File(path, fileName).exists())
                return BitmapFactory.decodeFile(path + File.separator + fileName);
        } catch (Exception e) {}

        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable) drawable).getBitmap();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);

    }

    public static Bitmap addShadow(final Bitmap bm, final int dstHeight, final int dstWidth, int size, float dx, float dy, int color, int alpha) {
        final Bitmap mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8);
        final Matrix scaleToFit = new Matrix();
        final RectF src = new RectF(0, 0, bm.getWidth(), bm.getHeight());
        final RectF dst = new RectF(0, 0, dstWidth - dx, dstHeight - dy);
            scaleToFit.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
        final Matrix dropShadow = new Matrix(scaleToFit);
            dropShadow.postTranslate(dx, dy);
        final Canvas maskCanvas = new Canvas(mask);
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            maskCanvas.drawBitmap(bm, scaleToFit, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            maskCanvas.drawBitmap(bm, dropShadow, paint);
        final BlurMaskFilter filter = new BlurMaskFilter(size, BlurMaskFilter.Blur.SOLID);
            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor(color);
            paint.setAlpha(alpha);
            paint.setMaskFilter(filter);
            paint.setFilterBitmap(true);
        final Bitmap ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888.ARGB_8888);
        final Canvas retCanvas = new Canvas(ret);
            retCanvas.drawBitmap(mask, 0, 0, paint);
            retCanvas.drawBitmap(bm, scaleToFit, null);
            mask.recycle();
        return ret;
    }

}


