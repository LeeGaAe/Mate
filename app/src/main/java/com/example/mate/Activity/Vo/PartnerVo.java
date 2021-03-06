package com.example.mate.Activity.Vo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by 가애 on 2018-02-03.
 */

@IgnoreExtraProperties
public class PartnerVo {

    public String part_name;
    public String part_phone_num;
    public String part_birth;
    public String part_email;
    public String part_fcmToken;
    public String part_uid;
    public String part_profile;


    public PartnerVo() {

    }

    public String getPart_name() {
        return part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public String getPart_phone_num() {
        return part_phone_num;
    }

    public void setPart_phone_num(String part_phone_num) {
        this.part_phone_num = part_phone_num;
    }

    public String getPart_birth() {
        return part_birth;
    }

    public void setPart_birth(String part_birth) {
        this.part_birth = part_birth;
    }

    public String getPart_email() {
        return part_email;
    }

    public void setPart_email(String part_email) {
        this.part_email = part_email;
    }

    public String getPart_fcmToken() {
        return part_fcmToken;
    }

    public void setPart_fcmToken(String part_fcmToken) {
        this.part_fcmToken = part_fcmToken;
    }

    public String getPart_uid() {
        return part_uid;
    }

    public void setPart_uid(String part_uid) {
        this.part_uid = part_uid;
    }

    public String getPart_profile() {
        return part_profile;
    }

    public void setPart_profile(String part_profile) {
        this.part_profile = part_profile;
    }
}
