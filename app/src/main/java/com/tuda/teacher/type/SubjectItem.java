package com.tuda.teacher.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tuda.teacher.type.StudentItem;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectItem {
    private Long studentSrl;
    private String subject;
    private String subjectEtc;
    private Boolean studyClose;    // true - 종료, false - 오픈 상태

    public SubjectItem() {

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

    public String getSubjectEtc() {
        return subjectEtc;
    }

    public void setSubjectEtc(String subjectEtc) {
        this.subjectEtc = subjectEtc;
    }

    public Boolean getStudyClose() {
        return studyClose;
    }

    public void setStudyClose(Boolean studyClose) {
        this.studyClose = studyClose;
    }
}
