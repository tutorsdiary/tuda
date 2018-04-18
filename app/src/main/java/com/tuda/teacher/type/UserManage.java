package com.tuda.teacher.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserManage {
    String option;
    String auth_email;
    String auth_hash;
    String update_auth_email;
    String update_auth_hash;

    public UserManage(String option, String email, String passwd) {
        this.option = option;
        this.auth_email = email;
        this.auth_hash = passwd;
        this.update_auth_email = null;
        this.update_auth_hash = null;
    }

    public void setUpdateAuthEmail(String update_auth_email) {
        this.update_auth_email = update_auth_email;
    }

    public void setUpdateAuthHash(String update_auth_hash) {
        this.update_auth_hash = update_auth_hash;
    }
}
