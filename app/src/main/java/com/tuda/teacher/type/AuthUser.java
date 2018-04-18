package com.tuda.teacher.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUser {
    String auth_email;
    String auth_hash;

    public AuthUser(String email, String passwd) {
        this.auth_email = email;
        this.auth_hash = passwd;
    }
}
