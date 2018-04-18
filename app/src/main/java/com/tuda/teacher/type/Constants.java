package com.tuda.teacher.type;

public class Constants {
    // 회원가입수단
    public static final String EMAIL = "EMAIL";
    public static final String FACEBOOK = "FACEBOOK";
    public static final String KAKAO = "KAKAO";
    public static final String NAVER = "NAVER";
    public static final String GOOGLE = "GOOGLE+";

    // 에러코드
    public static final Integer ERROR_DEFAULT = -1;
    public static final Integer ERROR_REGIST = 0;
    public static final Integer ERROR_DATA = 1;
    public static final Integer ERROR_NETWORK = 2;
    public static final Integer ERROR_TOKEN = 3;
    public static final Integer ERROR_NO_MEMBER = 4;
    public static final Integer ERROR_DUPLICATE_MEMBER = 5;
    public static final Integer ERROR_LOGIN_FACEBOOK = 6;
    public static final Integer ERROR_LOGIN_KAKAO = 7;
    public static final Integer ERROR_LOGIN_NAVER = 8;
    public static final Integer ERROR_IMAGE_DATA = 9;
    public static final Integer ERROR_IMAGE_UPLOAD = 10;
    public static final Integer ERROR_BAN_WORDS = 11;
    public static final Integer ERROR_MARKET_OWNER_NONE = 12;
    public static final Integer ERROR_NOTICE = 13;

    // Glide 라이브러리 - 이미지 Transform
    public static final Integer GLIDE_DEFAULT = 0;
    public static final Integer GLIDE_CIRCLE = 1;
    public static final Integer GLIDE_ROUND = 2;
    public static final Integer GLIDE_MULTI = 3;
    public static final Integer GLIDE_CENTER_CROP = 4;

    // 푸시 종류
    public static final String PUSH_TOPIC = "topic";
    public static final String PUSH_NORMAL = "norm";
    public static final String PUSH_NOTE = "note";
    public static final String PUSH_TALK = "talk";
    public static final String PUSH_TALK_SECRET = "talk_secret";
    // topic 하위 푸시 종류
    public static final String PUSH_NOTICE = "noti";
    public static final String PUSH_WEB = "web";
    public static final String PUSH_TALK_OPEN = "talk_open";

    // 잡마켓 정렬 모드
    public static final String MARKET_SORT_FULL = "full";
    public static final String MARKET_SORT_NORMAL = "normal";
    public static final String MARKET_SORT_SEARCH = "search";
    public static final String MARKET_SORT_NONE = "none";
}
