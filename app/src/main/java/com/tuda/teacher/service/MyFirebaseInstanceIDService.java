package com.tuda.teacher.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tuda.teacher.common.Preference;
import com.tuda.teacher.util.Log;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseInstanceIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + token);

        // 생성등록된 토큰을 Preference, Database 에 저장한다
        Preference.setPushToken(token, Preference.getAlarmUseService());
    }
}
