package com.tuda.teacher.type;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchEntity extends SearchItem {
    private Long srl;
    private String menuAlias;

    private Long memberSrl;
    private String createdAt;

    private String title;
    private String url;
    private Integer viewCount;
    private String copySource;
    private Long copySrl;

    public SearchEntity() {

    }

    public Long getSrl() {
        return srl;
    }

    public void setSrl(Long srl) {
        this.srl = srl;
    }

    public String getMenuAlias() {
        return menuAlias;
    }

    public void setMenuAlias(String menuAlias) {
        this.menuAlias = menuAlias;
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



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getCopySource() {
        return copySource;
    }

    public void setCopySource(String copySource) {
        this.copySource = copySource;
    }

    public Long getCopySrl() {
        return copySrl;
    }

    public void setCopySrl(Long copySrl) {
        this.copySrl = copySrl;
    }
}
