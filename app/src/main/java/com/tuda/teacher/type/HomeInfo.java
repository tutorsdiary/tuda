package com.tuda.teacher.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeInfo {

    private Gender gender;
    private String nickName;
    private String picture;
    private Long marketLikeCount;
    private Boolean isPhoneNumberOk;
    private String noteCount;

    public HomeInfo() {
        this.marketLikeCount = 0L;
        this.isPhoneNumberOk = false;
        this.noteCount = "0";     // 쪽지 안읽은개수
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Long getMarketLikeCount() {
        return marketLikeCount;
    }

    public void setMarketLikeCount(Long marketLikeCount) {
        this.marketLikeCount = marketLikeCount;
    }

    public Boolean getIsPhoneNumberOk() {
        return isPhoneNumberOk;
    }

    public void setIsPhoneNumberOk(Boolean isPhoneNumberOk) {
        this.isPhoneNumberOk = isPhoneNumberOk;
    }

    public String getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(String noteCount) {
        this.noteCount = noteCount;
    }
}
