package com.tuda.teacher.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMail {
    String option;
    String auth_email;
    String auth_hash;
    String mail_sender_name;
    String mail_sender_email;
    String mail_receiver_name;
    String mail_receiver_email;
    String mail_title;
    String mail_content;

    public UserMail(String option, String email, String passwd) {
        this.option = option;
        this.auth_email = email;
        this.auth_hash = passwd;
        this.mail_sender_name = null;
        this.mail_sender_email = null;
        this.mail_receiver_name = null;
        this.mail_receiver_email = null;
        this.mail_title = null;
        this.mail_content = null;
    }

    public void setMailSender(String mail_sender_name, String mail_sender_email) {
        this.mail_sender_name = mail_sender_name;
        this.mail_sender_email = mail_sender_email;
    }

    public void setMailReceiver(String mail_receiver_name, String mail_receiver_email) {
        this.mail_receiver_name = mail_receiver_name;
        this.mail_receiver_email = mail_receiver_email;
    }

    public void setMailContent(String mail_title, String mail_content) {
        this.mail_title = mail_title;
        this.mail_content = mail_content;
    }
}
