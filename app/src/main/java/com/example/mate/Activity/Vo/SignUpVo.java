package com.example.mate.Activity.Vo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by 가애 on 2018-01-30.
 */

@IgnoreExtraProperties
public class SignUpVo {

    public String email;
    public String password;
    public String nickname;
    public String gender;
    public String birth;
    public String phone_num;
    public PartnerVo partnerVo;
    public String connectYN;
    public String groupID;

    public SignUpVo() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public PartnerVo getPartnerVo() {
        return partnerVo;
    }

    public void setPartnerVo(PartnerVo partnerVo) {
        this.partnerVo = partnerVo;
    }

    public String getConnectYN() {
        return connectYN;
    }

    public void setConnectYN(String connectYN) {
        this.connectYN = connectYN;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}
