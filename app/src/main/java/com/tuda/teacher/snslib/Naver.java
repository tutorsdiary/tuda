package com.tuda.teacher.snslib;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.tuda.teacher.R;
import com.tuda.teacher.type.AuthType;
import com.tuda.teacher.type.Constants;
import com.tuda.teacher.type.Gender;
import com.tuda.teacher.type.SnsParam;
import com.tuda.teacher.util.DataManager;
import com.tuda.teacher.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.HashMap;

public class Naver extends BaseSNS {

    private String TAG = "Naver";
    private OAuthLogin mOAuthLoginModule;
    private String mMode;

    public Naver(Context context) {
        super(context);

        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                context
                , context.getApplicationContext().getResources().getString(R.string.naver_client_id)
                , context.getApplicationContext().getResources().getString(R.string.naver_client_secret)
                , context.getApplicationContext().getResources().getString(R.string.naver_client_name)
        );
    }

    public OAuthLogin getOAuthLoginModule() {
        return mOAuthLoginModule;
    }
    public OAuthLoginHandler getOAuthLoginHandler() {
        return mOAuthLoginHandler;
    }

    public String getAccessToken() {
        return getOAuthLoginModule().getAccessToken(mContext);
    }

    public boolean isLogin() {
        return getAccessToken() != null;
    }

    public void logout() {
        mOAuthLoginModule.logout(mContext);
    }

    public void regist() {
        regist("regist");
    }
    public void regist(String mode) {
        this.mMode = mode;
        mOAuthLoginModule.startOauthLoginActivity((Activity)mContext, mOAuthLoginHandler);
    }

    public void login() {
        new refreshTokenTask().execute();
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                String tokenType = mOAuthLoginModule.getTokenType(mContext);

                /*Log.e(TAG, "naver] accessToken-" + accessToken + " / refreshToken-" + refreshToken + " / expireAt-" + expiresAt + " / tokenType-" + tokenType);*/

                new loginApiTask().execute(mMode, accessToken);
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);

                Log.e(TAG, "errorCode-" + errorCode + " / errorDesc-" + errorDesc);
            }
        };
    };

    private class refreshTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return mOAuthLoginModule.getInstance().refreshAccessToken(mContext);
        }

        protected void onPostExecute(String accessToken) {
            new loginApiTask().execute("login", accessToken);
        }
    }

    private class loginApiTask extends AsyncTask<String, Void, HashMap<String, String>> {
        protected HashMap<String, String> doInBackground(String... token) {
            HashMap<String, String> userInfo = parseXML(token[0], token[1]);

            return userInfo;
        }

        protected void onPostExecute(HashMap<String, String> userInfo) {
            if ("00".equals(userInfo.get("resultcode"))) {
                String[] arrAge = userInfo.get("age").split("-");
                String birthdayYear = "";
                try {
                    birthdayYear = DataManager.getYearByAge(Integer.valueOf(arrAge[0])).toString();
                } catch (NumberFormatException e) {
                }
                Gender gender = ("M".equals(userInfo.get("gender"))) ? Gender.MALE : Gender.FEMALE;

                SnsParam snsParam = new SnsParam();
                snsParam.mode = userInfo.get("mode");
                snsParam.authType = AuthType.NAVER;
                snsParam.authKey = userInfo.get("authKey");
                snsParam.email = userInfo.get("email");
                snsParam.gender = gender;
                snsParam.birthday = birthdayYear + "-" + userInfo.get("birthday");

                if ("regist".equals(userInfo.get("mode"))) {
                    checkMember(snsParam);
                } else if ("login".equals(userInfo.get("mode"))){
                    directLogin(snsParam);
                } else if ("config".equals(userInfo.get("mode"))) {
                    updateMember(snsParam);
                }
            } else {
                SnsParam snsParam = new SnsParam();
                snsParam.mode = userInfo.get("mode");
                snsParam.authType = AuthType.NAVER;
                snsParam.errorType = Constants.ERROR_NETWORK;
                loginError(snsParam);
            }
        }
    }

    private HashMap<String, String> parseXML(String mode, String accessToken) {
        HashMap<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("mode", mode);

        try {
            String resultXML = mOAuthLoginModule.requestApi(mContext, accessToken, "https://openapi.naver.com/v1/nid/getUserProfile.xml");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(resultXML));
            int eventType = parser.getEventType();
            String tag = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        if ("resultcode".equals(tagName))
                            tag = "resultcode";
                        else if ("id".equals(tagName))
                            tag = "id";
                        else if ("email".equals(tagName))
                            tag = "email";
                        else if ("age".equals(tagName))
                            tag = "age";
                        else if ("gender".equals(tagName))
                            tag = "gender";
                        else if ("birthday".equals(tagName))
                            tag = "birthday";
                        else
                            tag = "";
                        break;
                    case XmlPullParser.TEXT:
                        if ("resultcode".equals(tag))
                            userInfo.put("resultcode", parser.getText());
                        else if ("id".equals(tag))
                            userInfo.put("authKey", parser.getText());
                        else if ("email".equals(tag))
                            userInfo.put("email", parser.getText());
                        else if ("age".equals(tag))
                            userInfo.put("age", parser.getText());
                        else if ("gender".equals(tag))
                            userInfo.put("gender", parser.getText());
                        else if ("birthday".equals(tag))
                            userInfo.put("birthday", parser.getText());
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            userInfo.put("resultcode", "-1");
        }

        return userInfo;
    }
}
