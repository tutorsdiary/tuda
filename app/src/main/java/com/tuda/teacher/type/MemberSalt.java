package com.tuda.teacher.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberSalt {
    private String memberSalt;

    public MemberSalt() {
    }

    public String getMemberSalt() {
        return memberSalt;
    }

    public void setMemberSalt(String memberSalt) {
        this.memberSalt = memberSalt;
    }
}
