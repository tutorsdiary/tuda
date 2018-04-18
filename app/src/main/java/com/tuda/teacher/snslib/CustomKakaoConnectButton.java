package com.tuda.teacher.snslib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.kakao.usermgmt.LoginButton;
import com.tuda.teacher.R;

public class CustomKakaoConnectButton extends LoginButton {

    public CustomKakaoConnectButton(Context context) {
        super(context);
    }

    public CustomKakaoConnectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomKakaoConnectButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        View v = inflate(this.getContext(), R.layout.kakao_connect_layout, this);
    }
}
