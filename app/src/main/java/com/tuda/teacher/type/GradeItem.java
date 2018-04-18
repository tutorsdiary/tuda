package com.tuda.teacher.type;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GradeItem {
    private Long studentSrl;
    private String schoolType;
    private Integer schoolYear;
    private Integer term;
    private String examType;
    private String examTypeEtc;
    private String subject;
    private Float point;
    private Float avgPoint;
    private Integer grade;
    private Integer avgGrade;
    private String memo;

    public GradeItem() {

    }

    public Long getStudentSrl() {
        return studentSrl;
    }

    public void setStudentSrl(Long studentSrl) {
        this.studentSrl = studentSrl;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public Integer getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(Integer schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getExamTypeEtc() {
        return examTypeEtc;
    }

    public void setExamTypeEtc(String examTypeEtc) {
        this.examTypeEtc = examTypeEtc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public Float getAvgPoint() {
        return avgPoint;
    }

    public void setAvgPoint(Float avgPoint) {
        this.avgPoint = avgPoint;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getAvgGrade() {
        return avgGrade;
    }

    public void setAvgGrade(Integer avgGrade) {
        this.avgGrade = avgGrade;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
