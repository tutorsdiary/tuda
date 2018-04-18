package com.tuda.teacher.type;

public enum UserType {
    STUDENT("STUDENT"),     // 학생+학부모
    TEACHER("TEACHER"),     // 선생님
    PARTNER("PARTNER"),     // 기업
    ALL("ALL"),             // 분류없음
    OPERTATOR("OPERATOR"),  // 운영자
    TESTER("TESTER"),       // 테스터
    ADMIN("ADMIN");         // 최고관리자

    private String type;

    UserType(String type) {
        this.type = type;
    }

    public String getValue() {
        return type;
    }

    public void setValue(String type) {
        this.type = type;
    }

}
