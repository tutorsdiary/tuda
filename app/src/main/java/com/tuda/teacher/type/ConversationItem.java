package com.tuda.teacher.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationItem {

    private Long studentSrl;
    private Date conversationDay;
    private String memo;

    public ConversationItem() {

    }

    public void setStudentSrl(Long studentSrl) {
        this.studentSrl = studentSrl;
    }

    public Long getStudentSrl() {
        return this.studentSrl;
    }

    public void setConversationDay(Date conversationDay) {
        this.conversationDay = conversationDay;
    }

    public Date getConversationDay() {
        return this.conversationDay;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}