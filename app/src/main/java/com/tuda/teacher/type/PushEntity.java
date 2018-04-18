package com.tuda.teacher.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PushEntity {
    private String pushKey;
    private Boolean useOk;

    public PushEntity() {
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public Boolean getUseOk() {
        return useOk;
    }

    public void setUseOk(Boolean useOk) {
        this.useOk = useOk;
    }
}
