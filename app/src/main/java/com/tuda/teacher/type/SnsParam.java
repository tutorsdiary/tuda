package com.tuda.teacher.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SnsParam {
    public String mode;
    public AuthType authType;
    public String authKey;
    public String email;
    public Gender gender;
    public String birthday;
    public String token;
    public Integer errorType;
}