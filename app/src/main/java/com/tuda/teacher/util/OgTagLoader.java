package com.tuda.teacher.util;

import android.text.TextUtils;

import com.tuda.teacher.BuildConfig;
import com.tuda.teacher.common.TudaCache;
import com.tuda.teacher.type.HashType;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by crazyluv on 2017. 1. 22..
 */

public class OgTagLoader<T> implements Callback {

    private OkHttpClient.Builder client = new OkHttpClient.Builder();

    private final static Pattern metaCharset = Pattern.compile("charset\\s*?=\\s*?['\"]?([^>'\"\\s;]+)['\"]?");
    private T token;
    private OgTags ret = new OgTags();

    public OgTagLoader() {
    }

    private OgTagCallback myCallback = null;
    public void getTag(String url, T token, OgTagCallback callback) {

        this.token = token;

        getTag(url, callback);
    }

    public void getTag(String url, OgTagCallback callback) {

        this.myCallback = callback;
        ret.hash = CryptoManager.getHash(url, HashType.MD5);

        if (TextUtils.isEmpty(url))
            return;

        if (!url.startsWith("http"))
            url = "http://"+url;

        OgTagLoader.OgTags ogTags = (OgTagLoader.OgTags) TudaCache.get("ogTags::"+ret.hash);
        if (ogTags != null) {
            if (myCallback != null) {
                myCallback.onResponse(ogTags, token);
                return;
            }
        }

        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().maxAge(24*7, TimeUnit.HOURS).build())
                .url(url)
                .build();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            client.addInterceptor(logging);
        }

        client.build().newCall(request).enqueue(this);

    }

    @Override
    public void onFailure(Call call, final IOException e) {

        ret.site = call.request().url().host();

        if (myCallback != null) {
            myCallback.onFailure(ret, token, e);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        ret.site = call.request().url().host();

        byte[] body = response.body().bytes();

        String charSet = "UTF-8";

        // header와 html문서에서 charset을 찾아 encoding하여 처리한다
        Matcher matcher = metaCharset.matcher(response.header("Content-Type").toString());

        if (!matcher.find()) {
            matcher = metaCharset.matcher(new String(body));
            if (matcher.find()) {
                charSet = matcher.group(1);
            }
        } else {
            charSet = matcher.group(1);
        }

        Document doc = Jsoup.parse(new String(body, charSet));

        Elements ogTags = doc.select("meta[property^=og:]");
        if (ogTags.size() <= 0) {
            return;
        }

        // 필요한 OGTag를 추려낸다
        for (int i = 0; i < ogTags.size(); i++) {
            Element tag = ogTags.get(i);

            String text = tag.attr("property");

            switch (text) {
                case "og:url" :
                    ret.url = tag.attr("content");
                    break;
                case "og:image" :
                    ret.image = tag.attr("content");
                    break;
                case "og:description" :
                    ret.desc = tag.attr("content");
                    break;
                case "og:title" :
                    ret.title = tag.attr("content");
                    break;
            }
        }

        TudaCache.set("ogTags::"+ret.hash, ret, new DateTime().plusDays(2));

        if (myCallback != null) {
            myCallback.onResponse(ret, token);
        }
    }

    public static class OgTags<T> {
        public String hash;
        public String url;
        public String site;
        public String image;
        public String title;
        public String desc;
    }

    public interface OgTagCallback<T> {
        void onResponse(OgTags ogTags, T token);
        void onFailure(OgTags ogTags, T token, IOException e);
    }

}
