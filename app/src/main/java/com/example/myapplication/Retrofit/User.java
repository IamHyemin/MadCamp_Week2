package com.example.myapplication.Retrofit;

import java.util.ArrayList;

public class User {
    private String name;
    private String phoneNum;
    private String email;
    private String password;
    private Double[] position = new Double[]{36.372333, 127.360411};
    private String state = "want to find friends";
    private String[] likeList = {};
    private String[] friendsList = {};

    public User( String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String password, String phoneNum, String state, String[] likeList ){
        this.name = name;
        this.password = password;
        this.phoneNum = phoneNum;
        this.state = state;
        this.likeList = likeList;
    }

    public User(String name, String email, String password,  Double[] position, String phoneNum, String state, String[] likeList, String[] friendsList ){
        this.name = name;
        this.email = email;
        this.position = position;
        this.friendsList = friendsList;
        this.password = password;
        this.phoneNum = phoneNum;
        this.state = state;
        this.likeList = likeList;
    }

    public String getName(){ return name; }
    public String getPhoneNum(){ return phoneNum; }
    public String getEmail(){ return email; }
    public String getPassword(){ return password; }
    public Double[] getPosition(){ return position; }
    public String getState(){ return state; }
    public String[] getLikeList(){ return likeList; }
    public String[] getFriendsList(){ return friendsList; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFriendsList(String[] friendsList) {
        this.friendsList = friendsList;
    }

    public void setLikeList(String[] likeList) {
        this.likeList = likeList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setPosition(Double[] position) {
        this.position = position;
    }

    public void setState(String state) {
        this.state = state;
    }
}
