package com.tuda.teacher.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.tuda.teacher.type.Constants;

import java.io.File;
import java.util.HashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class GlideManager {
    static private Context mContext;

    // from url
    static public void drawFromURL(Context context, ImageView v, Integer mode, String url, int holderResourceId) {
        DrawableRequestBuilder builder = Glide
                .with(context)
                .load(url)
                .placeholder(holderResourceId)
                .dontAnimate();    // crossFade 와 같은 animation 금지
                //.placeholder(R.drawable.common_student_default)
                //.error(R.drawable.common_student_default)
                //.crossFade()  // default
                //.centerCrop()     // 가운데부터 잘라낸후 보여주기
                //.skipMemoryCache(false)   // default, 메모리캐시 안하기
                //.diskCacheStrategy(DiskCacheStrategy.ALL)     // default, 디스크캐시정책 설정
                //.thumbnail(0.1f);     // 용량 큰 이미지의 경우 10%만 먼저 다운로드 & viewing
        setTransform(context, builder, mode);
        builder.into(v);
    }
    static public void drawFromURL(Context context, ImageView v, Integer mode, String url) {
        DrawableRequestBuilder builder = Glide
                .with(context)
                .load(url)
                .dontAnimate();    // crossFade 와 같은 animation 금지
        //.placeholder(R.drawable.common_student_default)
        //.error(R.drawable.common_student_default)
        //.crossFade()  // default
        //.centerCrop()     // 가운데부터 잘라낸후 보여주기
        //.skipMemoryCache(false)   // default, 메모리캐시 안하기
        //.diskCacheStrategy(DiskCacheStrategy.ALL)     // default, 디스크캐시정책 설정
        //.thumbnail(0.1f);     // 용량 큰 이미지의 경우 10%만 먼저 다운로드 & viewing
        setTransform(context, builder, mode);
        builder.into(v);
    }

    // from url
    static public void drawFromURL(Context context, ImageView v, Integer mode, String url, int holderResourceId, RequestListener listener) {
        DrawableRequestBuilder builder = Glide
                .with(context)
                .load(url)
                .placeholder(holderResourceId)
                .listener(listener)
                .dontAnimate();    // crossFade 와 같은 animation 금지
        //.placeholder(R.drawable.common_student_default)
        //.error(R.drawable.common_student_default)
        //.crossFade()  // default
        //.centerCrop()     // 가운데부터 잘라낸후 보여주기
        //.skipMemoryCache(false)   // default, 메모리캐시 안하기
        //.diskCacheStrategy(DiskCacheStrategy.ALL)     // default, 디스크캐시정책 설정
        //.thumbnail(0.1f);     // 용량 큰 이미지의 경우 10%만 먼저 다운로드 & viewing
        setTransform(context, builder, mode);
        builder.into(v);
    }

    // from file
    static public void drawFromFile(Context context, ImageView v, File file) {
        drawFromFile(context, v, Constants.GLIDE_DEFAULT, file);
    }
    static public void drawFromFile(Context context, ImageView v, Integer mode, File file) {
        DrawableRequestBuilder builder = Glide
                .with(context)
                .load(file)
                .dontAnimate();
        setTransform(context, builder, mode);
        builder.into(v);
    }
    static public void drawFromFileNoCache(Context context, ImageView v, File file, int holderResourceId, boolean dontAnimate) {
        DrawableRequestBuilder builder = Glide
                .with(context)
                .load(file)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(holderResourceId);
        if (dontAnimate)
            builder.dontAnimate();
        setTransform(context, builder, Constants.GLIDE_DEFAULT);
        builder.into(v);
    }

    // from uri
    static public void drawFromURI(Context context, ImageView v, Uri uri) {
        drawFromURI(context, v, Constants.GLIDE_DEFAULT, uri);
    }
    static public void drawFromURI(Context context, ImageView v, Integer mode, Uri uri) {
        DrawableRequestBuilder builder = Glide
                .with(context)
                .load(uri)
                .dontAnimate();
        setTransform(context, builder, mode);
        builder.into(v);
    }

    // from url
    static public void drawFromURL(Context context, ImageView v, String url) {
        drawFromURI(context, v, Constants.GLIDE_DEFAULT, url);
    }
    static public void drawFromURI(Context context, ImageView v, Integer mode, String url) {
        DrawableRequestBuilder builder = Glide
                .with(context)
                .load(url)
                .dontAnimate();
        setTransform(context, builder, mode);
        builder.into(v);
    }

    // from resource
    static public void drawFromResource(Context context, ImageView v, int resourceId) {
        drawFromResource(context, v, Constants.GLIDE_DEFAULT, resourceId, false);
    }

    static public void drawFromResource(Context context, ImageView v, int resourceId, boolean isFade) {
        drawFromResource(context, v, Constants.GLIDE_DEFAULT, resourceId, isFade);
    }

    static public void drawFromResource(Context context, ImageView v, Integer mode, int resourceId) {
        drawFromResource(context, v, mode, resourceId, false);
    }

    static public void drawFromResource(Context context, ImageView v, Integer mode, int resourceId, boolean isFade) {
        DrawableRequestBuilder builder = Glide
                .with(context)
                .load(resourceId);

        if (!isFade)
            builder.dontAnimate();
        else
            builder.skipMemoryCache(true)
                   .diskCacheStrategy(DiskCacheStrategy.NONE);

        setTransform(context, builder, mode);
        builder.into(v);
    }

    // view transform (circle, round corner)
    static private void setTransform(Context context, DrawableRequestBuilder builder, Integer mode) {
        if (mode == Constants.GLIDE_ROUND)
            builder.bitmapTransform(new RoundedCornersTransformation(context, 5, 0));
        else if (mode == Constants.GLIDE_MULTI) {
            Transformation t = new MultiTransformation(new CenterCrop(context), new RoundedCornersTransformation(context, DPManager.dp2px(context, 3f), 0, RoundedCornersTransformation.CornerType.TOP));
            builder.bitmapTransform(t);
        } else if (mode == Constants.GLIDE_CENTER_CROP) {
            builder.bitmapTransform(new CenterCrop(context));
        } else if (mode == Constants.GLIDE_CIRCLE)
            builder.bitmapTransform(new CropCircleTransformation(context));
    }

    // clear all cache
    static public void clearAllCache(Context context) {
        mContext = context;

        new clearTask().execute("");
    }

    static private class clearTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... token) {
            Glide.get(mContext).clearMemory();
            Glide.get(mContext).clearDiskCache();

            return null;
        }

        protected void onPostExecute(HashMap<String, String> userInfo) {
        }
    }
}
