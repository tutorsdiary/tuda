package com.tuda.teacher.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.firebase.messaging.RemoteMessage;
import com.tuda.teacher.BuildConfig;
import com.tuda.teacher.R;
import com.tuda.teacher.common.Preference;
import com.tuda.teacher.common.Tuda;
import com.tuda.teacher.database.AlarmItem;
import com.tuda.teacher.database.DatabaseManager;
import com.tuda.teacher.database.NoteList;
import com.tuda.teacher.database.TalkList;
import com.tuda.teacher.database.TalkRoomConfig;
import com.tuda.teacher.talk.activity.TalkListActivity;
import com.tuda.teacher.type.Constants;
import com.tuda.teacher.type.QuickstartPreferences;
import com.tuda.teacher.ui.activity.BackgroundActivity;
import com.tuda.teacher.util.AppInfoManager;
import com.tuda.teacher.util.DataManager;
import com.tuda.teacher.util.Log;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";

    private String pushType;
    private String topicType = null;

    private String title;
    private String message;
    private String menuAlias;

    private Long srl = null;
    private Long roomSrl;
    private Long memberSrl;
    private String nickName;
    private String createdAt;
    private String picture;
    private String userType;
    private String gender;
    private String marketType;
    private Long marketSrl;
    private String webUrl;

    /**
     *
     * param from SenderID 값을 받아온다.
     * param data Set형태로 FCM으로 받은 데이터
     *
     *             공통>
     *             pt - pushType (norm : 일반푸시, note : 쪽지, talk_secret : 1:1톡,
     *                              talk_join : 톡 신규회원 환영, topic : 토픽)
     *             tt - topicType (noti : 공지사항, web : 모바일웹 오픈, talk_open : 신규톡방, talk : 튜다톡)
     *             tit - title
     *             msg - message
     *
     *             타입별>
     *             srl - list item serial
     *             ma - menuAlias
     *             mSrl - memberSrl
     *             rSrl - roomSrl
     *             name - nickName
     *             cAt - createdAt
     *             pic - picture
     *             ut - userType
     *             gd - gender
     *             mt - marketType
     *             mkSrl - marketSrl
     *             tMt - talk MenuType
     *             url - web url
     */

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null)
            return;

        if (BuildConfig.DEBUG) {
            Iterator<String> iterator = remoteMessage.getData().keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = remoteMessage.getData().get(key).toString();
                Log.e(TAG, "onMessage ::: key:" + key + ", value:" + value);
            }
        }

        sendNotification(remoteMessage.getData());
    }

    private void sendNotification(Map<String, String> data) {

        pushType = data.get("pt").toString();
        Log.e(TAG, "pushType is ..." + pushType);

        // push type 이 없으면 처리 안함
        if (TextUtils.isEmpty(pushType))
            return;

        if (Constants.PUSH_TOPIC.equals(pushType)) {
            if (TextUtils.isEmpty(topicType))
                topicType = data.get("tt").toString();
            if (TextUtils.isEmpty(message))
                message = data.get("msg").toString();

            if (TextUtils.isEmpty(topicType) || TextUtils.isEmpty(message))
                return;

            // 기능이 있는 topic push 라면 pushType 을 topicType 으로 대체
            if (!topicType.isEmpty())
                pushType = (topicType.startsWith("talkroom_")) ? "talk" : topicType;

            if (BuildConfig.DEBUG)
                Log.e(TAG, "TopicType: " + topicType + " / Title: " + title + " / Message: " + message);

            if ("noti".equals(topicType)) {
                if (TextUtils.isEmpty(title))
                    title = data.get("tit").toString();
                if (srl == null) {
                    try {
                        srl = Long.parseLong(data.get("srl").toString());
                    } catch (Exception e) {
                        srl = null;
                    }
                }

                if (srl == null || TextUtils.isEmpty(title))
                    return;

                if (BuildConfig.DEBUG)
                    Log.e(TAG, "Srl: " + srl);

                setNotification();
            } else if ("web".equals(topicType)) {
                if (TextUtils.isEmpty(title))
                    title = data.get("tit").toString();
                if (TextUtils.isEmpty(webUrl))
                    webUrl = data.get("url").toString();

                if (TextUtils.isEmpty(webUrl) || TextUtils.isEmpty(title))
                    return;

                if (BuildConfig.DEBUG)
                    Log.e(TAG, "webUrl: " + webUrl);

                setWebPush();
            } else if ("talk_open".equals(topicType)) {
                if (TextUtils.isEmpty(title))
                    title = data.get("tit").toString();
                if (srl == null) {
                    try {
                        srl = Long.parseLong(data.get("srl").toString());
                    } catch (Exception e) {
                        srl = null;
                    }
                }

                if (srl == null || TextUtils.isEmpty(title))
                    return;

                roomSrl = srl;

                if (BuildConfig.DEBUG)
                    Log.e(TAG, "roomSrl: " + roomSrl);

                setTalkNotification("mainAndTalk", false);
            } else if (topicType.startsWith("talkroom_")) {
                try {
                    srl = Long.parseLong(data.get("srl").toString());
                    roomSrl = Long.parseLong(data.get("rSrl").toString());
                    memberSrl = Long.parseLong(data.get("mSrl").toString());
                } catch (Exception e) {
                    roomSrl = null;
                }
                nickName = data.get("name").toString();
                createdAt = data.get("cAt").toString();
                picture = data.get("pic").toString();
                userType = data.get("ut").toString();
                gender = data.get("gd").toString();

                if (roomSrl == null || memberSrl == Preference.getLoginMemberSrl())
                    return;

                if (BuildConfig.DEBUG)
                    Log.e(TAG, "Talk srl: " + srl + " / roomSrl: " + roomSrl + " / memberSrl: " + memberSrl + " / nickName: " + nickName + " / createdAt: " + createdAt + " / picture: " + picture + " / userType: " + userType + " / gender: "+ gender);

                // push 로 들어온 채팅 메세지를 broadcast
                if (Tuda.TALK_ROOM_SRL == null) {
                    // 채팅방에 들어가 있지 않은 경우(앱 종료 상태 포함) 하단 메뉴를 튜다톡으로 변경하고 채팅창 띄움
                    setTalkNotification("mainAndTalk", true);
                } else {
                    if (!roomSrl.equals(Tuda.TALK_ROOM_SRL)) {
                        // 현재 들어가있는 채팅방과 다른 방에서 메세지가 온 경우 튜다톡 액티비티로 이동후 listview 갱신
                        setTalkNotification("talk", true);
                    } else {
                        // 현재 들어가있는 채팅방에서 메세지가 온 경우 메세지 바로 전달
                        sendFromPushToTalk(true);
                    }
                }
            } else {
                setTopicPush();
            }
        } else if (Constants.PUSH_NORMAL.equals(pushType)) {
            if (TextUtils.isEmpty(title))
                title = data.get("tit").toString();
            if (TextUtils.isEmpty(message))
                message = data.get("msg").toString();
            if (TextUtils.isEmpty(menuAlias))
                menuAlias = data.get("ma").toString();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(message))
                return;

            if (BuildConfig.DEBUG)
                Log.e(TAG, "Title: " + title + " / Message: " + message + " / menuAlias: " + menuAlias);

            setNormalPush();
        } else if (Constants.PUSH_NOTE.equals(pushType)) {
            message = data.get("msg").toString();
            try {
                srl = Long.parseLong(data.get("srl").toString());
            } catch (Exception e) {
                srl = null;
            }

            if (TextUtils.isEmpty(message) || srl == null)
                return;

            if (BuildConfig.DEBUG)
                Log.e(TAG, "Note Message: " + message + " / srl: " + srl);

            setNote();
        } else if (Constants.PUSH_TALK.equals(pushType)) {
            message = data.get("msg");
            try {
                srl = Long.parseLong(data.get("srl").toString());
                roomSrl = Long.parseLong(data.get("rSrl").toString());
                memberSrl = Long.parseLong(data.get("mSrl").toString());
            } catch (Exception e) {
                roomSrl = null;
            }
            nickName = data.get("name").toString();
            createdAt = data.get("cAt").toString();
            picture = data.get("pic").toString();
            userType = data.get("ut").toString();
            gender = data.get("gd").toString();

            if (TextUtils.isEmpty(message) || roomSrl == null)
                return;

            if (BuildConfig.DEBUG)
                Log.e(TAG, "Talk Message: " + message + " / srl: " + srl + " / roomSrl: " + roomSrl + " / memberSrl: " + memberSrl + " / nickName: " + nickName + " / createdAt: " + createdAt + " / picture: " + picture + " / userType: " + userType + " / gender: "+ gender);

            // push 로 들어온 채팅 메세지를 broadcast
            if (Tuda.TALK_ROOM_SRL == null) {
                // 채팅방에 들어가 있지 않은 경우(앱 종료 상태 포함) 하단 메뉴를 튜다톡으로 변경하고 채팅창 띄움
                setTalkNotification("mainAndTalk", true);
            } else {
                if (!roomSrl.equals(Tuda.TALK_ROOM_SRL)) {
                    // 현재 들어가있는 채팅방과 다른 방에서 메세지가 온 경우 튜다톡 액티비티로 이동후 listview 갱신
                    setTalkNotification("talk", true);
                } else {
                    // 현재 들어가있는 채팅방에서 메세지가 온 경우 메세지 바로 전달
                    sendFromPushToTalk(true);
                }
            }
        } else if (Constants.PUSH_TALK_SECRET.equals(pushType)) {
            title = data.get("tit").toString();
            message = data.get("msg").toString();

            try {
                roomSrl = Long.parseLong(data.get("rSrl").toString());
            } catch (Exception e) {
                roomSrl = null;
            }
            createdAt = data.get("cAt").toString();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(message) || roomSrl == null)
                return;

            if (BuildConfig.DEBUG)
                Log.e(TAG, "Title: " + title + " / Message: " + message + " / roomSrl: " + roomSrl + " / createdAt: " + createdAt);

            setTalkSecretNotification();
        }
    }



    /*
     * 토픽 푸시 처리
     */
    private void setTopicPush() {
        // 방해 금지 시간 확인
        // 알림 수신설정이 꺼져있으면 처리 안함
        if (!isAbleAlarmTime() || !Preference.getAlarmUseService()) {
            // 알림센터 저장
            saveAlarmList(pushType, title, message, null);
            return;
        }

        String topicTitle = title;

        Intent intent = new Intent(this, BackgroundActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Tuda.EXTRA_PUSH_TYPE, pushType);
        intent.putExtra(Tuda.EXTRA_LAUNCH_TYPE, menuAlias);
        PendingIntent pendingIntent = getPendingIntent(this, intent);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
                .setTicker(topicTitle)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setDefaults(getAlarmType())
                .setContentTitle(topicTitle)
                .setContentText(message)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.tuda_launcher_icon));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.mint));
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small_transparent);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Tuda.NOTIFICATION_ID, notificationBuilder.build());

        // 알림센터 저장
        saveAlarmList(pushType, title, message, null);
    }

    /*
     * 일반 푸시 처리
     */
    private void setNormalPush() {
        // 방해 금지 시간 확인
        // 알림 수신설정이 꺼져있으면 처리 안함
        if (!isAbleAlarmTime() || !Preference.getAlarmUseService()) {
            // 알림센터 저장
            saveAlarmList(pushType, title, message, null);
            return;
        }

        String notiTitle = title;

        Intent intent = new Intent(this, BackgroundActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Tuda.EXTRA_PUSH_TYPE, pushType);
        intent.putExtra(Tuda.EXTRA_LAUNCH_TYPE, menuAlias);
        PendingIntent pendingIntent = getPendingIntent(this, intent);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
                .setTicker(notiTitle)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setDefaults(getAlarmType())
                .setContentTitle(notiTitle)
                .setContentText(message)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.tuda_launcher_icon));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.mint));
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small_transparent);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Tuda.NOTIFICATION_ID, notificationBuilder.build());

        // 알림센터 저장
        saveAlarmList(pushType, title, message, null);
    }

    /*
     * 공지사항
     */
    private void setNotification() {
        // 방해 금지 시간 확인
        // 알림 수신설정이 꺼져있으면 처리 안함
        if (!isAbleAlarmTime() || !Preference.getAlarmUseService()) {
            // 알림센터 저장
            saveAlarmList(pushType, title, message, srl.toString());
            return;
        }

        String notiTitle = title;

        Intent intent = new Intent(this, BackgroundActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Tuda.EXTRA_PUSH_TYPE, pushType);
        intent.putExtra(Tuda.EXTRA_PUSH_SRL, srl);
        PendingIntent pendingIntent = getPendingIntent(this, intent);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
                .setTicker(notiTitle)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setDefaults(getAlarmType())
                .setContentTitle(notiTitle)
                .setContentText(message)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.tuda_launcher_icon));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.mint));
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small_transparent);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Tuda.NOTIFICATION_ID, notificationBuilder.build());

        // 알림센터 저장
        saveAlarmList(pushType, title, message, srl.toString());
    }

    /*
     * 튜다톡
     * 마케팅 푸시(핑거푸시, 새방 생성 푸시)로 받은 경우에는 폰에 저장하지 않는다.
     */
    private void setTalkNotification(String mode, boolean isSave) {
        // 방해 금지 시간이면 띄우지 않음
        // 로그인이 안 되어 있거나 튜다톡 수신설정이 꺼져있으면 Notification 띄우지 않음
        // 톡방 설정에서 알림이 꺼져있으면 Notification 띄우지 않음
        TalkRoomConfig troomConfig = DatabaseManager.getInstance().getTalkRoomConfig(roomSrl, Preference.getLoginMemberSrl());
        if (!isAbleAlarmTime()
                || TextUtils.isEmpty(Preference.getLoginToken())
                || !Preference.getAlarmUseTalk()
                || (troomConfig != null && !troomConfig.getIsAlarm())) {

            subActionTalkNotification(isSave);
            return;
        }

        String notiTitle = "튜다톡";
        if (!TextUtils.isEmpty(title))
            notiTitle = title;

        Intent intent = null;
        if ("mainAndTalk".equals(mode)) {
            // 채팅방에 들어가 있지 않은 경우 하단 메뉴를 튜다톡으로 변경하고 채팅창 띄움
            intent = new Intent(this, BackgroundActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Tuda.EXTRA_PUSH_TYPE, pushType);
            intent.putExtra(Tuda.EXTRA_LAUNCH_TYPE, "talk");
            intent.putExtra(Tuda.EXTRA_TALKROOM_SRL, roomSrl);
        } else if ("talk".equals(mode)) {
            // 현재 들어가있는 채팅방과 다른 방에서 메세지가 온 경우 튜다톡 액티비티로 이동후 listview 갱신
            intent = new Intent(this, TalkListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Tuda.EXTRA_TALKROOM_JOINSOURCE, "push");
            intent.putExtra(Tuda.EXTRA_TALKROOM_SRL, roomSrl);
        }

        if (message.startsWith(Tuda.IMG_PREFIX))
            message = "사진";

        if (intent != null) {

            PendingIntent pendingIntent = getPendingIntent(this, intent);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
                    .setTicker(notiTitle)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(getAlarmType())
                    .setContentTitle(notiTitle)
                    .setContentText(message)
                    //.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.tuda_launcher_icon));
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.mint));
                notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small_transparent);
            } else {
                notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small);
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(Tuda.NOTIFICATION_ID, notificationBuilder.build());
        }

        subActionTalkNotification(isSave);
    }

    private void subActionTalkNotification(boolean isSave) {
        // 알림센터 저장
        saveAlarmList(pushType, "튜다톡 수신", message, roomSrl.toString(), true);

        if (isSave) {
            // 수신 톡 ORMLite DB 에 저장
            saveTalkList();

            // 읽지 않은 글 갯수 홈 화면 배지 업데이트
            Long allCountNotReadTalkList = DatabaseManager.getInstance().getAllCountNotReadTalkList(Preference.getLoginMemberSrl());
            AppInfoManager.updateIconBadge(getApplicationContext(), allCountNotReadTalkList.intValue());

            // 새글 리프레시
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(QuickstartPreferences.NEW_COUNT_RECEIVE_COMPLETE));
        }
    }

    private void sendFromPushToTalk(boolean isSave) {
        if (isSave) {
            // 알림센터 저장
            saveAlarmList(pushType, "튜다톡 수신", message, roomSrl.toString(), true);

            // 수신 톡 ORMLite DB 에 저장
            saveTalkList();

            // talkListActivity 에 message 전달
            if (!memberSrl.equals(Preference.getLoginMemberSrl())) {
                Intent talkReceiveComplete = new Intent(QuickstartPreferences.TALK_RECEIVE_COMPLETE);
                talkReceiveComplete.putExtra("srl", srl);
                talkReceiveComplete.putExtra("roomSrl", roomSrl);
                talkReceiveComplete.putExtra("memberSrl", memberSrl);
                talkReceiveComplete.putExtra("nickName", nickName);
                talkReceiveComplete.putExtra("message", message);
                talkReceiveComplete.putExtra("createdAt", createdAt);
                talkReceiveComplete.putExtra("picture", picture);
                talkReceiveComplete.putExtra("gender", gender);
                talkReceiveComplete.putExtra("userType", userType);
                LocalBroadcastManager.getInstance(this).sendBroadcast(talkReceiveComplete);
            }
        }
    }

    private void saveTalkList() {
        TalkList talkList = new TalkList();
        talkList.setSrl(srl);
        talkList.setRoomSrl(roomSrl);
        talkList.setMemberSrl(memberSrl);
        talkList.setNickName(nickName);
        talkList.setMessage(message);
        talkList.setIsRead(false);
        talkList.setCreatedAt(createdAt);
        talkList.setPicture(picture);
        talkList.setGender(gender);
        talkList.setUserType(userType);
        talkList.setReceiverSrl(Preference.getLoginMemberSrl());

        DatabaseManager.getInstance().addTalkList(talkList);
    }

    /*
     * 쪽지
     */
    private void setNote() {
        // 방해 금지 시간 확인
        // 로그인이 안 되어 있거나 쪽지 수신설정이 꺼져있으면 처리 안함
        if (!isAbleAlarmTime()
                || TextUtils.isEmpty(Preference.getLoginToken())
                || !Preference.getAlarmUseNote()) {
            subActionNote();
            return;
        }

        String notiTitle = "쪽지";

        Intent intent = new Intent(this, BackgroundActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Tuda.EXTRA_PUSH_TYPE, pushType);
        intent.putExtra(Tuda.EXTRA_PUSH_SRL, srl);
        PendingIntent pendingIntent = getPendingIntent(this, intent);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
                .setTicker(notiTitle)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setDefaults(getAlarmType())
                .setContentTitle(notiTitle)
                .setContentText(message)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.tuda_launcher_icon));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.mint));
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small_transparent);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Tuda.NOTIFICATION_ID, notificationBuilder.build());

        subActionNote();
    }

    private void subActionNote() {
        // 알림센터 저장
        saveAlarmList(pushType, "쪽지 수신", message, srl.toString());

        // 수신 쪽지 ORMLite DB 에 저장
        NoteList noteList = new NoteList(srl, Preference.getLoginMemberSrl());
        DatabaseManager.getInstance().addNoteList(noteList);

        // 새글 리프레시
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(QuickstartPreferences.NEW_COUNT_RECEIVE_COMPLETE));
    }

    /*
     * 톡 관련 기타기능(1:1톡, 등)
     */
    private void setTalkSecretNotification() {
        // 방해 금지 시간 확인
        // 알림 수신설정이 꺼져있으면 처리 안함
        if (!isAbleAlarmTime() || !Preference.getAlarmUseTalk()) {
            // 알림센터 저장
            saveAlarmList(pushType, title, message, roomSrl.toString());
            return;
        }

        String notiTitle = title;

        Intent intent = new Intent(this, BackgroundActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Tuda.EXTRA_PUSH_TYPE, pushType);
        intent.putExtra(Tuda.EXTRA_TALKROOM_SRL, roomSrl);
        PendingIntent pendingIntent = getPendingIntent(this, intent);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
                .setTicker(notiTitle)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setDefaults(getAlarmType())
                .setContentTitle(notiTitle)
                .setContentText(message)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.tuda_launcher_icon));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.pink));
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small_transparent);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Tuda.NOTIFICATION_ID_TALK_SECRET, notificationBuilder.build());

        // 알림센터 저장
        saveAlarmList(pushType, title, message, roomSrl.toString());
    }

    /*
     * 웹뷰
     */
    private void setWebPush() {
        // 방해 금지 시간 확인
        // 알림 수신설정이 꺼져있으면 처리 안함
        if (!isAbleAlarmTime() || !Preference.getAlarmUseService()) {
            // 알림센터 저장
            saveAlarmList(pushType, title, message, webUrl);
            return;
        }

        String notiTitle = title;

        Intent intent = new Intent(this, BackgroundActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Tuda.EXTRA_PUSH_TYPE, pushType);
        intent.putExtra(Tuda.EXTRA_PUSH_PAGE_URL, webUrl);
        PendingIntent pendingIntent = getPendingIntent(this, intent);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
                .setTicker(notiTitle)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setDefaults(getAlarmType())
                .setContentTitle(notiTitle)
                .setContentText(message)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.tuda_launcher_icon));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.mint));
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small_transparent);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.tuda_launcher_icon_small);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Tuda.NOTIFICATION_ID, notificationBuilder.build());

        // 알림센터 저장
        saveAlarmList(pushType, title, message, webUrl);
    }



    /*
     * 기타
     */

    private PendingIntent getPendingIntent(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // 방해 금지 시간 체크
    private boolean isAbleAlarmTime() {
        if (!Preference.getAlarmUseSnooze())
            return true;

        try {
            // 현재 시간 (시분)
            Calendar oCalendar = Calendar.getInstance();
            int nowHour = oCalendar.get(Calendar.HOUR_OF_DAY);
            int nowMin = oCalendar.get(Calendar.MINUTE);
            int now = Integer.parseInt(nowHour + "" + nowMin);

            // 환경설정 금지 시간 (시분)
            String snoozeStartTime = Preference.getSnoozeStartTime();
            String snoozeEndTime = Preference.getSnoozeEndTime();

            String startTime = ("0000".equals(snoozeStartTime)) ? "0" : (snoozeStartTime.startsWith("000")) ? snoozeStartTime.substring(3) : (snoozeStartTime.startsWith("00")) ? snoozeStartTime.substring(2) : (snoozeStartTime.startsWith("0")) ? snoozeStartTime.substring(1) : snoozeStartTime;
            int start = Integer.parseInt(startTime);

            String endTime = ("0000".equals(snoozeEndTime)) ? "0" : (snoozeEndTime.startsWith("000")) ? snoozeEndTime.substring(3) : (snoozeEndTime.startsWith("00")) ? snoozeEndTime.substring(2) : (snoozeEndTime.startsWith("0")) ? snoozeEndTime.substring(1) : snoozeEndTime;
            int end = Integer.parseInt(endTime);

            // 비교
            if (start > end) {
                // 시작시간이 종료시간보다 이후인 경우 (예 : 시작 21시 ~ 종료 08시(다음날))
                if (now >= start || now <= end)
                    return false;
            } else if (start < end ){
                // 시작시간이 종료시간보다 이전인 경우 (예 : 시작 08시 ~ 종료 21시)
                if (now >= start && now <= end)
                    return false;
            } else {
                // 시작시간과 종료시간이 같은 경우 (예 : 시작 08시00분 ~ 종료 08시00분)
                if (now == start)
                    return false;
            }
        } catch (Exception e) {
            // 오류가 발생할 경우에는 시간 체크는 무시
            // 왜? 방해 금지 모드를 켰으므로...
            return false;
        }

        return true;
    }

    // 푸시 알림 방법
    private int getAlarmType() {
        if (!Preference.getAlarmSound() && !Preference.getAlarmVibration())
            return 0;
        else if (Preference.getAlarmSound())
            return Notification.DEFAULT_SOUND;
        else if (Preference.getAlarmVibration())
            return Notification.DEFAULT_VIBRATE;
        else
            return Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
    }

    // 알림센터에 저장
    private void saveAlarmList(String alarmType, String title, String content, String customField) {
        saveAlarmList(alarmType, title, content, customField, false);
    }
    private void saveAlarmList(String alarmType, String title, String content, String customField, Boolean isRead) {
        try {
            String receivedAt = DataManager.getNow();
            if (Constants.PUSH_NORMAL.equals(alarmType))
                isRead = true;

            AlarmItem alarmItem = new AlarmItem();
            alarmItem.setAlarmType(alarmType);
            alarmItem.setTitle(title);
            alarmItem.setContent(content);
            alarmItem.setCustomField(customField);
            alarmItem.setReceivedAt(receivedAt);
            alarmItem.setIsRead(isRead);

            DatabaseManager.getInstance().addAlarmList(alarmItem);
        } catch (Exception e) {}
    }
}
