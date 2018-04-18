package com.tuda.teacher.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppInfo {
    private String updateVersion;
    private Boolean isForceUpdate;
    private String updateMessage;

    private String startEventUrl;

    private String endAdUrl;

    private String time;

    public AppInfo() {

    }

    public String getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(String updateVersion) {
        this.updateVersion = updateVersion;
    }

    public Boolean getIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(Boolean isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getStartEventUrl() {
        return startEventUrl;
    }

    public void setStartEventUrl(String startEventUrl) {
        this.startEventUrl = startEventUrl;
    }

    public String getEndAdUrl() {
        return endAdUrl;
    }

    public void setEndAdUrl(String endAdUrl) {
        this.endAdUrl = endAdUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
