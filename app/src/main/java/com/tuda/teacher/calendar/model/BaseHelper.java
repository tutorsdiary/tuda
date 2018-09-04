package com.tuda.teacher.calendar.model;

import android.content.Context;

import com.tuda.teacher.calendar.AsyncQueryService;

/**
 * Created by crazyluv on 2016. 1. 25..
 */
public class BaseHelper {
    public final String TAG = getClass().getSimpleName();
    protected Context mContext;

    protected AsyncQueryService mService;

    public BaseHelper(Context context) {
        mContext = context;
        mService = getAsyncQueryService();
    }

    public synchronized AsyncQueryService getAsyncQueryService() {
        if (mService == null) {
            mService = new AsyncQueryService(mContext);
        }
        return mService;
    }
}
