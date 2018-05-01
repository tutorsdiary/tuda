package com.tuda.teacher.util;

import android.text.TextUtils;

import com.tuda.teacher.network.code.EntryStatus;
import com.tuda.teacher.network.entry.ResponseEntry;

public class Log {

    public static void i(String tag, String message) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(message))
            android.util.Log.i(tag, message);
    }

    public static void d(String tag, String message) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(message))
            android.util.Log.d(tag, message);
    }

    public static void v(String tag, String message) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(message))
            android.util.Log.v(tag, message);
    }

    public static void w(String tag, String message) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(message))
            android.util.Log.w(tag, message);
    }

    public static void e(String tag, String message) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(message))
            android.util.Log.e(tag, message);
    }
    public static String makeErrorMsg(int code) {
        ResponseEntry obj = new ResponseEntry();
        obj.setMessage(String.valueOf(code));
        return makeErrorMsg(obj);
    }

    public static String makeErrorMsg(String msg) {
        ResponseEntry obj = new ResponseEntry();
        obj.setMessage(msg);
        return makeErrorMsg(obj);
    }

    public static String makeErrorMsg(ResponseEntry obj) {
        String errorMsg = "";
        if (obj == null) {
            errorMsg += " | Response Object None";
        } else {
            if (!EntryStatus.SUCCESS.equals(obj.getStatus()))
                errorMsg += " | Response code - " + obj.getCode() + " / message - " + obj.getMessage();
            if (obj.getData() == null)
                errorMsg += " | Response Data None";
        }

        return errorMsg;
    }
}
