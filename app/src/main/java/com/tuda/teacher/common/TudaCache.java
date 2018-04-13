package com.tuda.teacher.common;

import android.support.v4.util.LruCache;

import org.joda.time.DateTime;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;

/**
 * Created by crazyluv on 2017. 1. 12..
 */

public class TudaCache {

    private static LruCache<String, TudaCacheData> cacheData = new LruCache(1024);

//    static {
//        new Timer(true).schedule(new TimerTask() {
//            @Override
//            public void run() {
//                for (Map.Entry<String, TudaCacheData> cache : cacheData.entrySet()) {
//                    if (cache.getValue().expire != null
//                            && cache.getValue().expire.isBeforeNow())
//                        cacheData.remove(cache);
//                }
//            }
//        }, 0, 1000*60*10);
//    }

    public static void set(String keyName, Object data) {
        set(keyName, data, null);
    }

    public static void set(String keyName, Object data, DateTime expire) {
        expireData(keyName);
        cacheData.put(keyName, new TudaCacheData(data, expire));
    }

    public static Object get(String keyName) {
        expireData(keyName);
        TudaCacheData cache = cacheData.get(keyName);

        if (cache != null)
            return cache.data;

        return null;
    }

    private static void expireData(String keyName) {
        TudaCacheData cache = cacheData.get(keyName);

        if (cache != null) {
            if (cache.expire != null
                    && cache.expire.isBeforeNow())
                cacheData.remove(keyName);
        }
    }

    public static class TudaCacheData  {
        public Object data;
        public DateTime expire;

        public TudaCacheData(Object data, DateTime expire) {
            if (expire == null || expire.isBeforeNow())
                expire = new DateTime().plusMinutes(10);
            this.data = data;
            this.expire = expire;
        }
    }
}
