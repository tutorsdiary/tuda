package com.tuda.teacher.snslib;

import android.content.Context;
import android.text.TextUtils;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.tuda.teacher.common.Preference;
import com.tuda.teacher.type.AuthType;
import com.tuda.teacher.type.Constants;
import com.tuda.teacher.type.SnsParam;
import com.tuda.teacher.ui.activity.LoginActivity;
import com.tuda.teacher.util.Log;

public class Kakao extends BaseSNS {

    private String TAG = "Kakao";

    private SessionCallback callback = null;
    private String mMode;

    public Kakao(Context context) {
        super(context);

        if (callback == null) {
            callback = new SessionCallback();
            Session.getCurrentSession().addCallback(callback);
        }
        mMode = null;
    }

    public String getAccessToken() {
        return Session.getCurrentSession().getAccessToken();
    }

    public void removeCallback() {
        Session.getCurrentSession().removeCallback(callback);
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(getAccessToken());
    }

    public void logout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.e(TAG, "Kakao logout success");
            }
        });
    }

    public void setMode(String mode) {
        this.mMode = mode;
    }

    public void login() {
        loginApiTask("login");
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.e(TAG, "kakao connect ok");

            loginApiTask(null);
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e(TAG, "kakao connect fail");
            if(exception != null) {
                Log.e(TAG, "kakao onSessionOpenFailed / msg-" + exception);
            }

            if (mContext instanceof LoginActivity)
                ((LoginActivity) mContext).dataErrorForKakaoException();
        }
    }

    private void loginApiTask(String from) {
        if (!TextUtils.isEmpty(mMode)) {
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e(TAG, "kakao requestMe onFailure / msg-" + errorResult);

                    SnsParam snsParam = new SnsParam();
                    snsParam.mode = mMode;
                    snsParam.authType = AuthType.KAKAO;
                    snsParam.errorType = Constants.ERROR_DATA;
                    //loginError(snsParam);
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e(TAG, "kakao requestMe onSessionClosed / msg-" + errorResult);

                    SnsParam snsParam = new SnsParam();
                    snsParam.mode = mMode;
                    snsParam.authType = AuthType.KAKAO;
                    snsParam.errorType = Constants.ERROR_DATA;
                    //loginError(snsParam);
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    Log.e(TAG, "kakao requestMe onSuccess / mode-" + mMode + " / profile-" + userProfile + " / id-" + userProfile.getId());

                    SnsParam snsParam = new SnsParam();
                    snsParam.mode = mMode;
                    snsParam.authType = AuthType.KAKAO;
                    snsParam.authKey = String.valueOf(userProfile.getId());
                    snsParam.email = Preference.getLoginEmail();
                    snsParam.gender = null;
                    snsParam.birthday = null;

                    if ("regist".equals(mMode)) {
                        checkMember(snsParam);
                    } else if ("login".equals(mMode)) {
                        directLogin(snsParam);
                    } else if ("config".equals(mMode)) {
                        updateMember(snsParam);
                    }
                }

                @Override
                public void onNotSignedUp() {
                    Log.e(TAG, "kakao requestMe onNotSignedUp");

                    SnsParam snsParam = new SnsParam();
                    snsParam.mode = mMode;
                    snsParam.authType = AuthType.KAKAO;
                    snsParam.errorType = Constants.ERROR_DATA;
                    //loginError(snsParam);
                }
            });
        }
    }
}