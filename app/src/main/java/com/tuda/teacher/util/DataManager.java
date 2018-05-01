package com.tuda.teacher.util;

import android.text.TextUtils;

import com.tuda.teacher.type.Gender;
import com.tuda.teacher.network.entity.member.MemberEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DataManager {


    public static ArrayList<Integer> getLessonCount() {

        ArrayList<Integer> lessonCountlist = new ArrayList<Integer>();

        for (int i=1; i<=52; i++) {
            lessonCountlist.add(i);
        }

        return lessonCountlist;
    }

    public static ArrayList<Integer> getScore() {

        ArrayList<Integer> scorelist = new ArrayList<Integer>();

        for (int i=0; i<=5; i++) {
            scorelist.add(i);
        }

        return scorelist;
    }

    public static ArrayList<String> getYears(){
        return getYears(1900, null);
    }
    public static ArrayList<String> getYears(Integer start){
        return getYears(start, null);
    }

    public static ArrayList<String> getYears(Integer start, Integer end) {

        ArrayList<String> yearlist = new ArrayList<String>();

        if (end == null) {
            Calendar cal = Calendar.getInstance();
            end = cal.get(cal.YEAR)+10;
        }

        for (int i=start; i<=end; i++) {
            yearlist.add(String.format("%02d",i));
        }

        return yearlist;
    }
    public static ArrayList<String> getYearsUntilNow(Integer start) {

        Calendar cal = Calendar.getInstance();
        ArrayList<String> yearlist = new ArrayList<String>();

        for (int i=cal.get(cal.YEAR); i>=start; i--) {
            yearlist.add(String.valueOf(i));
        }

        return yearlist;
    }
    public static Integer getYearByAge(Integer age) {

        Calendar cal = Calendar.getInstance();
        return cal.get(cal.YEAR) - age;
    }

    public static ArrayList<String> getMonth(){
        ArrayList<String> monthlist = new ArrayList<String>();

        for (int i=1; i<=12; i++) {
            monthlist.add(String.format("%02d",i));
        }

        return monthlist;
    }

    public static ArrayList<String> getDays(){
        ArrayList<String> daylist = new ArrayList<String>();

        for (int i=1; i<=31; i++) {
            daylist.add(String.format("%02d",i));
        }

        return daylist;
    }

    public static ArrayList<String> getTimes(){
        ArrayList<String> timelist = new ArrayList<String>();

        for (int i=0; i<=23; i++) {
            timelist.add(String.format("%02d",i));
        }

        return timelist;
    }

    public static ArrayList<String> getMinutes(){
        ArrayList<String> minutelist = new ArrayList<String>();

        for (int i=0; i<=55; i+=5) {
            minutelist.add(String.format("%02d",i));
        }

        return minutelist;
    }

    /*
    public static DateTime getToday() {
        return CalendarHelper.convertDateToDateTime(new Date());
    }

    public static DateTime getDateToDateTime(Date date) {
        return CalendarHelper.convertDateToDateTime(date);
    }
    */

    public static String convertDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String getNow() {
        return getNow(FormatManager.DATE_TIME);
    }
    public static String getNow(int type) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = null;
        switch(type) {
            case FormatManager.DATE :
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case FormatManager.PLAIN_DATE :
                sdf = new SimpleDateFormat("yyyyMMdd");
                break;
            case FormatManager.TIME :
                sdf = new SimpleDateFormat("HH:mm");
                break;
            case FormatManager.TIME_SEC :
                sdf = new SimpleDateFormat("HH:mm:ss");
                break;
            case FormatManager.PLAIN_DATE_KOR :
                sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
                break;
            case FormatManager.DATE_TIME :
            default:
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
        }
        return sdf.format(calendar.getTime());
    }
    public static boolean checkMemberRequiredInfo(MemberEntity entity) {
        String nickName = entity.getNickName();
        Gender gender = entity.getGender();
        String mobile = entity.getPhoneNumber();
        String birthday = entity.getBirthDay();

        if (!TextUtils.isEmpty(nickName) && gender != null && !TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(birthday))
            return true;

        return false;
    }

    public static boolean checkMemberInfoOk(MemberEntity entity) {
        String nickName = entity.getNickName();
        Gender gender = entity.getGender();
        String address = entity.getProfileLessonArea1().getStringValue();
        String mobile = entity.getPhoneNumber();
        String birthday = entity.getBirthDay();

        if (!TextUtils.isEmpty(nickName) && gender != null && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(birthday))
            return true;

        return false;
    }
}
