package com.tuda.teacher.util;

import android.text.TextUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class FormatManager {
    public static final int DATE = 1;
    public static final int TIME = 2;
    public static final int TIME_SEC = 3;
    public static final int DATE_TIME = 4;
    public static final int PLAIN_DATE = 5;
    public static final int PLAIN_DATE_KOR = 6;

    public static String number_format(int n) {
        return NumberFormat.getNumberInstance().format(n);
    }

    public static Date StringToDate(String datetime) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return transFormat.parse(datetime);
        } catch (ParseException e) {
        }
        return null;
    }

    public static String date_format(Date date) {
        return date_format(FormatManager.DATE_TIME, date);
    }

    public static String date_format(String format, Date date) {
        SimpleDateFormat sdf = null;

        try {
            sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
        }

        return "";
    }

    public static String date_format(int type, Long millisec) {
        return date_format(type, new Date(millisec));
    }

    public static String date_format(int type, Date date) {
        SimpleDateFormat sdf = null;

        try {
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
            return sdf.format(date);
        } catch (Exception e) {
        }

        return "";
    }

    public static String getDayOfWeek(int dayNum) {
        String dayOfWeek = null;

        switch(dayNum){
            case 2:
                dayOfWeek = "월";
                break ;
            case 3:
                dayOfWeek = "화";
                break ;
            case 4:
                dayOfWeek = "수";
                break ;
            case 5:
                dayOfWeek = "목";
                break ;
            case 6:
                dayOfWeek = "금";
                break ;
            case 7:
                dayOfWeek = "토";
                break ;
            default:
                dayOfWeek = "일";
                break ;
        }
        dayOfWeek += "요일";

        return dayOfWeek ;
    }

    public static String convertDateTimeToAmPm(String createdAt) {
        return convertDateTimeToAmPm(createdAt, "full");
    }
    public static String convertDateTimeToAmPm(String createdAt, String mode) {
        String[] dateInfos = createdAt.split(" ");
        String dateYear = "";
        String dateHour = "";
        String year = "";
        String month = "";
        String day = "";
        Integer hour = 0;
        String min = "";
        String sec = "";
        if (dateInfos.length == 2) {
            dateYear = dateInfos[0];
            String[] dateYearInfo = dateYear.split("-");
            year = dateYearInfo[0];
            month = dateYearInfo[1];
            day = dateYearInfo[2];

            dateHour = dateInfos[1];
            hour = Integer.parseInt(dateHour.substring(0, 2));
            min = dateHour.substring(2, 5);
            sec = dateHour.substring(5);
        }

        String am_pm = "";
        int am_pm_hour = 0;
        if (hour < 12) {
            am_pm = "오전";
            am_pm_hour = (hour == 0) ? 12 : hour;
        } else {
            am_pm = "오후";
            am_pm_hour = ((hour - 12) == 0) ? 12 : hour - 12;
        }

        return year + "년 " + month + "월 " + day + "일 " + am_pm + " " + am_pm_hour + min + (("full".equals(mode)) ? sec : "");
    }

    public static String getOnlyNumberString(String str){
        if(str == null) return str;

        StringBuffer sb = new StringBuffer();
        int length = str.length();
        for(int i = 0 ; i < length ; i++){
            char curChar = str.charAt(i);
            if(Character.isDigit(curChar)) sb.append(curChar);
        }

        return sb.toString();
    }

    public static String convertToHmsFromSec(double sec) {
        if (sec <= 0)
            return "00";

        double hour = (sec >= (60 * 60)) ? Math.floor(sec / (60 * 60)) : 0;

        double leftSec1 = sec - (hour * 60 * 60);
        double minute = (leftSec1 >= 60) ? Math.floor(leftSec1 / 60) : 0;

        double second = leftSec1 - (minute * 60);

        if (hour > 0)
            return String.format("%02d:%02d:%02d", (int)hour, (int)minute, (int)second);
        else if (minute > 0)
            return String.format("%02d:%02d", (int)minute, (int)second);
        else
            return String.format("%02d", (int)second);
    }

    public static String convertTimeToAtTime(String datetime) {
        try {
            // 비교 시간
            Date atDate = StringToDate(datetime);
            long atTime = atDate.getTime();

            // 현재 시간
            long current = System.currentTimeMillis();

            // 시간 차이
            long gapTime = current - atTime;

            if (gapTime <= (1 * 60 * 60 * 4)) {             // 4시간 이내
                return "방금전";
            } else {
                Calendar cal = Calendar.getInstance();

                // 오늘
                String nowDay = String.format("%d%02d%02d", cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1), cal.get(Calendar.DAY_OF_MONTH));
                if (nowDay.equals(date_format(FormatManager.PLAIN_DATE, atDate)))
                    return "오늘";

                // 어제
                cal.add(Calendar.DATE, -1);
                String prevDay = String.format("%d%02d%02d", cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1), cal.get(Calendar.DAY_OF_MONTH));
                if (prevDay.equals(date_format(FormatManager.PLAIN_DATE, atDate)))
                    return "어제";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "언젠가";
    }

    public static int countLineBreak(String str) {
        if (TextUtils.isEmpty(str))
            return 0;

        String[] infos = str.split(System.getProperty("line.separator"));
        return infos.length - 1;
    }
}
