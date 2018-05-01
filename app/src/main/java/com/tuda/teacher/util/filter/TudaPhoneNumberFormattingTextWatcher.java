package com.tuda.teacher.util.filter;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Selection;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Jeon on 2016-10-26.
 */

public class TudaPhoneNumberFormattingTextWatcher extends PhoneNumberFormattingTextWatcher {

    public TudaPhoneNumberFormattingTextWatcher() {
        super();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        super.beforeTextChanged(s, start, count, after);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);

    }

    boolean isBackSpace = false;

    @Override
    public synchronized void afterTextChanged(Editable s) {
        if (Pattern.matches("(050.{1})(.*)", s.toString().replace("-", ""))) {
            if (Selection.getSelectionEnd(s) == 14) {
                isBackSpace = true;
            }

            if(!isBackSpace) {
                if ((s.length() == 4 || s.length() == 9)) {
                    s.append("-");
                }
            }
        } else {
            super.afterTextChanged(s);
        }

        isBackSpace = false;

    }
}
