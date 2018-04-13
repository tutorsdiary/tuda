package com.tuda.teacher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.tuda.teacher.util.DataManager;
import com.tuda.teacher.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by crazyluv on 2016. 11. 21..
 */

public class SMSReceiver extends BroadcastReceiver {
    final static String TAG = "SMSReceiver";
    // 지정한 특정 액션이 일어나면 수행되는 메서드

    public interface SMSListener {
        void updateSMS(String authNum);
    }

    private SMSListener smsListener;

    public SMSReceiver(SMSListener listener) {
        smsListener = listener;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            Log.e(TAG, "sms received - " + DataManager.getNow());

            // bundle
            Bundle bundle = intent.getExtras();
            if (bundle == null)
                return;

            // pdu
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            if (pdusObj == null)
                return;

            // sms
            String message_body = "";
            SmsMessage[] smsMessages = new SmsMessage[pdusObj.length];
            for (int i = 0; i < pdusObj.length; i++) {
                smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                message_body = smsMessages[i].getMessageBody().toString();
            }

            // 메세지 포맷 - '[TUDA] 회원가입 인증번호는 [111111] 입니다. 수신번호란에 입력해주세요.'
            if (message_body.contains("[TUDA] 회원가입 인증번호")) {
                // 인증번호 입력
                String authNum = getAuthNumber(message_body.trim());
                if (!TextUtils.isEmpty(authNum) && authNum.length() == 6 && smsListener != null)
                    smsListener.updateSMS(authNum);
            }
        }
    }

    private String getAuthNumber(String smsMsg) {
        smsMsg = smsMsg.trim().substring(8);

        String regex = ".*인증번호는\\s\\[(.*)\\]\\s입니다.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(smsMsg);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        return "";
    }

}
