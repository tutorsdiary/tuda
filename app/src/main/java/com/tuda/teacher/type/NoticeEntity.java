package com.tuda.teacher.type;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoticeEntity {
    private Long srl;
    private String title;
    private String content;
    private Integer viewCount;
    private String createdAt;

    public NoticeEntity() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
