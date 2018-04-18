package com.tuda.teacher.type;

public enum AuthType {
    EMAIL("EMAIL"),
    FACEBOOK("FACEBOOK"),
    NAVER("NAVER"),
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE");

    private String value;

    AuthType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(AuthType authType) {
        if (authType == null)
            return false;

        if (this.getValue().equals(authType.getValue()))
            return true;
        return false;
    }
}
