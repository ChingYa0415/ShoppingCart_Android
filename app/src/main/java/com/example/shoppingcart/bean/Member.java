package com.example.shoppingcart.bean;

import java.sql.Timestamp;

public class Member {
    private int id;
    private String account;
    private String password;
    private String nickname;
    private int status;
    private Timestamp registerDatetime;
    private Timestamp lastUpdateTime;

    public Member(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getRegisterDatetime() {
        return registerDatetime;
    }

    public void setRegisterDatetime(Timestamp registerDatetime) {
        this.registerDatetime = registerDatetime;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
