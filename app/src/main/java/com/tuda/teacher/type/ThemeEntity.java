package com.tuda.teacher.type;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ThemeEntity {
    private Long srl;
    private String title;
    private String thumImg;
    private String orgImg;

    public ThemeEntity() {
    }

    public Long getSrl() {
        return srl;
    }

    public void setSrl(Long srl) {
        this.srl = srl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumImg() {
        return thumImg;
    }

    public void setThumImg(String thumImg) {
        this.thumImg = thumImg;
    }

    public String getOrgImg() {
        return orgImg;
    }

    public void setOrgImg(String orgImg) {
        this.orgImg = orgImg;
    }
}
