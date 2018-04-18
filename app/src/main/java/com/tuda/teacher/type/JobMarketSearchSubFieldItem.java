package com.tuda.teacher.type;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobMarketSearchSubFieldItem {
    private String si;
    private String gu;
    private String recruitSector;

    public JobMarketSearchSubFieldItem() {
    }

    public String getSi() {
        return si;
    }

    public void setSi(String si) {
        this.si = si;
    }

    public String getGu() {
        return gu;
    }

    public void setGu(String gu) {
        this.gu = gu;
    }

    public String getRecruitSector() {
        return recruitSector;
    }

    public void setRecruitSector(String recruitSector) {
        this.recruitSector = recruitSector;
    }
}
