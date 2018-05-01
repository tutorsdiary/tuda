package com.tuda.teacher.util.filter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

public class NumberFormatWatcher implements TextWatcher {

    String result = "";
    DecimalFormat df = new DecimalFormat("###,###.###");
    EditText view;

    public NumberFormatWatcher(EditText view) {
        this.view = view;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!s.toString().equals(result) && s.toString().length() > 0){     // StackOverflow를 막기위해,
            result = df.format(Long.parseLong(s.toString().replaceAll(",", "")));   // 에딧텍스트의 값을 변환하여, result에 저장.
            view.setText(result);    // 결과 텍스트 셋팅.
            view.setSelection(result.length());     // 커서를 제일 끝으로 보냄.
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}