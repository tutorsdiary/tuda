package com.tuda.teacher.type;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataFile {
    String option;
    String auth_email;
    String auth_hash;
    File file;

    public DataFile(String option, String email, String passwd) {
        this.option = option;
        this.auth_email = email;
        this.auth_hash = passwd;
        this.file = null;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
