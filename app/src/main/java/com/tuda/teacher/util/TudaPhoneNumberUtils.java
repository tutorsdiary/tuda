package com.tuda.teacher.util;

import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by Jeon on 2016-10-24.
 */

public class TudaPhoneNumberUtils extends PhoneNumberUtils{

    public static String formatNumber(String source) {
        if (TextUtils.isEmpty(source))
            return source;

        boolean isMatches = Pattern.matches("(050.{1})-?([0-9]{3,4})-?([0-9]{4})", source);


        if (isMatches) { //안심번호 이면 050X
            return source.replaceAll("(050.{1})-?([0-9]{3,4})-?([0-9]{4})", "$1-$2-$3");
        } else {
            return PhoneNumberUtils.formatNumber(source);
        }
    }
}
