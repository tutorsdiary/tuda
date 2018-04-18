package com.tuda.teacher.type;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeEntity extends ChargeItem {
    private Long srl;
    private Long memberSrl;
    private String createdAt;
    private String upddatedAt;

    public ChargeEntity() {
        
    }

    public Long getSrl() {
        return srl;
    }

    public void setSrl(Long srl) {
        this.srl = srl;
    }

    public Long getMemberSrl() {
        return memberSrl;
    }

    public void setMemberSrl(Long memberSrl) {
        this.memberSrl = memberSrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return upddatedAt;
    }

    public void setUpdatedAt(String upddatedAt) {
        this.upddatedAt = upddatedAt;
    }
}
