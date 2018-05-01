package com.tuda.teacher.util;

import android.content.Context;
import android.text.TextUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FormManager {

    /**
     * 공백을 제거한후 문자열의 공백여부 검사
     */
    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str))
            return true;

        return TextUtils.isEmpty(str.replaceAll("\\p{Space}", ""));
    }

    /**
     * 문자열의 공백 포함여부 검사
     */
    public static boolean isSpace(String str) {
        if (TextUtils.isEmpty(str))
            return true;

        for(int i = 0 ; i < str.length() ; i++)
        {
            if(str.charAt(i) == ' ')
                return true;
        }
        return false;
    }

    /**
     * 숫자 모양에 대한 형식 검사
     */
    public static boolean isNum(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^[0-9]*$", str);
    }

    /**
     * 영문으로만 구성되었는지에 대한 형식 검사
     */
    public static boolean isEng(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^[a-zA-Z]*$", str);
    }

    /**
     * 한글로만 구성되었는지에 대한 형식 검사
     */
    public static boolean isKor(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^[ㄱ-ㅎ가-힣]*$", str);
    }

    /**
     * 영문과 숫자로만 구성되었는지에 대한 형식 검사
     */
    public static boolean isEngNum(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^[a-zA-Z0-9]*$", str);
    }

    /**
     * 한글과 숫자로만 구성되었는지에 대한 형식 검사
     */
    public static boolean isKorNum(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^[ㄱ-ㅎ가-힣0-9]*$", str);
    }

    /**
     * (닉)네임에 대한 형식 검사
     * 단, 최소길이는 2
     */
    public static boolean checkNameRule(String str, int min, int max) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^([a-zA-Z가-힣]{1}.{"+(min-1)+","+max+"})$", str);
    }

    /**
     * 영문,한글,숫자로만 구성되었는지에 대한 형식 검사
     * 단, 최소길이는 2
     */
    public static boolean checkNicknameRule(String str, int min, int max) {
        return FormManager.checkNicknameRule(str, min, max, false);
    }

    public static boolean checkNicknameRule(String str, int min, int max, boolean useNumberFirst) {
        if (TextUtils.isEmpty(str))
            return false;

        // 첫자에 숫자허용 안함.
        if (useNumberFirst == false)
            return Pattern.matches("^([a-zA-Z가-힣]{1}[a-zA-Z가-힣0-9]{"+(min-1)+","+max+"})$", str);

        return Pattern.matches("^([a-zA-Z가-힣0-9]{"+min+","+max+"})$", str);
    }


    /**
     * (닉)네임에 대한 형식 검사
     * 단, 최소길이는 2
     */
    public static boolean isKorEng(String str, int min, int max) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^([a-zA-Zㄱ-ㅎ가-힣]{1}.{"+(min-1)+","+max+"})$", str);
    }

    /**
     * 영문,한글,숫자로만 구성되었는지에 대한 형식 검사
     * 단, 최소길이는 2
     */
    public static boolean isKorEngNum(String str, int min, int max) {
        return FormManager.isKorEngNum(str, min, max, false);
    }

    public static boolean isKorEngNum(String str, int min, int max, boolean useNumberFirst) {
        if (TextUtils.isEmpty(str))
            return false;

        // 첫자에 숫자허용 안함.
        if (useNumberFirst == false)
            return Pattern.matches("^([a-zA-Zㄱ-ㅎ가-힣]{1}[a-zA-Zㄱ-ㅎ가-힣0-9]{"+(min-1)+","+max+"})$", str);

        return Pattern.matches("^([a-zA-Zㄱ-ㅎ가-힣0-9]{"+min+","+max+"})$", str);
    }

    /**
     * 이메일 형식인지에 대한 검사 --> "아이디@도메인"의 형식을 충족해야 한다.
     */
    public static boolean isEmail(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$", str);
    }

    /**
     * 휴대폰번호인지에 대한 형식검사.
     * 반드시 앞자리가 010,010,016~9사이를 충족해야 하며,
     * 각 연결부는 "-"로 구분되어야 한다. 각 부분에 대한 자리수도 충족시켜야 한다.
     */
    public static boolean isMobile(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^01(?:0|1|[6-9])-\\d{3,4}-\\d{4}$", str);
    }

    /**
     * 전화번호인지에 대한 형식검사. 각 연결부는 "-"로 구분되어야 한다.
     * 각 부분에 대한 자리수도 충족시켜야 한다.
     */
    public static boolean isTel(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$", str);
    }

    /**
     * 전화번호에 사용가능한 문자 검사. 0~9 와 - 만 허용
     */
    public static boolean isPhoneNumber(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^[0-9-]*$", str);
    }

    /**
     * 주민번호에 대한 글자수 및 뒷자리 첫글자가 1~4의 범위에 있는지에 대한 검사
     */
    public static boolean isJumin(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("^\\d{6}-[1-4]\\d{6}", str);
    }

    /**
     * 아이피주소 형식에 대한 검사
     */
    public static boolean isIP(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        return Pattern.matches("([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})", str);
    }

    /**
     * URL 형식에 대한 검사
     */
    public static boolean isURL(String str) {
        if (TextUtils.isEmpty(str))
            return false;

        try {
            URL u = new URL(str);
            u.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * integer 숫자 범위 체크 (양수일 경우)
     */
    public static boolean isInt(String num) {
        try {
            return isInt(Integer.parseInt(num));
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isInt(int num) {
        if (num >= 0 && num <= 2000000000)
            return true;
        else
            return false;
    }

    /**
     * 특정 item 의 string-array 내 해당 array index 구하기
     */
    public static int getSpinnerItemPosition(Context context, int textArrayResId, String item, Integer defVal) {
        String[] arrayResource = context.getResources().getStringArray(textArrayResId);
        return FormManager.getSpinnerItemPosition(arrayResource, item, defVal);
    }

    public static<T> int getSpinnerItemPosition(ArrayList<T> arrayResource, T item, Integer defVal) {
        return FormManager.getSpinnerItemPosition(arrayResource.toArray(), item, defVal);
    }

    public static int getSpinnerItemPosition(Context context, int textArrayResId, String item) {
        String[] arrayResource = context.getResources().getStringArray(textArrayResId);
        return FormManager.getSpinnerItemPosition(arrayResource, item, 0);
    }

    public static<T> int getSpinnerItemPosition(ArrayList<T> arrayResource, T item) {
        return FormManager.getSpinnerItemPosition(arrayResource.toArray(), item, 0);
    }

    public static<T> int getSpinnerItemPosition(T[] arrayResource, T item, Integer defVal) {
        int idx = 0;
        if (item == null)
            return 0;

        for(T resItem : arrayResource) {
            if (item.equals(resItem)) {
                return idx;
            }
            idx++;
        }

        if (defVal == null || defVal == -1)
            return -1;

        return defVal;
    }
}
