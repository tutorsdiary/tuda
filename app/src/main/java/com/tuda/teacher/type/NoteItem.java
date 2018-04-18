package com.tuda.teacher.type;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoteItem {
    private Long receiverSrl;           // 수신자 회원 시리얼
    private String description;         // 쪽지 내용

    public NoteItem() {

    }

    public Long getReceiverSrl() {
        return receiverSrl;
    }

    public void setReceiverSrl(Long receiverSrl) {
        this.receiverSrl = receiverSrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
