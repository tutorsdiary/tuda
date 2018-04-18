package com.tuda.teacher.type;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FuncRequestEntity extends FuncRequestItem {
    private Long srl;
    private Long memberSrl;
    private String answer;
    private String createdAt;
    private String answerAt;

    public FuncRequestEntity() {

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAnswerAt() {
        return answerAt;
    }

    public void setAnswerAt(String answerAt) {
        this.answerAt = answerAt;
    }
}
