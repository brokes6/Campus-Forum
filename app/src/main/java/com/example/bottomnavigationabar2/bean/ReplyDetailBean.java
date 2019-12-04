package com.example.bottomnavigationabar2.bean;


/**
 * create by czj 2019/10/23
 */
public class ReplyDetailBean {
    private String username;
    private String uimg;
    private int uid;
    private String rcreateTime;
    private String content;
    private int love_count;

    public ReplyDetailBean(String username, String content,String rcreateTime) {
        this.username = username;
        this.content = content;
        this.rcreateTime=rcreateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getRcreateTime() {
        return rcreateTime;
    }

    public void setRcreateTime(String rcreateTime) {
        this.rcreateTime = rcreateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLove_count() {
        return love_count;
    }

    public void setLove_count(int love_count) {
        this.love_count = love_count;
    }
}
