package com.tuda.teacher.network.interceptor

import android.text.TextUtils
import com.tuda.teacher.common.Preference
import com.tuda.teacher.type.HashType
import com.tuda.teacher.util.CryptoManager
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.Request

class RequestHeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {

        var request:Request.Builder = chain?.request()!!.newBuilder()
        var token:String = Preference.getLoginToken()
        var appInfo:String = "{\"version\":\"" + Preference.getAppVersion() + "\",\"platform\":\"ad\"}"
        var encryptUUID:String = CryptoManager.getHash(Preference.getDeviceUUID(), HashType.SHA256)
        var userAgent:String = System.getProperty("http.agent")

        if (!TextUtils.isEmpty(token))
            request.addHeader("X-TudaAuth-Token", token);
        if (!TextUtils.isEmpty(encryptUUID))
            request.addHeader("X-TudaAuth-UUID", encryptUUID);
        if (!TextUtils.isEmpty(appInfo))
            request.addHeader("X-Tuda-AppInfo", appInfo);
        if (!TextUtils.isEmpty(userAgent))
            request.addHeader("User-Agent", userAgent);


        return chain!!.proceed(request.build())
    }
}