package com.tuda.teacher.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigInfo {
    private Boolean auto_login;
    private Boolean auto_lock;
    private Boolean alarm_use_service;
    private Boolean alarm_use_talk;
    private Boolean alarm_use_note;
    private Boolean alarm_use_snooze;
    private Boolean alarm_use_lesson;
    private String alarm_time;
    private String auto_lock_password;
    private String snooze_start_time;
    private String snooze_end_time;
    private Boolean alarm_sound;
    private Boolean alarm_vibration;

    public Boolean getAutoLogin() {
        return auto_login;
    }

    public void setAutoLogin(Boolean auto_login) {
        this.auto_login = auto_login;
    }

    public Boolean getAutoLock() {
        return auto_lock;
    }

    public void setAutoLock(Boolean auto_lock) {
        this.auto_lock = auto_lock;
    }

    public Boolean getAlarmUseService() {
        return alarm_use_service;
    }

    public void setAlarmUseService(Boolean alarm_use_service) {
        this.alarm_use_service = alarm_use_service;
    }

    public Boolean getAlarmUseTalk() {
        return alarm_use_talk;
    }

    public void setAlarmUseTalk(Boolean alarm_use_talk) {
        this.alarm_use_talk = alarm_use_talk;
    }

    public Boolean getAlarmUseNote() {
        return alarm_use_note;
    }

    public void setAlarmUseNote(Boolean alarm_use_note) {
        this.alarm_use_note = alarm_use_note;
    }

    public Boolean getAlarmUseSnooze() {
        return alarm_use_snooze;
    }

    public void setAlarmUseSnooze(Boolean alarm_use_snooze) {
        this.alarm_use_snooze = alarm_use_snooze;
    }

    public Boolean getAlarmUseLesson() {
        return alarm_use_lesson;
    }

    public void setAlarmUseLesson(Boolean alarm_use_lesson) {
        this.alarm_use_lesson = alarm_use_lesson;
    }

    public String getAlarmTime() {
        return alarm_time;
    }

    public void setAlarmTime(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getAutoLockPassword() {
        return auto_lock_password;
    }

    public void setAutoLockPassword(String auto_lock_password) {
        this.auto_lock_password = auto_lock_password;
    }

    public String getSnoozeStartTime() {
        return snooze_start_time;
    }

    public void setSnoozeStartTime(String snooze_start_time) {
        this.snooze_start_time = snooze_start_time;
    }

    public String getSnoozeEndTime() {
        return snooze_end_time;
    }

    public void setSnoozeEndTime(String snooze_end_time) {
        this.snooze_end_time = snooze_end_time;
    }

    public Boolean getAlarmSound() {
        return alarm_sound;
    }

    public void setAlarmSound(Boolean alarm_sound) {
        this.alarm_sound = alarm_sound;
    }

    public Boolean getAlarmVibration() {
        return alarm_vibration;
    }

    public void setAlarmVibration(Boolean alarm_vibration) {
        this.alarm_vibration = alarm_vibration;
    }
}
