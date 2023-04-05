package com.example.workoutapp;

/**
 * 사용자 계정 정보 모델 클래스
 */
public class UserAccount
{
    private String idToken; // Firebase Uid (고유id 토큰)
    private String emailId;
    private String password;
    private String name;
    private String phone;
    private String salt;


    public UserAccount() { }
    // Firebase에서 Realtime DB 에서 모델 클래스로 가져올 때 빈 생성자 필요

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
