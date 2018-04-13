package com.tuda.teacher.common;

import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuda.teacher.BuildConfig;
import com.tuda.teacher.R;
import com.tuda.teacher.TudaApp;
import com.tuda.teacher.network.entity.weather.WeatherLocationMappingForm;
import com.tuda.teacher.type.AuthType;
import com.tuda.teacher.type.ConfigInfo;
import com.tuda.teacher.network.code.EntryStatus;
import com.tuda.teacher.type.Gender;
import com.tuda.teacher.type.PushEntity;
import com.tuda.teacher.network.entry.RequestEntry;
import com.tuda.teacher.network.entry.ResponseEntry;
import com.tuda.teacher.type.UserType;
import com.tuda.teacher.util.DataManager;
import com.tuda.teacher.util.FormatManager;
import com.tuda.teacher.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class Preference {

    private static final String TAG = "Preference";
    private static SharedPreferences sharedPref;

    private static final String PREF_APP_VERSION = "app_version";
    private static final String PREF_APP_VERSION_INT = "app_version_int";
    private static final String PREF_HOST_ADDR = "host_addr";
    private static final String PREF_IMG_HOST_ADDR = "img_host_addr";
    private static final String PREF_CONFIG_INIT = "config_init";
    private static final String PREF_CONFIG_AUTO_LOGIN = "config_auto_login";
    private static final String PREF_CONFIG_AUTO_LOCK = "config_auto_lock";
    private static final String PREF_CONFIG_ALARM_USE_SERVICE = "config_alarm_use_service";
    private static final String PREF_CONFIG_ALARM_USE_TALK = "config_alarm_use_talk";
    private static final String PREF_CONFIG_ALARM_USE_NOTE = "config_alarm_use_note";
    private static final String PREF_CONFIG_ALARM_USE_SNOOZE = "config_alarm_use_snooze";
    private static final String PREF_CONFIG_SNOOZE_START_TIME = "config_snooze_start_time";
    private static final String PREF_CONFIG_SNOOZE_END_TIME = "config_snooze_end_time";
    private static final String PREF_CONFIG_ALARM_USE_LESSON = "config_alarm_use_lesson";
    private static final String PREF_CONFIG_ALARM_TIME = "config_alarm_time";
    private static final String PREF_CONFIG_ALARM_SOUND = "config_alarm_sound";
    private static final String PREF_CONFIG_ALARM_VIBRATION = "config_alarm_vibration";
    private static final String PREF_CONFIG_AUTOLOCK_PASSWD = "config_autolock_passwd";
    private static final String PREF_UUID = "pref_uuid";
    private static final String PREF_NICKNAME = "pref_nickname";
    private static final String PREF_ADDRESS = "pref_address";
    private static final String PREF_LOGIN_TOKEN = "login_token";
    private static final String PREF_LOGIN_AUTHKEY = "login_authkey";
    private static final String PREF_LOGIN_AUTHTYPE = "login_authtype";
    private static final String PREF_LOGIN_EMAIL = "login_email";
    private static final String PREF_LOGIN_PASSWORD = "login_password";
    private static final String PREF_LOGIN_MEMBERSRL = "login_membersrl";
    private static final String PREF_LOGIN_GROUPSRL = "login_groupsrl";
    private static final String PREF_LOGIN_USERTYPE = "login_usertype";
    private static final String PREF_LOGIN_GENDER = "login_gender";
    private static final String PREF_LOGIN_IDENTIFICATION_OK = "login_identification_ok";
    private static final String PREF_MEMBERINFO_OK = "memberinfo_ok";
    private static final String PREF_FIRST_RUN_AFTER_INSTALL = "first_run_after_install";
    private static final String PREF_IS_APP_RUNNING = "is_app_running";
    private static final String PREF_STUDENT_LIST_SORT = "student_list_sort";
    private static final String PREF_LAST_LESSON_CHECKED_DATE = "last_lesson_checked_date";
    private static final String PREF_LESSON_CHECKED = "lesson_checked";
    private static final String PREF_SCHOOL_NAME_LIST = "school_name_list";
    private static final String PREF_BOOK_NAME_LIST = "book_name_list";
    private static final String PREF_PUSH_TOKEN = "push_token";
    private static final String PREF_LAST_UPDATE_NOTI = "last_update_noti";
    private static final String PREF_LAST_ADV_VIEW = "last_adv_view";
    private static final String PREF_SNS_FACEBOOK = "sns_facebook";
    private static final String PREF_SNS_NAVER = "sns_naver";
    private static final String PREF_SNS_KAKAO = "sns_kakao";
    private static final String PREF_VIEW_INTRO = "view_intro";
    private static final String PREF_VIEW_EVENT = "view_event";
    private static final String PREF_CONFIG_MARKET_AGREE = "config_market_agree";
    private static final String PREF_CS_NOTICE_VIEW = "cs_notice_view";
    private static final String PREF_IS_LASTMENU_VIEW = "is_lastmenu_view";
    private static final String PREF_LASTMENU_GNB = "lastmenu_gnb";
    private static final String PREF_LASTMENU_TALK = "lastmenu_talk";
    private static final String PREF_LASTMENU_MARKET = "lastmenu_market";
    private static final String PREF_MAIN_NOTICE_VIEW_REVIEW = "main_notice_view_review";
    private static final String PREF_TALK_NICKGENDER = "talk_nickgender";
    private static final String PREF_PHONE_NOTICE_VIEW = "phone_notice_view";
    private static final String PREF_OPEN_MENUSCREEN = "open_menuscreen";
    private static final String PREF_MARKET_INTRO = "market_intro";
    private static final String PREF_TALK_NOTICE_VIEW = "talk_notice_view";
    private static final String PREF_PROFILE_WRITE_CHECK = "pref.profile_write_check";
    private static final String PREF_TALK_CATEGORY_VIEW_FOR_TALK = "talk_category_view_for_talk";
    private static final String PREF_CALENDAR_GUIDE = "calendar_guide";
    private static final String PREF_CALENDAR_VISIBLE_INFO = "calendar.visible.info";
    private static final String PREF_MAIN_TUTORIAL = "main_tutorial";
    private static final String PREF_GNB_SWIPE = "gnb_swipe";
    private static final String PREF_AUTH_MAIL_DIALOG_VIEW = "auth_mail_dialog_view";

    private static final String PREF_CALENDAR_DEFAULT_SYNC_CALENDAR_ID = "default_sync_calendar_id";
    private static final String PREF_CALENDAR_MIGRATION_STATUS = "calendar_migration_status";

    private static final String PREF_WEATHER_DEFAULT_LOCATION = "weather_default_location";
    private static final String PREF_FINE_DUST = "fine_dust";
    private static final String PREF_POINT_COLOR = "point_color";

    public static final String KEY_ALERTS_CATEGORY = "preferences_alerts_category";
    public static final String KEY_ALERTS = "preferences_alerts";
    public static final String KEY_ALERTS_VIBRATE = "preferences_alerts_vibrate";
    public static final String KEY_ALERTS_RINGTONE = "preferences_alerts_ringtone";
    public static final String KEY_ALERTS_POPUP = "preferences_alerts_popup";

    public static SharedPreferences getSharedPreferences() {
        return sharedPref;
    }

    public static void setupDefaults(SharedPreferences prefs) {
        sharedPref = prefs;
    }

    public static void wipeOutAllData() {
        sharedPref.edit().clear().commit();

    }

    public static void setAppVersion(String appVersion) {
        sharedPref.edit().putString(PREF_APP_VERSION, appVersion).commit();
    }

    public static String getAppVersion() {
        return sharedPref.getString(PREF_APP_VERSION, "1.0.0");
    }

    public static void setAppVersionInt(int version) {
        sharedPref.edit().putInt(PREF_APP_VERSION_INT, version).commit();
    }

    public static int getAppVersionInt() {
        return sharedPref.getInt(PREF_APP_VERSION_INT, 0);
    }

    public static void setApiURL(String api_url) {
        sharedPref.edit().putString(PREF_HOST_ADDR, api_url).commit();
    }

    public static String getApiURL() {
        return sharedPref.getString(PREF_HOST_ADDR, BuildConfig.DEFAULT_HOST_ADDR);
    }

    public static void setImgHostAddr(String img_domain) {
        sharedPref.edit().putString(PREF_IMG_HOST_ADDR, img_domain).commit();
    }

    public static String getImgHostAddr() {
        return sharedPref.getString(PREF_IMG_HOST_ADDR, BuildConfig.DEFAULT_IMG_HOST_ADDR);
    }

    public static void initApiUrlImgHostAddr() {
        sharedPref.edit().remove(PREF_HOST_ADDR).commit();
        sharedPref.edit().remove(PREF_IMG_HOST_ADDR).commit();
    }

    public static void setConfigInfo(ConfigInfo configInfo) {
        RequestEntry<ConfigInfo> requestEntry = new RequestEntry<ConfigInfo>(configInfo);
        Call<ResponseEntry> call = TudaApp.api.configUp(requestEntry);
        call.enqueue(new Callback<ResponseEntry>() {

            @Override
            public void onResponse(Call<ResponseEntry> request, Response<ResponseEntry> response) {
                if (response.isSuccessful()) {
                    if (EntryStatus.SUCCESS.equals(response.body().getStatus())) {
                        Log.e(TAG, "configUp Success...");
                    } else {
                        Log.e(TAG, "configUp Fail... : " + Log.makeErrorMsg(response.body()));
                    }
                } else {
                    Log.e(TAG, "configUp Fail... : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseEntry> call, Throwable t) {
                Log.e(TAG, "configUp Error... : " + t.getMessage());
            }

        });
    }

    public static void initConfigInfo() {
        // 기본 환경설정 Preference 저장
        Preference.setAutoLogin(true);
        Preference.setAutoLock(false);
        Preference.setAlarmUseService(true);
        Preference.setAlarmUseTalk(true);
        Preference.setAlarmUseNote(true);
        Preference.setAlarmUseSnooze(false);
        Preference.setSnoozeStartTime(Tuda.SNOOZE_START_TIME);
        Preference.setSnoozeEndTime(Tuda.SNOOZE_END_TIME);
        Preference.setAlarmSound(true);
        Preference.setAlarmVibration(true);
        Preference.setAlarmUseLesson(true);
        Preference.setAlarmTime("1시간전");
        Preference.setAutoLockPassword("");

        // 기본 환경설정 서버 저장 (UUID 로 저장)
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setAutoLogin(true);
        configInfo.setAutoLock(false);
        configInfo.setAlarmUseService(true);
        configInfo.setAlarmUseTalk(true);
        configInfo.setAlarmUseNote(true);
        configInfo.setAlarmUseSnooze(true);
        configInfo.setSnoozeStartTime(Tuda.SNOOZE_START_TIME);
        configInfo.setSnoozeEndTime(Tuda.SNOOZE_END_TIME);
        configInfo.setAlarmSound(true);
        configInfo.setAlarmVibration(true);
        configInfo.setAlarmUseLesson(true);
        configInfo.setAlarmTime("1시간전");
        configInfo.setAutoLockPassword("");

        setConfigInfo(configInfo);
    }

    public static void setDeviceUUID(String uuid) {
        sharedPref.edit().putString(PREF_UUID, uuid).commit();
    }

    public static String getDeviceUUID() {
        return sharedPref.getString(PREF_UUID, "");
    }

    public static void setLoginToken(String token) {
        sharedPref.edit().putString(PREF_LOGIN_TOKEN, token).commit();
    }

    public static String getLoginToken() {
        return sharedPref.getString(PREF_LOGIN_TOKEN, null);
    }

    public static void setLoginAuthKey(String authKey) {
        sharedPref.edit().putString(PREF_LOGIN_AUTHKEY, authKey).commit();
    }

    public static String getLoginAuthKey() {
        return sharedPref.getString(PREF_LOGIN_AUTHKEY, null);
    }

    public static void setLoginAuthType(String authType) {
        sharedPref.edit().putString(PREF_LOGIN_AUTHTYPE, authType).commit();
    }

    public static String getLoginAuthType() {
        return sharedPref.getString(PREF_LOGIN_AUTHTYPE, null);
    }

    public static String getLoginPassword() {
        return sharedPref.getString(PREF_LOGIN_PASSWORD, null);
    }

    public static void setLoginPassword(String password) {
        sharedPref.edit().putString(PREF_LOGIN_PASSWORD, password).commit();
    }

    public static Long getLoginMemberSrl() {
        return sharedPref.getLong(PREF_LOGIN_MEMBERSRL, 0L);
    }

    public static void setLoginMemberSrl(Long memberSrl) {
        sharedPref.edit().putLong(PREF_LOGIN_MEMBERSRL, memberSrl).commit();
    }

    public static List<Long> getLoginGroupSrl() {
        String json = sharedPref.getString(PREF_LOGIN_GROUPSRL, "");
        List<Long> groupSrls = null;

        try {
            JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, Long.class);
            groupSrls = mapper.readValue(json, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return groupSrls;
    }

    public static boolean hasLoginGroupSrl(Long groupSrl) {
        List<Long> groupSrls = getLoginGroupSrl();

        if (groupSrls == null || groupSrl == null)
            return false;

        for (Long srl : groupSrls)
            if (srl.equals(groupSrl))
                return true;

        return false;
    }

    public static void setLoginGroupSrl(List<Long> groupSrls) {

        String json = "";
        try {
            json = mapper.writeValueAsString(groupSrls);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        sharedPref.edit().putString(PREF_LOGIN_GROUPSRL, json).commit();
    }

    public static String getLoginEmail() {
        return sharedPref.getString(PREF_LOGIN_EMAIL, null);
    }

    public static void setLoginEmail(String email) {
        sharedPref.edit().putString(PREF_LOGIN_EMAIL, email).commit();
    }

    public static String getNickName() {
        return sharedPref.getString(PREF_NICKNAME, null);
    }

    public static void setNickName(String nickName) {
        sharedPref.edit().putString(PREF_NICKNAME, nickName).commit();
    }

    public static String getAddress() {
        return sharedPref.getString(PREF_ADDRESS, null);
    }

    public static void setAddress(String address) {
        sharedPref.edit().putString(PREF_ADDRESS, address).commit();
    }

    public static String getUserType() {
        return sharedPref.getString(PREF_LOGIN_USERTYPE, null);
    }

    public static void setUserType(UserType userType) {
        String typeValue = (userType != null) ? userType.getValue() : null;
        sharedPref.edit().putString(PREF_LOGIN_USERTYPE, typeValue).commit();
    }

    public static String getGender() {
        return sharedPref.getString(PREF_LOGIN_GENDER, null);
    }

    public static void setGender(Gender gender) {
        String typeValue = (gender != null) ? gender.getValue() : null;
        sharedPref.edit().putString(PREF_LOGIN_GENDER, typeValue).commit();
    }

    public static Boolean getIdentificationOk() {
        return sharedPref.getBoolean(PREF_LOGIN_IDENTIFICATION_OK, false);
    }

    public static void setIdentificationOk(Boolean identificationOk) {
        sharedPref.edit().putBoolean(PREF_LOGIN_IDENTIFICATION_OK, identificationOk).commit();
    }

    public static Boolean getMemberInfoOk() {
        return sharedPref.getBoolean(PREF_MEMBERINFO_OK, false);
    }

    public static void setMemberInfoOk(Boolean isOk) {
        sharedPref.edit().putBoolean(PREF_MEMBERINFO_OK, isOk).commit();
    }

    public static void login(String token, String authKey, AuthType authType, String email, String password) {
        setLoginToken(token);
        setLoginAuthKey(authKey);
        setLoginAuthType(authType.getValue());
        setLoginEmail(email);
        setLoginPassword(password);
    }

    public static void logout() {
        setLoginToken(null);
        setLoginAuthKey(null);
        setLoginAuthType(null);
        setLoginPassword(null);
        setLoginMemberSrl(0L);
        setLoginGroupSrl(null);
        setNickName(null);
        setUserType(null);
        setGender(null);
    }

    public static void setFirstRunAfterInstall() {
        sharedPref.edit().putBoolean(PREF_FIRST_RUN_AFTER_INSTALL, true).commit();
    }

    public static boolean getFirstRunAfterInstall() {
        return sharedPref.getBoolean(PREF_FIRST_RUN_AFTER_INSTALL, false);
    }

    public static void setIsAppRunning(boolean isAppRunning) {
        sharedPref.edit().putBoolean(PREF_IS_APP_RUNNING, isAppRunning).commit();
    }

    public static boolean getIsAppRunning() {
        return sharedPref.getBoolean(PREF_IS_APP_RUNNING, false);
    }

    public static void setStudentSort(String sortValue) {
        sharedPref.edit().putString(PREF_STUDENT_LIST_SORT, sortValue).commit();
    }

    public static String getStudentSort() {
        return sharedPref.getString(PREF_STUDENT_LIST_SORT, "createdAt");
    }

    public static void setLastLessonCheckDate() {
        sharedPref.edit().putString(PREF_LAST_LESSON_CHECKED_DATE, DataManager.getNow(FormatManager.DATE)).commit();
    }

    public static String getLastLessonCheckDate() {
        return sharedPref.getString(PREF_LAST_LESSON_CHECKED_DATE, DataManager.getNow(FormatManager.DATE));
    }

    public static void setLessonChecked(String checkString) {
        String prefString = getLessonChecked();

        // 마지막 수업체크 날짜와 현재 날짜가 다르면 체크 스트링 초기화
        if (!getLastLessonCheckDate().equals(DataManager.getNow(FormatManager.DATE))) {
            setLastLessonCheckDate();
            prefString = "";
        }

        prefString += checkString;
        sharedPref.edit().putString(PREF_LESSON_CHECKED, prefString).commit();
    }

    public static String getLessonChecked() {
        return sharedPref.getString(PREF_LESSON_CHECKED, "");
    }

    public static void setBookNameList(String bookName) {
        String bookNameList = getBookNameList();
        if (!bookNameList.contains(bookName)) {
            String newBookNameList = (bookNameList.length() == 0) ? bookName : bookNameList + "|" + bookName;
            sharedPref.edit().putString(PREF_BOOK_NAME_LIST, newBookNameList).commit();
        }
    }

    public static String getBookNameList() {
        return sharedPref.getString(PREF_BOOK_NAME_LIST, "");
    }

    public static void setPushToken(String pushToken, Boolean alarmUseService) {
        sharedPref.edit().putString(PREF_PUSH_TOKEN, pushToken).commit();

        // push token 서버 저장
        PushEntity requestItem = new PushEntity();
        requestItem.setPushKey(pushToken);
        requestItem.setUseOk(alarmUseService);

        RequestEntry<PushEntity> requestEntry = new RequestEntry<PushEntity>(requestItem);
        TudaApp.api.pushUp(requestEntry).enqueue(new Callback<ResponseEntry>() {
            @Override
            public void onResponse(Call<ResponseEntry> request, Response<ResponseEntry> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && EntryStatus.SUCCESS.equals(response.body().getStatus())) {
                        Log.e(TAG, "pushUp Success...");
                    } else {
                        Log.e(TAG, "pushUp Fail... : " + Log.makeErrorMsg(response.body()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseEntry> call, Throwable t) {
                Log.e(TAG, "pushUp Error... : " + t.getMessage());
            }

        });
    }

    public static String getPushToken() {
        return sharedPref.getString(PREF_PUSH_TOKEN, "");
    }
    public static ObjectMapper mapper = new ObjectMapper();

    public static void setLastUpdateCheck(long lastTime) {
        sharedPref.edit().putLong(PREF_LAST_UPDATE_NOTI, lastTime).commit();
    }

    public static long getLastUpdateCheck() {
        return sharedPref.getLong(PREF_LAST_UPDATE_NOTI, 0l);
    }

    public static void setAdvLastTime(long lastTime) {
        sharedPref.edit().putLong(PREF_LAST_ADV_VIEW, lastTime).commit();
    }

    public static long getAdvLastTime() {
        return sharedPref.getLong(PREF_LAST_ADV_VIEW, 0l);
    }

    public static void setConfigInit(boolean autoLogin) {
        sharedPref.edit().putBoolean(PREF_CONFIG_INIT, autoLogin).commit();
    }

    public static boolean getConfigInit() {
        return sharedPref.getBoolean(PREF_CONFIG_INIT, false);
    }

    public static void setAutoLogin(boolean autoLogin) {
        sharedPref.edit().putBoolean(PREF_CONFIG_AUTO_LOGIN, autoLogin).commit();
    }

    public static boolean getAutoLogin() {
        return sharedPref.getBoolean(PREF_CONFIG_AUTO_LOGIN, true);
    }

    public static void setAutoLock(boolean autoLock) {
        sharedPref.edit().putBoolean(PREF_CONFIG_AUTO_LOCK, autoLock).commit();
    }

    public static boolean getAutoLock() {
        return sharedPref.getBoolean(PREF_CONFIG_AUTO_LOCK, false);
    }

    public static void setAlarmUseService(boolean alarmUse) {
        sharedPref.edit().putBoolean(PREF_CONFIG_ALARM_USE_SERVICE, alarmUse).commit();
    }

    public static boolean getAlarmUseService() {
        return sharedPref.getBoolean(PREF_CONFIG_ALARM_USE_SERVICE, true);
    }

    public static void setAlarmUseTalk(boolean talk) {
        sharedPref.edit().putBoolean(PREF_CONFIG_ALARM_USE_TALK, talk).commit();
    }

    public static boolean getAlarmUseTalk() {
        return sharedPref.getBoolean(PREF_CONFIG_ALARM_USE_TALK, true);
    }

    public static void setAlarmUseNote(boolean note) {
        sharedPref.edit().putBoolean(PREF_CONFIG_ALARM_USE_NOTE, note).commit();
    }

    public static boolean getAlarmUseNote() {
        return sharedPref.getBoolean(PREF_CONFIG_ALARM_USE_NOTE, true);
    }

    public static void setAlarmUseSnooze(boolean snooze) {
        sharedPref.edit().putBoolean(PREF_CONFIG_ALARM_USE_SNOOZE, snooze).commit();
    }

    public static boolean getAlarmUseSnooze() {
        return sharedPref.getBoolean(PREF_CONFIG_ALARM_USE_SNOOZE, false);
    }

    public static void setSnoozeStartTime(String start) {
        sharedPref.edit().putString(PREF_CONFIG_SNOOZE_START_TIME, start).commit();
    }

    public static String getSnoozeStartTime() {
        return sharedPref.getString(PREF_CONFIG_SNOOZE_START_TIME, Tuda.SNOOZE_START_TIME);
    }

    public static void setSnoozeEndTime(String end) {
        sharedPref.edit().putString(PREF_CONFIG_SNOOZE_END_TIME, end).commit();
    }

    public static String getSnoozeEndTime() {
        return sharedPref.getString(PREF_CONFIG_SNOOZE_END_TIME, Tuda.SNOOZE_END_TIME);
    }

    public static void setAlarmUseLesson(boolean alarmUse) {
        sharedPref.edit().putBoolean(PREF_CONFIG_ALARM_USE_LESSON, alarmUse).commit();
    }

    public static boolean getAlarmUseLesson() {
        return sharedPref.getBoolean(PREF_CONFIG_ALARM_USE_LESSON, false);
    }

    public static void setAlarmTime(String alarmTime) {
        sharedPref.edit().putString(PREF_CONFIG_ALARM_TIME, alarmTime).commit();
    }

    public static String getAlarmTime() {
        return sharedPref.getString(PREF_CONFIG_ALARM_TIME, "");
    }

    public static void setAlarmSound(boolean enable) {
        sharedPref.edit().putBoolean(PREF_CONFIG_ALARM_SOUND, enable).commit();
    }

    public static boolean getAlarmSound() {
        return sharedPref.getBoolean(PREF_CONFIG_ALARM_SOUND, true);
    }

    public static void setAlarmVibration(boolean enable) {
        sharedPref.edit().putBoolean(PREF_CONFIG_ALARM_VIBRATION, enable).commit();
    }

    public static boolean getAlarmVibration() {
        return sharedPref.getBoolean(PREF_CONFIG_ALARM_VIBRATION, true);
    }

    public static void setAutoLockPassword(String alarmTime) {
        sharedPref.edit().putString(PREF_CONFIG_AUTOLOCK_PASSWD, alarmTime).commit();
    }

    public static String getAutoLockPassword() {
        return sharedPref.getString(PREF_CONFIG_AUTOLOCK_PASSWD, "");
    }

    public static void setViewIntro(boolean view_intro) {
        sharedPref.edit().putBoolean(PREF_VIEW_INTRO, view_intro).commit();
    }

    public static boolean getViewIntro() {
        return sharedPref.getBoolean(PREF_VIEW_INTRO, false);
    }

    public static void setViewEvent(String eventInfo) {
        sharedPref.edit().putString(PREF_VIEW_EVENT, eventInfo).commit();
    }

    public static String getViewEvent() {
        return sharedPref.getString(PREF_VIEW_EVENT, "");
    }

    public static void setMarketAgree(boolean marketAgree) {
        sharedPref.edit().putBoolean(PREF_CONFIG_MARKET_AGREE, marketAgree).commit();
    }

    public static boolean getMarketAgree() {
        return sharedPref.getBoolean(PREF_CONFIG_MARKET_AGREE, false);
    }

    public static void setSnsFacebook(boolean sns) {
        sharedPref.edit().putBoolean(PREF_SNS_FACEBOOK, sns).commit();
    }

    public static boolean getSnsFacebook() {
        return sharedPref.getBoolean(PREF_SNS_FACEBOOK, false);
    }

    public static void setSnsNaver(boolean sns) {
        sharedPref.edit().putBoolean(PREF_SNS_NAVER, sns).commit();
    }

    public static boolean getSnsNaver() {
        return sharedPref.getBoolean(PREF_SNS_NAVER, false);
    }

    public static void setSnsKakao(boolean sns) {
        sharedPref.edit().putBoolean(PREF_SNS_KAKAO, sns).commit();
    }

    public static boolean getSnsKakao() {
        return sharedPref.getBoolean(PREF_SNS_KAKAO, false);
    }

    public static void setCsNoticeView(boolean isView) {
        sharedPref.edit().putBoolean(PREF_CS_NOTICE_VIEW, isView).commit();
    }

    public static boolean getCsNoticeView() {
        return sharedPref.getBoolean(PREF_CS_NOTICE_VIEW, false);
    }

    public static void setIsLastMenuView(boolean isView) {
        sharedPref.edit().putBoolean(PREF_IS_LASTMENU_VIEW, isView).commit();
    }

    public static boolean getIsLastMenuView() {
        return sharedPref.getBoolean(PREF_IS_LASTMENU_VIEW, true);
    }

    public static void setLastMenuGnb(String lastMenuGnb) {
        sharedPref.edit().putString(PREF_LASTMENU_GNB, lastMenuGnb).commit();
    }

    public static String getLastMenuGnb() {
        return sharedPref.getString(PREF_LASTMENU_GNB, "schedule");
    }

    public static void setLastMenuTalk(String lastMenuTalk) {
        sharedPref.edit().putString(PREF_LASTMENU_TALK, lastMenuTalk).commit();
    }

    public static String getLastMenuTalk() {
        return sharedPref.getString(PREF_LASTMENU_TALK, "");
    }

    public static void setLastMenuMarket(String lastMenuMarket) {
        sharedPref.edit().putString(PREF_LASTMENU_MARKET, lastMenuMarket).commit();
    }

    public static String getLastMenuMarket() {
        return sharedPref.getString(PREF_LASTMENU_MARKET, "");
    }

    public static void setMainReviewNoticeView(int viewDate) {
        sharedPref.edit().putInt(PREF_MAIN_NOTICE_VIEW_REVIEW, viewDate).commit();
    }

    public static int getMainReviewNoticeView() {
        return sharedPref.getInt(PREF_MAIN_NOTICE_VIEW_REVIEW, 0);
    }

    public static void setTalkNickGender(boolean isOk) {
        sharedPref.edit().putBoolean(PREF_TALK_NICKGENDER, isOk).commit();
    }

    public static boolean getTalkNickGender() {
        return sharedPref.getBoolean(PREF_TALK_NICKGENDER, false);
    }

    public static long getDefaultSyncCalendarId() {
        return sharedPref.getLong(PREF_CALENDAR_DEFAULT_SYNC_CALENDAR_ID, -999L);
    }

    public static void setDefaultSyncCalendarId(Long calendarId) {
        sharedPref.edit().putLong(PREF_CALENDAR_DEFAULT_SYNC_CALENDAR_ID, calendarId).commit();
    }

    public static boolean getCalendarMigrationStatus() {
        return sharedPref.getBoolean(PREF_CALENDAR_MIGRATION_STATUS, false);
    }

    public static void setCalendarMigrationStatus(boolean flag) {
        sharedPref.edit().putBoolean(PREF_CALENDAR_MIGRATION_STATUS, flag).commit();
    }

    public static WeatherLocationMappingForm getDefaultWeatherLocation() {
        String json = sharedPref.getString(PREF_WEATHER_DEFAULT_LOCATION, "");
        WeatherLocationMappingForm location = null;

        try {
            location = mapper.readValue(json, WeatherLocationMappingForm.class);
        } catch (IOException e) {
        }

        return location;
    }

    public static void setDefaultWeatherLocation(WeatherLocationMappingForm form) {

        String json = "";
        try {
            json = mapper.writeValueAsString(form);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        sharedPref.edit().putString(PREF_WEATHER_DEFAULT_LOCATION, json).commit();
    }

    public static void setPhoneNumberNoticeView(int viewDate) {
        sharedPref.edit().putInt(PREF_PHONE_NOTICE_VIEW, viewDate).commit();
    }

    public static int getPhoneNumberNoticeView() {
        return sharedPref.getInt(PREF_PHONE_NOTICE_VIEW, 0);
    }

    public static Boolean getOpenMenuScreen() {
        return sharedPref.getBoolean(PREF_OPEN_MENUSCREEN, false);
    }

    public static void setOpenMenuScreen(Boolean openMenuScreen) {
        sharedPref.edit().putBoolean(PREF_OPEN_MENUSCREEN, openMenuScreen).commit();
    }

    public static void setMarketIntro(boolean marketIntro) {
        sharedPref.edit().putBoolean(PREF_MARKET_INTRO, marketIntro).commit();
    }

    public static boolean getMarketIntro() {
        return sharedPref.getBoolean(PREF_MARKET_INTRO, false);
    }

    public static void setTalkNoticeView(boolean isView) {
        sharedPref.edit().putBoolean(PREF_TALK_NOTICE_VIEW, isView).commit();
    }

    public static boolean getTalkNoticeView() {
        return sharedPref.getBoolean(PREF_TALK_NOTICE_VIEW, false);
    }

    public static void setProfileWriteCheck(boolean isChecked) {
        sharedPref.edit().putBoolean(PREF_PROFILE_WRITE_CHECK, isChecked).commit();
    }

    public static boolean getProfileWriteCheck() {
        return sharedPref.getBoolean(PREF_PROFILE_WRITE_CHECK, false);
    }

    public static void setTalkCategoryViewForTalk(boolean isView) {
        sharedPref.edit().putBoolean(PREF_TALK_CATEGORY_VIEW_FOR_TALK, isView).commit();
    }

    public static boolean getTalkCategoryViewForTalk() {
        return sharedPref.getBoolean(PREF_TALK_CATEGORY_VIEW_FOR_TALK, false);
    }

    public static void setFineDust(String findDust) {
        sharedPref.edit().putString(PREF_FINE_DUST, findDust).commit();
    }

    public static String getFineDust() {
        return sharedPref.getString(PREF_FINE_DUST, "");
    }

    public static void setPointColor(int resId) {
        sharedPref.edit().putInt(PREF_POINT_COLOR, resId).commit();
    }

    public static int getPointColor() {
        int color = sharedPref.getInt(PREF_POINT_COLOR, ContextCompat.getColor(TudaApp.getGlobalApplicationContext(), R.color.point_color_2));
        if (color > 0)
            color = ContextCompat.getColor(TudaApp.getGlobalApplicationContext(), R.color.point_color_2);
        return color;
    }

    public static void setCalendarGuide(boolean isGuide) {
        sharedPref.edit().putBoolean(PREF_CALENDAR_GUIDE, isGuide).commit();
    }

    public static boolean getCalendarGuide() {
        return sharedPref.getBoolean(PREF_CALENDAR_GUIDE, false);
    }

    public static void setMainTutorial(boolean isView) {
        sharedPref.edit().putBoolean(PREF_MAIN_TUTORIAL, isView).commit();
    }

    public static boolean getMainTutorial() {
        return sharedPref.getBoolean(PREF_MAIN_TUTORIAL, false);
    }

    public static void setGnbSwipe(boolean isView) {
        sharedPref.edit().putBoolean(PREF_GNB_SWIPE, isView).commit();
    }

    public static boolean getGnbSwipe() {
        return sharedPref.getBoolean(PREF_GNB_SWIPE, false);
    }

    public static HashMap<String, Boolean> getCalendarVisibility() {
        String json = sharedPref.getString(PREF_CALENDAR_VISIBLE_INFO, "");
        HashMap visibleInfo = null;

        try {
            visibleInfo = mapper.readValue(json, HashMap.class);
        } catch (IOException e) {
        }

        if (visibleInfo == null)
            visibleInfo = new HashMap();

        return visibleInfo;
    }

    public static void setCalendarVisibility(HashMap<String, Boolean> visibleInfo) {

        if (visibleInfo == null)
            visibleInfo = new HashMap<>();
        String json = "";
        try {
            json = mapper.writeValueAsString(visibleInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        sharedPref.edit().putString(PREF_CALENDAR_VISIBLE_INFO, json).commit();
    }

    public static void setAuthMailDialogView(int viewDate) {
        sharedPref.edit().putInt(PREF_AUTH_MAIL_DIALOG_VIEW, viewDate).commit();
    }

    public static int getAuthMailDialogView() {
        return sharedPref.getInt(PREF_AUTH_MAIL_DIALOG_VIEW, 0);
    }
}
