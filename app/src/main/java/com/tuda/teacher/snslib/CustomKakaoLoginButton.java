package com.tuda.teacher.snslib;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.tuda.teacher.R;
import com.tuda.teacher.ui.activity.LoginActivity;

import java.util.List;

public class CustomKakaoLoginButton extends LoginButton {

    private Context mContext;
    private String mTitle = null;
    private TextView txt_title;

    public CustomKakaoLoginButton(Context context) {
        super(context);

        mContext = context;
    }

    public CustomKakaoLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomKakaoLoginButton);
        mTitle = typeArray.getString(R.styleable.CustomKakaoLoginButton_buttonTitle);
    }

    public CustomKakaoLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomKakaoLoginButton);
        mTitle = typeArray.getString(R.styleable.CustomKakaoLoginButton_buttonTitle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        inflate(this.getContext(), R.layout.kakao_login_layout, this);
    }
}
