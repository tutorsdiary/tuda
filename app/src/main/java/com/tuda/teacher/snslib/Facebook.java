package com.tuda.teacher.snslib;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.tuda.teacher.type.AuthType;
import com.tuda.teacher.type.Constants;
import com.tuda.teacher.type.Gender;
import com.tuda.teacher.type.SnsParam;
import com.tuda.teacher.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Facebook extends BaseSNS {

    private String TAG = "Facebook";
    private CallbackManager mCallbackManager;

    public Facebook(Context context) {
        super(context);

        if (!FacebookSdk.isInitialized())
            FacebookSdk.sdkInitialize(context.getApplicationContext());
        this.mContext = context;
        this.mCallbackManager = CallbackManager.Factory.create();
    }

    public CallbackManager getCallbackManager() {
        return mCallbackManager;
    }

    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    public boolean isLogin() {
        return getAccessToken() != null;
    }

    public void logout() {
        LoginManager.getInstance().logOut();
    }

    public void regist() {
        regist("regist");
    }
    public void regist(final String mode) {
        LoginManager.getInstance().logInWithReadPermissions((Activity)mContext, Arrays.asList("public_profile", "email", "user_birthday"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "facebook regist success");

                loginApiTask(mode);
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "facebook regist cancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e(TAG, "facebook regist error");
            }
        });
    }

    public void login() {
        loginApiTask("login");
    }

    private void loginApiTask(final String mode) {
        GraphRequest request = GraphRequest.newMeRequest(getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // 개인정보 수신
                        String authKey = null;
                        String name = null;
                        String email = null;
                        String gender = null;
                        String birthday = null;
                        String[] birth = new String[2];
                        Gender gender1 = null;

                        try {
                            if (response.getError() == null) {
                                JSONObject obj = response.getJSONObject();
                                if (obj != null) {

                                    try {
                                        authKey = obj.getString("id");
                                    } catch (Exception e) {}
                                    try {
                                        name = obj.getString("name");
                                    } catch (Exception e) {}
                                    try {
                                        email = obj.getString("email");
                                    } catch (Exception e) {}
                                    try {
                                        gender = obj.getString("gender");
                                    } catch (Exception e) {}
                                    try {
                                        birthday = obj.getString("birthday");
                                    } catch (Exception e) {}
                                }

                                //Log.e(TAG, "facebook] authKey-" + authKey + " / name-" + name + " / email-" + email + " / gender-" + gender + " / birthday-" + birthday);

                                if (!TextUtils.isEmpty(birthday))
                                    birth = birthday.split("/");
                                if (!TextUtils.isEmpty(gender))
                                    gender1 = ("MALE".equals(gender.toUpperCase())) ? Gender.MALE : Gender.FEMALE;

                                SnsParam snsParam = new SnsParam();
                                snsParam.mode = mode;
                                snsParam.authType = AuthType.FACEBOOK;
                                snsParam.authKey = authKey;
                                snsParam.email = email;
                                snsParam.gender = (gender1 != null) ? gender1 : null;
                                snsParam.birthday = (!TextUtils.isEmpty(birth[0])) ? birth[2] + "-" + birth[0] + "-" + birth[1] : null;

                                if ("regist".equals(mode)) {
                                    checkMember(snsParam);
                                } else if ("login".equals(mode)){
                                    directLogin(snsParam);
                                } else if ("config".equals(mode)) {
                                    updateMember(snsParam);
                                }
                            } else {
                                Log.e(TAG, "facebook async " + mode + " fail");

                                SnsParam snsParam = new SnsParam();
                                snsParam.mode = mode;
                                snsParam.authType = AuthType.FACEBOOK;
                                snsParam.errorType = Constants.ERROR_DATA;
                                loginError(snsParam);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "facebook async " + mode + " error");

                            SnsParam snsParam = new SnsParam();
                            snsParam.mode = mode;
                            snsParam.authType = AuthType.FACEBOOK;
                            snsParam.errorType = Constants.ERROR_NETWORK;
                            loginError(snsParam);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }
}