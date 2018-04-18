package com.tuda.teacher.snslib;

import android.content.Context;

import com.tuda.teacher.TudaApp;
import com.tuda.teacher.common.GACommon;
import com.tuda.teacher.type.AuthType;
import com.tuda.teacher.type.Constants;
import com.tuda.teacher.network.code.EntryStatus;
import com.tuda.teacher.network.entity.member.MemberEntity;
import com.tuda.teacher.network.entity.member.MemberInfo;
import com.tuda.teacher.network.entry.RequestEntry;
import com.tuda.teacher.network.entry.ResponseEntry;
import com.tuda.teacher.type.SignIn;
import com.tuda.teacher.type.SnsParam;
import com.tuda.teacher.ui.activity.ConfigMainActivity;
import com.tuda.teacher.ui.activity.LoginActivity;
import com.tuda.teacher.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseSNS {

    private String TAG = "BaseSNS";
    public Context mContext;

    public BaseSNS(Context context) {
        this.mContext = context;
    }

    protected void checkMember(final SnsParam snsParam) {
        SignIn signIn = new SignIn();
        signIn.setAuthKey(snsParam.authKey);
        signIn.setAuthType(snsParam.authType);

        RequestEntry<SignIn> requestEntry = new RequestEntry<SignIn>(signIn);
        TudaApp.api.checkAuthKey(requestEntry).enqueue(new Callback<ResponseEntry>() {
            @Override
            public void onResponse(Call<ResponseEntry> call, Response<ResponseEntry> response) {
                if (response.isSuccessful()) {
                    if (EntryStatus.SUCCESS.equals(response.body().getStatus())) {
                        Log.e(TAG, snsParam.authType.getValue() + "] signCheck Success...");

                        ((LoginActivity) mContext).registOk(true, snsParam);
                    } else {
                        Log.e(TAG, snsParam.authType.getValue() + "] signCheck Fail... : " + Log.makeErrorMsg(response.body()));

                        snsParam.errorType = Constants.ERROR_DUPLICATE_MEMBER;
                        loginError(snsParam);
                    }

                } else {
                    Log.e(TAG, snsParam.authType.getValue() + "] signCheck Fail... : " + Log.makeErrorMsg(response.code()));

                    snsParam.errorType = Constants.ERROR_DUPLICATE_MEMBER;
                    loginError(snsParam);

                }

            }

            @Override
            public void onFailure(Call<ResponseEntry> call, Throwable t) {
                Log.e(TAG, snsParam.authType.getValue() + "] signCheck Error... : " + t.getMessage());

                // 회원가입 실패 처리
                snsParam.errorType = Constants.ERROR_NETWORK;
                loginError(snsParam);

            }

        });
    }

    protected void directLogin(final SnsParam snsParam) {
        SignIn signIn = new SignIn();
        signIn.setAuthKey(snsParam.authKey);
        signIn.setAuthType(snsParam.authType);

        RequestEntry<SignIn> requestEntry = new RequestEntry<SignIn>(signIn);
        TudaApp.api.signIn(requestEntry).enqueue(new Callback<ResponseEntry<String>>() {
            @Override
            public void onResponse(Call<ResponseEntry<String>> call, Response<ResponseEntry<String>> response) {
                if (response.isSuccessful()) {
                    if (EntryStatus.SUCCESS.equals(response.body().getStatus()) && response.body().getData() != null) {
                        Log.e(TAG, snsParam.authType.getValue() + "] signIn Success...");

                        String token = response.body().getData();
                        snsParam.token = token;

                        // 로그인 성공 처리
                        ((LoginActivity) mContext).loginOk(true, snsParam);

                        TudaApp.gaManager.eventTracking(GACommon.eventCodeMap.get("CA"), "user", "Login.sns_success");
                    } else {
                        Log.e(TAG, snsParam.authType.getValue() + "] signIn Fail... : " + Log.makeErrorMsg(response.body()));

                        // 로그인이 안됐다는건 해당 SNS 로 가입이 되지 않은 회원이라는 의미
                        snsParam.errorType = Constants.ERROR_NO_MEMBER;
                        loginError(snsParam);

                        TudaApp.gaManager.eventTracking(GACommon.eventCodeMap.get("CA"), "user", "Login.sns_fail");
                    }

                } else {
                    Log.e(TAG, snsParam.authType.getValue() + "] signIn Fail... : " + Log.makeErrorMsg(response.code()));

                    // 로그인이 안됐다는건 해당 SNS 로 가입이 되지 않은 회원이라는 의미
                    snsParam.errorType = Constants.ERROR_NO_MEMBER;
                    loginError(snsParam);

                    TudaApp.gaManager.eventTracking(GACommon.eventCodeMap.get("CA"), "user", "Login.sns_fail");

                }

            }

            @Override
            public void onFailure(Call<ResponseEntry<String>> call, Throwable t) {
                Log.e(TAG, snsParam.authType.getValue() + "] signIn Error... : " + t.getMessage());

                snsParam.errorType = Constants.ERROR_DATA;
                loginError(snsParam);

                TudaApp.gaManager.eventTracking(GACommon.eventCodeMap.get("CA"), "user", "Login.sns_error");

            }
        });
    }

    protected void updateMember(final SnsParam snsParam) {
        MemberInfo requestItem = new MemberInfo();

        if (AuthType.FACEBOOK.equals(snsParam.authType)) {
            requestItem.setFacebookAuthKey(snsParam.authKey);
        } else if (AuthType.NAVER.equals(snsParam.authType)) {
            requestItem.setNaverAuthKey(snsParam.authKey);
        } else if (AuthType.KAKAO.equals(snsParam.authType)) {
            requestItem.setKakaoAuthKey(snsParam.authKey);
        }

        // 서버에 저장
        RequestEntry<MemberInfo> requestEntry = new RequestEntry<MemberInfo>(requestItem);
        TudaApp.api.updateMemberInfo(requestEntry).enqueue(new Callback<ResponseEntry<MemberEntity>>() {
            @Override
            public void onResponse(Call<ResponseEntry<MemberEntity>> call, Response<ResponseEntry<MemberEntity>> response) {
                if (response.isSuccessful()) {
                    if (EntryStatus.SUCCESS.equals(response.body().getStatus()) && response.body().getData() != null) {
                        Log.e(TAG, "updateMemberInfo Success...");

                        ((ConfigMainActivity) mContext).loginOk(true, snsParam);
                    } else {
                        Log.e(TAG, "updateMemberInfo Fail... : " + Log.makeErrorMsg(response.body()));

                        ((ConfigMainActivity) mContext).loginOk(false, snsParam, response.body().getCode());
                    }

                } else {
                    Log.e(TAG, "updateMemberInfo Fail... : " + Log.makeErrorMsg(response.code()));

                    ((ConfigMainActivity) mContext).loginOk(false, snsParam, response.message());

                }

            }

            @Override
            public void onFailure(Call<ResponseEntry<MemberEntity>> call, Throwable t) {

                Log.e(TAG, "updateMemberInfo Fail... : " + t.getMessage());
            }

        });
    }

    protected void loginError(SnsParam snsParam) {
        if ("regist".equals(snsParam.mode))
            ((LoginActivity) mContext).registOk(false, snsParam);
        else if ("config".equals(snsParam.mode))
            ((ConfigMainActivity) mContext).loginOk(false, snsParam);
        else if ("login".equals(snsParam.mode))
            ((LoginActivity) mContext).loginOk(false, snsParam);
    }
}