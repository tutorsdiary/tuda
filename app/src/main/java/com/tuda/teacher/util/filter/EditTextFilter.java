package com.tuda.teacher.util.filter;

import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;


public class EditTextFilter {
    public static InputFilter EMOJI_FILTER = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {
                int type = Character.getType(source.charAt(index));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    };


}
