package com.tuda.teacher.type;


import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeItem {
    private Long studentSrl;
    private String subject;
    private Integer charge;
    private String paid;
    private String payHow;
    private String chargeDay;
    private String memo;

    public ChargeItem() {

    }

    public Long getStudentSrl() {
        return studentSrl;
    }

    public void setStudentSrl(Long studentSrl) {
        this.studentSrl = studentSrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPayHow() {
        return payHow;
    }

    public void setPayHow(String payHow) {
        this.payHow = payHow;
    }

    public String getChargeDay() {
        return chargeDay;
    }

    public void setChargeDay(String chargeDay) {
        this.chargeDay = (!TextUtils.isEmpty(chargeDay)) ? chargeDay + " 00:00:00" : chargeDay;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
