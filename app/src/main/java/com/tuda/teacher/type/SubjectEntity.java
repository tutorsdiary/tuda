package com.tuda.teacher.type;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectEntity extends SubjectItem {
    private Long srl;
    private Long memberSrl;

    public SubjectEntity() {

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
}
