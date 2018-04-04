package com.tuda.teacher;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.tuda.teacher.common.Preference;
import com.tuda.teacher.common.Tuda;
import com.tuda.teacher.database.DatabaseManager;
import com.tuda.teacher.network.ApiInterface;
import com.tuda.teacher.network.ApiService;
import com.tuda.teacher.util.GAManager;
import com.tuda.teacher.util.Log;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class TudaApp extends MultiDexApplication {

    private static String TAG = "TudaApp";

    public static ApiService apiService  = null;            // network library (retrofit)
    public static ApiInterface api = null;                  // network library (retrofit)
    public static GAManager gaManager = null;               // GoogleAnalytics
    private static TudaApp app;                             // application base class

    private static Activity currentActivity = null;

    public static TudaApp getInstance() {
        return app;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
    public static void setCurrentActivity(Activity currentActivity) {
        TudaApp.currentActivity = currentActivity;
    }

    public static TudaApp getGlobalApplicationContext() {
        if(app == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = (TudaApp) getApplicationContext();

        // POST방식 서버 통신시 java.io.EOFException 오류
        // - libcore.io.Streams.readAsciiLine(Streams.java:203)
        System.setProperty("http.keepAlive", "false");

        // Crashlytics(Fabric) 초기화
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        // preference init
        Preference.setupDefaults(PreferenceManager.getDefaultSharedPreferences(this));

        // ORMLite 초기화
        DatabaseManager.init(getApplicationContext());

        // API Service 초기화
        apiService = new ApiService();
        api = apiService.getApiInterface();

        // Google Analytics Manager 초기화
        gaManager = new GAManager(getApplicationContext());

        // GA Crash Report
        gaManager.crashTracking();

        // kakao init
        //KakaoSDK.init(new KakaoSDKAdapter());

        // 저장공간 생성
        makePhotoSaveStorage();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void updateApiService() {
        apiService.rerun();
        api = apiService.getApiInterface();
    }

    private void makePhotoSaveStorage() {
        try {
            // 내 사진 저장소
            String pathMy = Tuda.MYPICTURE_FILE_PATH;
            File fileMy = new File(pathMy);
            if (!fileMy.exists())
                fileMy.mkdirs();

            // 학생 사진 저장소
            String pathStudent = Tuda.PHOTO_FILE_PATH;
            File fileStudent = new File(pathStudent);
            if (!fileStudent.exists())
                fileStudent.mkdirs();

            // 튜다톡 사진 저장소
            String pathTalk = Tuda.TALK_FILE_PATH;
            File fileTalk = new File(pathTalk);
            if (!fileTalk.exists())
                fileTalk.mkdirs();

            // 튜다톡방 사진 저장소
            String pathTalkRoom = Tuda.TALKROOM_FILE_PATH;
            File fileTalkRoom = new File(pathTalkRoom);
            if (!fileTalkRoom.exists())
                fileTalkRoom.mkdirs();

            // 이미지 다운로드 저장소
            String pathImage = Tuda.PICTURES_STORAGE_FILE_PATH;
            File fileImage = new File(pathImage);
            if (!fileImage.exists())
                fileImage.mkdirs();

            // 메모 저장소
            String pathMemo = Tuda.MEMO_FILE_PATH;
            File fileMemo = new File(pathMemo);
            if (!fileMemo.exists())
                fileMemo.mkdirs();
        } catch (Exception e) {
            Log.e(TAG, "makePhotoSaveStorage Error... - "+e.toString());
        }
    }
}
